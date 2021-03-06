package ua.com.taxi.entity;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * @author Geniy00
 */
//TODO: rename this class to have better self description
@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private BookingRequestDetails bookingRequest;

    @OneToOne(cascade = CascadeType.ALL)
    private ClientDetails client;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "assignToExpiryTime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime assignToExpiryTime;

    @Column(name = "reason")
    private String reason;

    @Version
    @Column(name = "lockVersion")
    private Long version;

    public enum Status {
        NEW, ASSIGNED, REVOKED, ACCEPTED, REJECTED, REFUSED, EXPIRED
    }

    public Booking() {
        status = Status.NEW;
    }

    public Booking(BookingRequestDetails bookingRequestDetails) {
        this.bookingRequest = bookingRequestDetails;
        this.status = Status.NEW;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BookingRequestDetails getBookingRequest() {
        return bookingRequest;
    }

    public void setBookingRequest(BookingRequestDetails bookingRequest) {
        this.bookingRequest = bookingRequest;
    }

    public ClientDetails getClient() {
        return client;
    }

    public void setClient(ClientDetails client) {
        this.client = client;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public DateTime getAssignToExpiryTime() {
        return assignToExpiryTime;
    }

    public void setAssignToExpiryTime(DateTime assignToExpiryTime) {
        this.assignToExpiryTime = assignToExpiryTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(final Long version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Booking booking = (Booking) o;

        if (bookingRequest != null ? !bookingRequest.equals(booking.bookingRequest) : booking.bookingRequest != null)
            return false;
        if (client != null ? !client.equals(booking.client) : booking.client != null) return false;
        if (id != null ? !id.equals(booking.id) : booking.id != null) return false;
        if (reason != null ? !reason.equals(booking.reason) : booking.reason != null) return false;
        if (status != booking.status) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (bookingRequest != null ? bookingRequest.hashCode() : 0);
        result = 31 * result + (client != null ? client.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (reason != null ? reason.hashCode() : 0);
        return result;
    }
}
