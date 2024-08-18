


package com.example.daang.Service;


import com.example.daang.Model.BookingDetails;
import com.example.daang.Model.Room;

import java.time.LocalDate;
import java.util.List;

public interface BookingDetailsService {
    List<BookingDetails> getAllBookings();
    BookingDetails getBookingById(Long bookingId);
    List<BookingDetails> getBookingsByGuestName(String guestName);
    List<BookingDetails> getBookingsStartingAfter(String date);
    List<BookingDetails> getBookingsEndingBefore(String date);
    
    void bookRoom(BookingDetails bookingDetails);
}
