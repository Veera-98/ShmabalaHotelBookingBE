



package com.example.daang.Controller;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.daang.Model.BookingDetails;
import com.example.daang.Model.BookingMember;
import com.example.daang.Model.Feedback;
import com.example.daang.Model.Invoice;
import com.example.daang.Model.Member;
import com.example.daang.Model.Room;
import com.example.daang.Model.User;
import com.example.daang.Repository.BookingDetailsRepository;
import com.example.daang.Repository.BookingMemberRepository;
import com.example.daang.Repository.FeedBackRepository;
import com.example.daang.Repository.InvoiceRepository;
import com.example.daang.Repository.MemberRepository;
import com.example.daang.Repository.RoomRepository;
import com.example.daang.Repository.UserRepository;
import com.example.daang.Service.BookingDetailsService;
import com.example.daang.dto.BookingDetailsdto;
import com.example.daang.dto.BookingFormData;

@RestController
@RequestMapping("/api/hotel-staff")
@CrossOrigin
public class HotelStaffController {
	
	@Autowired
	FeedBackRepository feedBackRepository;

	@Autowired
    UserRepository userRepository;
	@Autowired
    MemberRepository memberRepository;
    @Autowired 
    BookingMemberRepository bookingMemberRepository;
    
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    InvoiceRepository invoiceRepository;
    @Autowired
    BookingDetailsRepository bookingDetailsRepository;
    @Autowired
    BookingDetailsService bookingDetailsService;

    // Sample data for room inventory management
    private List<Room> rooms = new ArrayList<>();

    // Sample data for room availability management
    private Map<Long, Boolean> roomAvailability = new HashMap<>();
    private List<Invoice> invoices = new ArrayList<>();
    // Sample data for billing and invoicing
    private Map<Long, Double> roomPrices = new HashMap<>(); // Mapping room ID to price
    // Sample data for walk-in reservations
    private List<BookingDetails> walkInReservations = new ArrayList<>();

    @PostMapping("/add-room")
    public ResponseEntity<String> addRoom(@RequestBody Room newRoom) {
        Long roomId = newRoom.getRoom_id();

        // Check if room ID already exists
        if (roomRepository.existsById(roomId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Room with ID " + roomId + " already exists.");
        }

        // Save the new room
        roomRepository.save(newRoom);

        return ResponseEntity.ok("Room added successfully");
    }

    // Get all rooms
    @GetMapping("/get-rooms")
    public ResponseEntity<List<Room>> getRooms() {
        List<Room> rooms = roomRepository.findAll();
        return ResponseEntity.ok(rooms);
    }

    @PutMapping("/update-room/{roomId}")
    public ResponseEntity<String> updateRoom(@PathVariable Long roomId, @RequestBody Room updatedRoom) {
        Optional<Room> updateRoom = roomRepository.findById(roomId);
        if (updateRoom.isPresent()) {
            updateRoom.get().setType(updatedRoom.getType());
            updateRoom.get().setCapacity(updatedRoom.getCapacity());
            roomRepository.save(updateRoom.get());

            return ResponseEntity.status(HttpStatus.OK).body("Room updated");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room not found");
    }

    // Delete room based on the given room ID
    @DeleteMapping("/delete-room/{roomId}")
    public ResponseEntity<String> deleteRoom(@PathVariable Long roomId) {
        // Check if the room with the given ID exists
        if (roomRepository.existsById(roomId)) {
            // Delete the room
            roomRepository.deleteById(roomId);
            return ResponseEntity.ok("Room with ID " + roomId + " deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room with ID " + roomId + " not found");
        }
    }


    // Get room availability
    @GetMapping("/get-room-availability/{roomId}")
    public ResponseEntity<Boolean> getRoomAvailability(@PathVariable Long roomId) {
        Boolean availability = roomAvailability.getOrDefault(roomId, false); // Default to false if room not found
        return ResponseEntity.ok(availability);
    }

    // Update room availability
    @PutMapping("/update-room-availability/{roomId}/{status}")
    public ResponseEntity<String> updateRoomAvailability(@PathVariable Long roomId, @PathVariable Boolean status) {
        // Logic to update room availability status
        roomAvailability.put(roomId, status);
        return ResponseEntity.ok("Room availability updated successfully");
    }

    @PostMapping("/walk-in-reservation")
    public ResponseEntity<String> walkInReservation(@RequestBody BookingDetailsdto bookdto) {
        try {
            // Save booking details to generate booking ID
            BookingDetails bookingDetails = new BookingDetails();
            User user = userRepository.findByUserid(bookdto.getUserid());

            bookingDetails.setEndDate(bookdto.getEndDate());
            bookingDetails.setStartDate(bookdto.getStartDate());
            bookingDetails.setUser(user);
            bookingDetails.setRoom(bookdto.getRoom());
            bookingDetails.setIsdeleted(bookdto.isIsdeleted());
            bookingDetails.setBookedDate(bookdto.getBookedDate());

            // Save booking details
            BookingDetails savedBooking = bookingDetailsRepository.save(bookingDetails);
            Long bookingId = savedBooking.getBooking_id();
            // Iterate through members and save them individually
            List<Member> savedMembers = new ArrayList<>();
            for (Member member : bookdto.getMembers()) {
                Member savedMember = memberRepository.save(member);
                savedMembers.add(savedMember);
            }

            // Associate members with the booking and save the association
            for (Member savedMember : savedMembers) {
                BookingMember bookingMember = new BookingMember();
                bookingMember.setBooking(savedBooking);
                bookingMember.setMember(savedMember);
                bookingMemberRepository.save(bookingMember);
            }

            return ResponseEntity.ok(bookingId.toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error occurred while processing the reservation");
        }
    }

    @GetMapping("/get-walk-in-reservations")
    public ResponseEntity<List<BookingDetails>> getWalkInReservations() {
        // Return the list of walk-in reservations
        List<BookingDetails> bookingDetails = bookingDetailsService.getAllBookings();
        return ResponseEntity.ok(bookingDetails);
    }

    // Generate bill for a guest based on their booking
    @PostMapping("/generate-bill/{bookingId}")
    public ResponseEntity<String> generateBill(@PathVariable Long bookingId) {
        // Retrieve booking details by ID
        Optional<BookingDetails> bookingDetailsOptional = bookingDetailsRepository.findById(bookingId);

        // Check if booking details exist
        if (bookingDetailsOptional.isPresent()) {
            BookingDetails bookingDetails = bookingDetailsOptional.get();
            Long roomId = bookingDetails.getRoom().getRoom_id();

            // Retrieve room price from the database
            Optional<Room> roomOptional = roomRepository.findById(roomId);

            // Check if room exists
            if (roomOptional.isPresent()) {
                Room room = roomOptional.get();
                Double roomPrice = room.getPrice();

                // Check if room price is valid
                if (roomPrice != null && roomPrice > 0.0) {
                    // Additional logic for generating bill...
                    // For demonstration purposes, let's assume a fixed service charge of $50
                    Double serviceCharge = 50.0;
                    Double totalCost = roomPrice + serviceCharge;

                    // Return the bill along with the room price
                    return ResponseEntity.ok("Bill generated successfully. Room ID: " + roomId + ", Room price: $" + roomPrice + ", Service charge: $" + serviceCharge + ", Total cost: $" + totalCost);
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Invalid room price for room ID: " + roomId);
                }
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Room not found for ID: " + roomId);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking details not found for ID: " + bookingId);
        }
    }

    
    
    @GetMapping("/bookingDetails/{bookingId}")
    public ResponseEntity<BookingDetails> generateInvoice(@PathVariable Long bookingId) {
        // Retrieve booking details from the database based on the booking ID
        Optional<BookingDetails> bookingDetailsOptional = bookingDetailsRepository.findById(bookingId);
        if (bookingDetailsOptional.isPresent()) {
            BookingDetails bookingDetails = bookingDetailsOptional.get();
            return ResponseEntity.ok(bookingDetails);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    
    
    
    @GetMapping("/bookingDetails/user/{userId}")
    public ResponseEntity<List<BookingDetails>> getBookingDetailsByUserId(@PathVariable String userId) {
    	User uses=userRepository.findByUserid(userId);
        List<BookingDetails> bookingDetails = bookingDetailsRepository.findByUserId(uses.getId());
        return ResponseEntity.ok(bookingDetails);
    }
    

    @GetMapping("/bookingDetails/all")
    public ResponseEntity<List<BookingDetails>> getBookingDetails() {
    	
        List<BookingDetails> bookingDetails = bookingDetailsRepository.findAll();
        return ResponseEntity.ok(bookingDetails);
    }
    
    
    @GetMapping("/bookingDetails/delete/{bookingId}")
    public ResponseEntity<List<BookingDetails>> deletebookingdetails() {
    	
        List<BookingDetails> bookingDetails = bookingDetailsRepository.findAll();
        return ResponseEntity.ok(bookingDetails);
    }
    
    @PutMapping("/bookingDetails/delete/{bookingId}")
    public ResponseEntity<?> updateIsDeletedStatus(@PathVariable Long bookingId) {
        Optional<BookingDetails> bookingDetailsOptional = bookingDetailsRepository.findById(bookingId);
        if (bookingDetailsOptional.isPresent()) {
            BookingDetails bookingDetails = bookingDetailsOptional.get();
            bookingDetails.setIsdeleted(true); // Set isdeleted flag to true
            bookingDetailsRepository.save(bookingDetails); // Save the updated booking details
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/save/feedback")
    public ResponseEntity<?> saveFeedback(@RequestBody Feedback feedback) {
        try {
            // Check if a feedback for the given booking ID already exists
            Optional<Feedback> existingFeedback = feedBackRepository.findByBookingId(feedback.getBooking().getBooking_id());
            if (existingFeedback.isPresent()) {
                // Feedback for the booking already exists, return a message indicating so
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Feedback for this booking already exists");
            } else {
                // Save the new feedback entry
                Feedback savedFeedback = feedBackRepository.save(feedback);
                return ResponseEntity.status(HttpStatus.CREATED).body("Feedback submitted successfully");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving feedback: " + e.getMessage());
        }
    }
    
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable String userId) {
        // Find the user by ID in the repository
        User user = userRepository.findByUserid(userId);
        
        // Check if the user exists
        if (user != null) {
            return ResponseEntity.ok(user); // Return the user details with status 200 OK
        } else {
            return ResponseEntity.notFound().build(); // Return 404 Not Found if user does not exist
        }
    }
    
    @PostMapping("/save/invoice")
    public ResponseEntity<Invoice> saveInvoice(@RequestBody Invoice invoice) {
        try {
            Optional<Invoice> existingInvoice = invoiceRepository.findByBookingId(invoice.getBooking().getBooking_id());
            if (existingInvoice.isPresent()) {
                // If an invoice with the same booking ID already exists, return the existing invoice
                return ResponseEntity.ok(existingInvoice.get());
            } else {
                // Otherwise, save the new invoice
                Invoice savedInvoice = invoiceRepository.save(invoice);
                return new ResponseEntity<>(savedInvoice, HttpStatus.CREATED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    @GetMapping("/reports/{userId}")
    public Map<String, List<?>> getBookingDetails(@PathVariable String userId) {
        // Define the date range (start date is inclusive, end date is exclusive)
    	LocalDate startDate =LocalDate.now(); // Start date is today
        LocalDate endDate = startDate.plusDays(1); // End date is tomorrow

        // Get the count of occupied rooms
        LocalDate today = LocalDate.now();
        List<BookingDetails> bdetails=bookingDetailsRepository.findAll()
                .stream()
                .filter(booking -> booking.getUser().getUserid().equals(userId) &&
                        !booking.isIsdeleted() &&
                        booking.getStartDate().isBefore(today) &&
                        booking.getEndDate().isAfter(today))
                .collect(Collectors.toList());

        // Get the count of empty rooms
        List<Room> emptyRooms = bookingDetailsRepository.findEmptyRooms(startDate, endDate);

        List<Integer> roomIds = Arrays.asList(1, 30); // Assuming room IDs 1 and 30

        // Get the count of empty rooms for specified room IDs
//        long emptyRoomsCount1 = bookingDetailsRepository.roomscoutning(roomIds);
        
        
        
        List<BookingDetails> walkInReservations = bookingDetailsRepository.findWalkInReservations(startDate);

        // Get the count of online reservations for today
        List<BookingDetails> onlineReservations=bookingDetailsRepository.findOnlineReservations(startDate);
        // Create a map to hold the counts
        Map<String, List<?>> countsMap = new HashMap<>();
        countsMap.put("occupiedRooms", bdetails);
        countsMap.put("emptyRooms", emptyRooms);
        countsMap.put("walkInReservations", walkInReservations);
        countsMap.put("onlineReservations", onlineReservations);

        return countsMap;
    }
    
    
    @PostMapping("/bookingDetails")
    public ResponseEntity<String> createBooking( @RequestBody BookingFormData formData) {
        // Fetch all rooms
    	
    	 BookingDetails bookingDetails1 = bookingDetailsRepository.findByBookingId(formData.getBooking_id());
    	Long roomid= bookingDetails1.getRoom().getRoom_id();
    	
    	 String type = formData.getRoomType();
    	 boolean roomfree=bookingDetailsRepository.isRoomFree(roomid,formData.getStartDate() ,formData.getEndDate());
    	 
    	 if(roomfree)
    	 {
    		 
    		 BookingDetails bookingDetails = bookingDetailsRepository.findByBookingId(formData.getBooking_id());
    		
     	    bookingDetails.setStartDate(formData.getStartDate());
     	    bookingDetails.setEndDate(formData.getEndDate());
     	  
     	    
     	    // Save the updated BookingDetails entity
     	    bookingDetailsRepository.save(bookingDetails);
     	    
     	   return ResponseEntity.ok("bookede");
    		 
    	 }
    	 
    	 
    	 else if(!type.isEmpty())
    {
           List<Room> rooms = roomRepository.findAll();
        

        // Filter rooms by type and map them to their IDs
       
            List<Long> roomIds = rooms.stream()
                                  .filter(room -> room.getType().equalsIgnoreCase(type))
                                  .map(Room::getRoom_id)
                                  .collect(Collectors.toList());
        
        // Find booked room IDs between the specified dates
           List<Long> bookedRoomIds = bookingDetailsRepository.findBookedRoomIdsBetweenDates(formData.getStartDate(), formData.getEndDate());

        // Find available room IDs by excluding booked rooms
        List<Long> availableRoomIdsForDates = roomIds.stream()
                                                     .filter(roomId -> !bookedRoomIds.contains(roomId))
                                                     .collect(Collectors.toList());

        if (availableRoomIdsForDates.isEmpty()) {
            // No available rooms found
            return ResponseEntity.noContent().build();
        } else {
            
        	System.out.println(availableRoomIdsForDates);
        	 Long selectedRoomId = availableRoomIdsForDates.get(new Random().nextInt(availableRoomIdsForDates.size()));
        	 Long bookingId = formData.getBooking_id();
             BookingDetails bookingDetails = bookingDetailsRepository.findByBookingId(bookingId);
             // Print the selected room ID
             System.out.println("Selected Room ID: " + selectedRoomId);
             Room room = roomRepository.findById(selectedRoomId).orElse(null);
             
             if (room != null) {
            	    // Set the room property of the bookingDetails object to the fetched Room entity
            	    bookingDetails.setRoom(room);
            	    bookingDetails.setStartDate(formData.getStartDate());
            	    bookingDetails.setEndDate(formData.getEndDate());
            	  
            	    
            	    // Save the updated BookingDetails entity
            	    bookingDetailsRepository.save(bookingDetails);
            	    
            	    System.out.println("BookingDetails updated with new room ID: " + selectedRoomId);
            	} else {
            	    System.out.println("Selected room with ID " + selectedRoomId + " not found.");
            	}
             
            return ResponseEntity.ok("bookede");
            
            
            
        }
    }
    	 else
    	 {
    		 
    		 Long bookingId = formData.getBooking_id();
    		 BookingDetails bookingDetails = bookingDetailsRepository.findByBookingId(bookingId);
    		 Long roomId=bookingDetails.getRoom().getRoom_id();
    		 int con = bookingDetailsRepository.countBookingsForRoomBetweenDates(roomId, formData.getStartDate(), formData.getEndDate());
    		 
    		 
    		 int overlapCount = bookingDetailsRepository.countOverlappingBookingsForRoomExcludingBookingId(roomId, bookingId, formData.getStartDate(), formData.getEndDate());
    		 
    		 if(overlapCount==0)
    		 {
    			 bookingDetails.setStartDate(formData.getStartDate());
         	    bookingDetails.setEndDate(formData.getEndDate());
         	   bookingDetailsRepository.save(bookingDetails);
         	  return ResponseEntity.ok("bookede");
    		 }
    		 else if(con>0)
    		 {
    			 return ResponseEntity.badRequest().body("Room is not available for booking within the specified dates");
    			 
    		 }
    		 else
    		 {
    			 bookingDetails.setStartDate(formData.getStartDate());
         	    bookingDetails.setEndDate(formData.getEndDate());
         	    bookingDetailsRepository.save(bookingDetails);
         	   return ResponseEntity.ok("bookede");
    		 }

    		 
    		
    		 
    	 }
		
    

    }
    
    
    
    
    @GetMapping("/feedbacklist/all")
    public ResponseEntity<List<Feedback>> getFeedbackDetails() {
    	
        List<Feedback> Feedbackdetails = feedBackRepository.findAll();
        return ResponseEntity.ok(Feedbackdetails);
    }
    
    



}


