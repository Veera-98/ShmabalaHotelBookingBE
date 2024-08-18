

package com.example.daang.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.daang.Model.Member;


@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {

 //   List<Invoice> findByBooking_BookingId(Long bookingId);
}
