

package com.example.daang.Service;

import com.example.daang.Model.Room;
import org.springframework.stereotype.Service;

import java.util.List;
@Service

public interface RoomService {

    List<Room> getAllRooms();
    
   
    Room getRoomById(Long roomId);
    List<Room> getRoomsByType(String type);
    List<Room> getRoomsByCapacity(Integer capacity);
//    List<Room> getRoomsByAmenity(String amenity);
    void addRoom(Room room);
    void updateRoom(Long roomId, Room updatedRoom);
    void deleteRoom(Long roomId);
}

