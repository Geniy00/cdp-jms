package com.epam.cdp.core.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

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
    @OneToMany(cascade = CascadeType.ALL)
    private List<BookingRequest> bookingRequests;

    @Enumerated(EnumType.STRING)
    @Column(name = "orderStatus", nullable = false)
    private OrderStatus orderStatus;


    //TODO: check if EXPIRED status is needed
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (!customer.equals(order.customer)) return false;
        if (!id.equals(order.id)) return false;
        if (orderStatus != order.orderStatus) return false;
        if (!reservationRequest.equals(order.reservationRequest)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + customer.hashCode();
        result = 31 * result + reservationRequest.hashCode();
        result = 31 * result + orderStatus.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", customer=" + customer +
                ", reservationRequest=" + reservationRequest +
                ", bookingRequests=" + bookingRequests +
                '}';
    }
}
