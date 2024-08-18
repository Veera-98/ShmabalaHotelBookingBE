

package com.example.daang.Service;

import com.example.daang.Model.Room;
import com.example.daang.Repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService{

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public Room getRoomById(Long roomId) {
        return roomRepository.findById(roomId).orElse(null);
    }

    @Override
    public List<Room> getRoomsByType(String type) {
        return roomRepository.findByType(type);
    }

    @Override
    public List<Room> getRoomsByCapacity(Integer capacity) {
        return roomRepository.findByCapacityGreaterThanEqual(capacity);
    }


    @Override
    public void addRoom(Room room) {
        roomRepository.save(room);
    }

    @Override
    public void updateRoom(Long roomId, Room updatedRoom) {
        // Check if the room exists before updating
        Room existingRoom = roomRepository.findById(roomId).orElse(null);
        if (existingRoom != null) {
            updatedRoom.setRoom_id(roomId);
            roomRepository.save(updatedRoom);
        }
    }

    @Override
    public void deleteRoom(Long roomId) {
        roomRepository.deleteById(roomId);
    }
}
