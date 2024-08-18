
package com.example.daang.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.daang.Model.BookingDetails;
import com.example.daang.Model.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

	Optional<Invoice> findByBooking(BookingDetails booking);

	@Query("SELECT i FROM Invoice i WHERE i.booking.booking_id = :bookingId")
    Optional<Invoice> findByBookingId(Long bookingId);
}
