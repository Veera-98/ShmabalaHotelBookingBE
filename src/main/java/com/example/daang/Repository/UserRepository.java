package com.example.daang.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.daang.Model.User;



@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

 //   List<Invoice> findByBooking_BookingId(Long bookingId);
	
	
	User findByUserid(String userid);
}
