
package com.example.daang.Service;

import com.example.daang.Model.BookingDetails;
import com.example.daang.Model.Room;
import com.example.daang.Repository.BookingDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class BookingDetailsServiceImpl implements BookingDetailsService {

    @Autowired
    private BookingDetailsRepository bookingDetailsRepository;

    @Override
    public List<BookingDetails> getAllBookings() {
        return bookingDetailsRepository.findAll();
    }

    @Override
    public BookingDetails getBookingById(Long bookingId) {
        return bookingDetailsRepository.findById(bookingId).orElse(null);
    }

    

    @Override
    public List<BookingDetails> getBookingsStartingAfter(String date) {
        return bookingDetailsRepository.findByStartDateAfter(date);
    }

    

    @Override
    public void bookRoom(BookingDetails bookingDetails) {
        bookingDetailsRepository.save(bookingDetails);
    }

	@Override
	public List<BookingDetails> getBookingsByGuestName(String guestName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BookingDetails> getBookingsEndingBefore(String date) {
		// TODO Auto-generated method stub
		return null;
	}

	
}

