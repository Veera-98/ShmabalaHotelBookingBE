

package com.example.daang.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.daang.Model.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByType(String type);

    List<Room> findByCapacityGreaterThanEqual(Integer capacity);


}
