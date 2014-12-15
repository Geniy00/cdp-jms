package com.epam.cdp.core.entity;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Request that is created by router and was sent to taxi service through TaxiInputQueue
 *
 * @author Geniy00
 */
@Entity
@Table(name = "booking_request")
public class BookingRequest implements Serializable {

    private static final long serialVersionUID = 1820235611121505291L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "startPosition", nullable = false)
    private Integer startPosition;

    @Column(name = "finishPosition", nullable = false)
    private Integer finishPosition;

    @Column(name = "deliveryTime", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime deliveryTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicleType", nullable = false)
    private VehicleType vehicleType;

    @Column(name = "payment", nullable = false)
    private Double payment;

    @Column(name = "expiryTime", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime expiryTime;

    @ManyToOne(optional = false)
    private TaxiDispatcher taxiDispatcher;

    //TODO: there is possibility where several responses have to linked to a BookingRequest (i.e. ACCEPT, REFUSE)
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private BookingResponse bookingResponse;

    @Column(name = "created", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime created;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Order order;

    public BookingRequest() {
    }

    public BookingRequest(Order order, TaxiDispatcher taxiDispatcher, Double payment, DateTime expiryTime) {
        this.startPosition = order.getReservationRequest().getStartPosition();
        this.finishPosition = order.getReservationRequest().getFinishPosition();
        this.deliveryTime = order.getReservationRequest().getDeliveryTime();
        this.vehicleType = order.getReservationRequest().getVehicleType();

        this.payment = payment;
        this.expiryTime = expiryTime;
        this.taxiDispatcher = taxiDispatcher;
        this.order = order;
        this.customer = order.getCustomer();
    }

    @PrePersist
    protected void updateDates() {
        this.created = new DateTime();
    }

    //TODO: refactor it
    public void applyBookingResponse(BookingResponse bookingResponse) {
        this.bookingResponse = bookingResponse;
        final BookingRequestEnum.Status status = bookingResponse.getStatus();
        switch (status) {
        case ACCEPTED:
            order.setOrderStatus(Order.OrderStatus.PROCESSED);
            break;
        case REJECTED:
            order.setOrderStatus(Order.OrderStatus.DECLINED);
            break;
        case REFUSED:
            order.setOrderStatus(Order.OrderStatus.CANCELED);
            break;
        case EXPIRED:
            order.setOrderStatus(Order.OrderStatus.EXPIRED);
            break;
        case FAILED:
            order.setOrderStatus(Order.OrderStatus.FAILED);
            break;
        default:
            throw new RuntimeException("Unknown bookingResponse status");
        }
        this.bookingResponse = bookingResponse;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(Integer startPosition) {
        this.startPosition = startPosition;
    }

    public Integer getFinishPosition() {
        return finishPosition;
    }

    public void setFinishPosition(Integer finishPosition) {
        this.finishPosition = finishPosition;
    }

    public DateTime getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(DateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Double getPayment() {
        return payment;
    }

    public void setPayment(Double payment) {
        this.payment = payment;
    }

    public DateTime getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(DateTime expiryTime) {
        this.expiryTime = expiryTime;
    }

    public TaxiDispatcher getTaxiDispatcher() {
        return taxiDispatcher;
    }

    public void setTaxiDispatcher(TaxiDispatcher taxiDispatcher) {
        this.taxiDispatcher = taxiDispatcher;
    }

    public BookingResponse getBookingResponse() {
        return bookingResponse;
    }

    public void setBookingResponse(BookingResponse bookingResponse) {
        this.bookingResponse = bookingResponse;
    }

    public DateTime getCreated() {
        return created;
    }

    public void setCreated(DateTime created) {
        this.created = created;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        BookingRequest that = (BookingRequest) obj;

        return Objects.equals(this.id, that.id) && Objects.equals(this.startPosition, that.startPosition) && Objects
                .equals(this.finishPosition, that.finishPosition) && Objects.equals(this.deliveryTime,
                that.deliveryTime) && Objects.equals(this.vehicleType, that.vehicleType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startPosition, finishPosition, deliveryTime, vehicleType);
    }
}
