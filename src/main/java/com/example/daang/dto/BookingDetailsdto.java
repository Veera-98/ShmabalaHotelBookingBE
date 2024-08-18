package com.example.daang.dto;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.example.daang.Model.Member;
import com.example.daang.Model.Room;
import com.example.daang.Model.User;

public class BookingDetailsdto {
	 
	    

	    
	    private Room room;
	    

	    
	    private List<Member> members;

	    

	    
		private LocalDate startDate;
		private LocalDate endDate;
		
		private LocalDate bookedDate;
		
		
	    
	    public LocalDate getBookedDate() {
			return bookedDate;
		}




		public void setBookedDate(LocalDate bookedDate) {
			this.bookedDate = bookedDate;
		}




		private boolean isdeleted;
	    
	    public boolean isIsdeleted() {
			return isdeleted;
		}




		public void setIsdeleted(boolean isdeleted) {
			this.isdeleted = isdeleted;
		}




		private  String userid;


		public Room getRoom() {
			return room;
		}


		public void setRoom(Room room) {
			this.room = room;
		}


		public List<Member> getMembers() {
			return members;
		}


		public void setMembers(List<Member> members) {
			this.members = members;
		}


		


		public LocalDate getStartDate() {
			return startDate;
		}


		public void setStartDate(LocalDate startDate) {
			this.startDate = startDate;
		}


		public LocalDate getEndDate() {
			return endDate;
		}


		public void setEndDate(LocalDate endDate) {
			this.endDate = endDate;
		}


		public String getUserid() {
			return userid;
		}


		public void setUserid(String userid) {
			this.userid = userid;
		}
	    
		
}
