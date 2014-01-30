package com.epam.cdp.core.entity;

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 *
 * @author Geniy00
 */
@Entity
@Table(name="booking_response")
public class BookingResponse implements Serializable{
	private static final long serialVersionUID = -4490123034598037678L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;

    @OneToOne
	private TaxiDispatcher taxiDispatcher;

    @OneToOne
    private BookingRequest bookingRequest;

    @Enumerated(EnumType.STRING)
    @Column(name="bookingResponseStatus")
    private BookingResponseStatus bookingResponseStatus;

    @Column(name="reason")
    private String reason;

	@Column(name="dateTime")
	@Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
	private DateTime dateTime;
	

	public enum BookingResponseStatus {
		ACCEPTED, REJECTED, REFUSED, EXPIRED, FAILURE;
	}

	public BookingResponse() { }
	
	public BookingResponse(BookingResponseStatus bookingResponseStatus, String reason,
                           TaxiDispatcher taxiDispatcher) {
		super();
		this.bookingResponseStatus = bookingResponseStatus;
		this.reason = reason;
		this.taxiDispatcher = taxiDispatcher;
		this.dateTime = new DateTime();
	}


}