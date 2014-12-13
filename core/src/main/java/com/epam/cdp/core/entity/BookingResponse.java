package com.epam.cdp.core.entity;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

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

    public BookingResponse(BookingRequest bookingRequest, BookingRequestEnum.Status status) {
        this.bookingRequest = bookingRequest;
        this.status = status;
        this.reason = "";
    }

    @PrePersist
    protected void updateDates() {
        created = new DateTime();
    }

    public BookingResponse(BookingRequest bookingRequest, BookingRequestEnum.Status status, String reason) {
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
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        BookingResponse that = (BookingResponse) obj;

        return Objects.equals(this.id, that.id)
                && Objects.equals(this.bookingRequest, that.bookingRequest)
                && Objects.equals(this.status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bookingRequest, status);
    }
}