package com.epam.cdp.core.entity;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Geniy00
 */
@Entity
@Table(name = "booking_response")
public class BookingResponse implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    private BookingRequest bookingRequest;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingRequestEnum.Status status;

    @Column(name = "reason")
    private String reason;

    @Column(name = "created", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime created;


    public BookingResponse() {
    }

    public BookingResponse(BookingRequest bookingRequest, BookingRequestEnum.Status status){
        this.bookingRequest = bookingRequest;
        this.status = status;
        this.reason = "";
    }

    @PrePersist
    protected void updateDates() {
        created = new DateTime();
    }

    public BookingResponse(BookingRequest bookingRequest, BookingRequestEnum.Status status, String reason){
        this.bookingRequest = bookingRequest;
        this.status = status;
        this.reason = reason;
        created = new DateTime();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BookingRequest getBookingRequest() {
        return bookingRequest;
    }

    public void setBookingRequest(BookingRequest bookingRequest) {
        this.bookingRequest = bookingRequest;
    }

    public BookingRequestEnum.Status getStatus() {
        return status;
    }

    public void setStatus(BookingRequestEnum.Status status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public DateTime getCreated() {
        return created;
    }

    public void setCreated(DateTime created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookingResponse that = (BookingResponse) o;

        if (!bookingRequest.equals(that.bookingRequest)) return false;
        if (status != that.status) return false;
        if (!id.equals(that.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + bookingRequest.hashCode();
        result = 31 * result + status.hashCode();
        return result;
    }
}