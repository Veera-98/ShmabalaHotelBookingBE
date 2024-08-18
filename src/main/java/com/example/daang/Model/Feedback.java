

package com.example.daang.Model;

import javax.persistence.*;

@Entity
@Table(name = "feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedback_id;

    private String guest_name;
    private String feedback_text;
    private Integer rating;
    
    
    @ManyToOne
    @JoinColumn(name = "booking_id")
    private BookingDetails booking;
    
    

    public BookingDetails getBooking() {
		return booking;
	}

	public void setBooking(BookingDetails booking) {
		this.booking = booking;
	}

	public Feedback() {
        // Default constructor required by JPA
    }

    public Feedback(String guest_name, String feedback_text, Integer rating) {
        this.guest_name = guest_name;
        this.feedback_text = feedback_text;
        this.rating = rating;
    }

    public Long getFeedback_id() {
        return feedback_id;
    }

    public void setFeedback_id(Long feedback_id) {
        this.feedback_id = feedback_id;
    }

    public String getGuest_name() {
        return guest_name;
    }

    public void setGuest_name(String guest_name) {
        this.guest_name = guest_name;
    }

    public String getFeedback_text() {
        return feedback_text;
    }

    public void setFeedback_text(String feedback_text) {
        this.feedback_text = feedback_text;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "feedback_id=" + feedback_id +
                ", guest_name='" + guest_name + '\'' +
                ", feedback_text='" + feedback_text + '\'' +
                ", rating=" + rating +
                '}';
    }
}
