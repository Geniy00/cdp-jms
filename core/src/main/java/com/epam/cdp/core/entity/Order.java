package com.epam.cdp.core.entity;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 *
 * @author Geniy00
 */
@Entity
@Table(name="ord")
public class Order implements Serializable {

	private static final long serialVersionUID = 1820235678421505291L;
	
	@Id
	@Column(name="id")
	private String id;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	private Customer customer;
	
	@OneToOne (cascade = CascadeType.ALL)
    private ReservationRequest reservationRequest;

    // TODO: check orphanRemoval = true!!!
    @OneToMany (cascade = CascadeType.ALL)
    private List<BookingRequest> bookingRequests;

    @Enumerated(EnumType.STRING)
    @Column(name="orderStatus")
    private OrderStatus orderStatus;


    //TODO: check if EXPIRED status is needed
    public enum OrderStatus{
        /**
         * Order status meaning
         * NEW - just received from sender module
         * SENT - order was sent to taxi service and processing by it
         * DECLINED - order was rejected by taxi service
         * PROCESSED - order was processed by some taxi service
         * FINISHED - order was processed N hours ago (i.e. 24 hours ago)
         *
         * Exceptional statuses:
         * EXPIRED - order can't be sent because delivery time is expired
         * CANCELED - order was canceled
         */
        NEW, SENT, DECLINED, PROCESSED, FINISHED, EXPIRED, CANCELED
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

    public void addBookingRequest(BookingRequest bookingRequest){
        this.bookingRequests.add(bookingRequest);
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

        if (bookingRequests != null ? !bookingRequests.equals(order.bookingRequests) : order.bookingRequests != null)
            return false;
        if (customer != null ? !customer.equals(order.customer) : order.customer != null) return false;
        if (id != null ? !id.equals(order.id) : order.id != null) return false;
        if (reservationRequest != null ? !reservationRequest.equals(order.reservationRequest) : order.reservationRequest != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (customer != null ? customer.hashCode() : 0);
        result = 31 * result + (reservationRequest != null ? reservationRequest.hashCode() : 0);
        result = 31 * result + (bookingRequests != null ? bookingRequests.hashCode() : 0);
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
