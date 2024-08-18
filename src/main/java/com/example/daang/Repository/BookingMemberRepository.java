

package com.example.daang.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.daang.Model.BookingMember;


@Repository
public interface BookingMemberRepository extends JpaRepository<BookingMember, Integer> {

 //   List<Invoice> findByBooking_BookingId(Long bookingId);
}
