

package com.example.daang.Repository;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.daang.Model.BookingDetails;
import com.example.daang.Model.Room;
import com.example.daang.Model.User;

@Repository
public interface BookingDetailsRepository extends JpaRepository<BookingDetails, Long> {

   
    List<BookingDetails> findByStartDateAfter(String date);

    
    @Query("SELECT bd FROM BookingDetails bd " +
            "WHERE bd.room = :room " +
            "AND bd.isdeleted = false " + // Added condition for isdeleted
            "AND (:startDate BETWEEN bd.startDate AND bd.endDate OR :endDate BETWEEN bd.startDate AND bd.endDate OR :startDate <= bd.startDate AND :endDate >= bd.endDate)")
    List<BookingDetails> findByRoomAndStartDateBetweenAndEndDateBetween(
            @Param("room") Room room,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    @Modifying
    @Transactional
    @Query(value = "UPDATE bookings SET isdeleted = true WHERE end_date < CURRENT_DATE", nativeQuery = true)
    void updateIsDeletedStatus();
    
    boolean existsByRoomAndIsdeletedIsTrue(Room room);
    
    List<BookingDetails> findByUserId(int i);
    @Query("SELECT COUNT(r) FROM Room r WHERE r.room_id NOT IN :roomIds")
    long roomscoutning(List<Integer> roomIds);

    @Query("SELECT b FROM BookingDetails b WHERE b.user.role_id = 1 AND b.bookedDate = :startDate AND b.endDate >= :startDate")
    List<BookingDetails> findWalkInReservations(@Param("startDate") LocalDate startDate);


    @Query("SELECT b FROM BookingDetails b WHERE b.user.role_id = 2 AND b.bookedDate = :startDate AND b.endDate >= :startDate")
    List<BookingDetails> findOnlineReservations(@Param("startDate") LocalDate startDate);

	long countByUserId(int id);

	 long countByUserAndStartDateEquals(User user, LocalDate startDate);
	

	 @Query("SELECT r FROM Room r WHERE NOT EXISTS "
	            + "(SELECT b FROM BookingDetails b WHERE b.room = r AND b.startDate <= :endDate AND b.endDate >= :startDate AND b.isdeleted = false)")
	    List<Room> findEmptyRooms(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
  
	    
	    @Query("SELECT DISTINCT b.room.room_id FROM BookingDetails b " +
	            "WHERE (b.startDate BETWEEN :startDate AND :endDate) OR " +
	            "(b.endDate BETWEEN :startDate AND :endDate)")
	     List<Long> findBookedRoomIdsBetweenDates(
	             @Param("startDate") LocalDate startDate,
	             @Param("endDate") LocalDate endDate
	     );
	    
	    @Query("SELECT bd FROM BookingDetails bd WHERE bd.booking_id = :bookingId")
	    BookingDetails findByBookingId(@Param("bookingId") Long bookingId);
	    
	    
	    
	    
	    @Query("SELECT COUNT(b) FROM BookingDetails b WHERE b.room.room_id = ?1 AND " +
	            "((?2 BETWEEN b.startDate AND b.endDate) OR (?3 BETWEEN b.startDate AND b.endDate) " +
	            "OR (b.startDate BETWEEN ?2 AND ?3) OR (b.endDate BETWEEN ?2 AND ?3))")
	    int countBookingsForRoomBetweenDates(Long roomId, LocalDate startDate, LocalDate endDate);
	    
	    @Query("SELECT COUNT(b) = 0 " +
	            "FROM BookingDetails b " +
	            "WHERE b.room.room_id = :roomId " +
	            "AND NOT (b.endDate < :startDate OR b.startDate > :endDate) " +
	            "AND b.isdeleted = false")
	     boolean isRoomFree(@Param("roomId") Long roomId,
	                        @Param("startDate") LocalDate startDate,
	                        @Param("endDate") LocalDate endDate);

	    
	    @Query("SELECT COUNT(bd) FROM BookingDetails bd WHERE bd.room.room_id = :roomId AND bd.booking_id != :bookingId AND (:startDate BETWEEN bd.startDate AND bd.endDate OR :endDate BETWEEN bd.startDate AND bd.endDate)")
	    int countOverlappingBookingsForRoomExcludingBookingId(Long roomId, Long bookingId, LocalDate startDate, LocalDate endDate);


		

}

