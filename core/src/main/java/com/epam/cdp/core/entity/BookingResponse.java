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

    private static final long serialVersionUID = 1820235611121505292L;

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

    public BookingResponse(final BookingRequest bookingRequest, final BookingRequestEnum.Status status,
            final DateTime createdTimestamp) {
        this(bookingRequest, status, "", createdTimestamp);
    }

    public BookingResponse(final BookingRequest bookingRequest, final BookingRequestEnum.Status status,
            final String reason, final DateTime createdTimestamp) {
        this.bookingRequest = bookingRequest;
        this.status = status;
        this.reason = reason;
        this.created = createdTimestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public BookingRequest getBookingRequest() {
        return bookingRequest;
    }

    public void setBookingRequest(final BookingRequest bookingRequest) {
        this.bookingRequest = bookingRequest;
    }

    public BookingRequestEnum.Status getStatus() {
        return status;
    }

    public void setStatus(final BookingRequestEnum.Status status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(final String reason) {
        this.reason = reason;
    }

    public DateTime getCreated() {
        return created;
    }

    public void setCreated(final DateTime created) {
        this.created = created;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        final BookingResponse that = (BookingResponse) obj;

        return Objects.equals(this.id, that.id) && Objects.equals(this.bookingRequest, that.bookingRequest) && Objects
                .equals(this.status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bookingRequest, status);
    }
}