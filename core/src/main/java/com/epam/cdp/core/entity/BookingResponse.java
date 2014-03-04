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
    private static final long serialVersionUID = -4490123034598037678L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    private TaxiDispatcher taxiDispatcher;

    @OneToOne
    private BookingRequest bookingRequest;

    @Enumerated(EnumType.STRING)
    @Column(name = "bookingResponseStatus")
    private BookingResponseStatus bookingResponseStatus;

    @Column(name = "reason")
    private String reason;

    @Column(name = "dateTime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime dateTime;


    public enum BookingResponseStatus {
        ACCEPTED, REJECTED, REFUSED, EXPIRED, FAILURE;
    }

    public BookingResponse() {
    }

    public BookingResponse(BookingResponseStatus bookingResponseStatus, String reason,
                           TaxiDispatcher taxiDispatcher) {
        super();
        this.bookingResponseStatus = bookingResponseStatus;
        this.reason = reason;
        this.taxiDispatcher = taxiDispatcher;
        this.dateTime = new DateTime();
    }


}