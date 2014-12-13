package com.epam.cdp.core.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author Geniy00
 */
@Entity
@Table(name = "ord")
public class Order implements Serializable {
    //TODO: add @Version annotation
    private static final long serialVersionUID = 1820235678421505291L;

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
    private Customer customer;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    private ReservationRequest reservationRequest;

    // TODO: check orphanRemoval = true!!!
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookingRequest> bookingRequests;

    @Enumerated(EnumType.STRING)
    @Column(name = "orderStatus", nullable = false)
    private OrderStatus orderStatus;

    //TODO: maybe extract from this class
    public enum OrderStatus {
        /**
         * Order status meaning
         * NEW - just received from sender module
         * SENT - order was sent to taxi service and processing by it
         * DECLINED - order was rejected by taxi service                  |taxi response: REJECTED
         * PROCESSED - order was processed by some taxi service           |taxi response: ACCEPTED
         * FINISHED - order was processed N hours ago (i.e. 24 hours ago)
         * <p/>
         * Exceptional statuses:
         * EXPIRED - order can't be sent because delivery time is expired |taxi response: EXPIRED
         * CANCELED - order was canceled                                  |taxi response: REFUSED
         * FAILED - order can't be processed by taxi service             |taxi response: FAILED
         */
        NEW, SENT, DECLINED, PROCESSED, FINISHED, EXPIRED, CANCELED, FAILED
    }

    public Order() {
    }

    public Order(String id, Customer customer, ReservationRequest reservationRequest) {
        this.id = id;
        this.customer = customer;
        this.reservationRequest = reservationRequest;
        bookingRequests = new LinkedList<>();
        orderStatus = OrderStatus.NEW;
    }

    //TODO: check if I can use some external class to change status
    public void applyBookingRequest(BookingRequest bookingRequest) {
        this.bookingRequests.add(bookingRequest);
        bookingRequest.setOrder(this);
        this.orderStatus = OrderStatus.SENT;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public ReservationRequest getReservationRequest() {
        return reservationRequest;
    }

    public void setReservationRequest(ReservationRequest reservationRequest) {
        this.reservationRequest = reservationRequest;
    }

    public List<BookingRequest> getBookingRequests() {
        return bookingRequests;
    }

    public void setBookingRequests(List<BookingRequest> bookingRequests) {
        this.bookingRequests = bookingRequests;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Order that = (Order) obj;

        return Objects.equals(this.id, that.id)
                && Objects.equals(this.customer, that.customer)
                && Objects.equals(this.orderStatus, that.orderStatus)
                && Objects.equals(this.reservationRequest, that.reservationRequest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customer, orderStatus, reservationRequest);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", customer=" + customer +
                ", reservationRequest=" + reservationRequest +
                ", bookingRequests=" + bookingRequests +
                ", orderStatus=" + orderStatus +
                '}';
    }
}
