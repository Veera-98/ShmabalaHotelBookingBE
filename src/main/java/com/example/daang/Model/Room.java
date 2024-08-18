

package com.example.daang.Model;


import java.util.List;

import javax.persistence.*;



@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long room_id;
    private long roomNo;
    private String type;
    private Integer capacity;
    private Double price; // Added price attribute

    public Room() {
    }

    public Room(String type, Integer capacity, Double price) {
        this.type = type;
        this.capacity = capacity;
        this.price = price;
    }

    public Long getRoom_id() {
        return room_id;
    }

    public void setRoom_id(Long room_id) {
        this.room_id = room_id;
    }

    public String getType() {
        return type;
    }

    
    public long getRoomNo() {
		return roomNo;
	}

	public void setRoomNo(long roomNo) {
		this.roomNo = roomNo;
	}

	public void setType(String type) {
        this.type = type;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

	@Override
	public String toString() {
		return "Room [room_id=" + room_id + ", roomNo=" + roomNo + ", type=" + type + ", capacity=" + capacity
				+ ", price=" + price + ", getRoom_id()=" + getRoom_id() + ", getType()=" + getType() + ", getRoomNo()="
				+ getRoomNo() + ", getCapacity()=" + getCapacity() + ", getPrice()=" + getPrice() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}
}

    


 