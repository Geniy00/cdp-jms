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

    //TODO: it's possible that several responses have to linked to a BookingRequest (i.e. ACCEPT, REFUSE)
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private BookingResponse bookingResponse;

    @Column(name = "created", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime created;

    @ManyToOne
    private Order order;

    public BookingRequest() {
    }

    public BookingRequest(final Order order, final TaxiDispatcher taxiDispatcher, final Double payment,
            final DateTime expiryTime, final DateTime createdTimestamp) {
        this.startPosition = order.getReservationRequest().getStartPosition();
        this.finishPosition = order.getReservationRequest().getFinishPosition();
        this.deliveryTime = order.getReservationRequest().getDeliveryTime();
        this.vehicleType = order.getReservationRequest().getVehicleType();

        this.payment = payment;
        this.expiryTime = expiryTime;
        this.taxiDispatcher = taxiDispatcher;
        this.order = order;
        this.created = createdTimestamp;
    }

    //TODO: refactor it
    public void applyBookingResponse(BookingResponse bookingResponse) {
        this.bookingResponse = bookingResponse;
        final Status responseStatus = bookingResponse.getStatus();
        switch (responseStatus) {
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
            throw new RuntimeException("Unknown responseStatus status " + responseStatus);
        }
        this.bookingResponse = bookingResponse;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Integer getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(final Integer startPosition) {
        this.startPosition = startPosition;
    }

    public Integer getFinishPosition() {
        return finishPosition;
    }

    public void setFinishPosition(final Integer finishPosition) {
        this.finishPosition = finishPosition;
    }

    public DateTime getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(final DateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(final VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Double getPayment() {
        return payment;
    }

    public void setPayment(final Double payment) {
        this.payment = payment;
    }

    public DateTime getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(final DateTime expiryTime) {
        this.expiryTime = expiryTime;
    }

    public TaxiDispatcher getTaxiDispatcher() {
        return taxiDispatcher;
    }

    public void setTaxiDispatcher(final TaxiDispatcher taxiDispatcher) {
        this.taxiDispatcher = taxiDispatcher;
    }

    public BookingResponse getBookingResponse() {
        return bookingResponse;
    }

    public void setBookingResponse(final BookingResponse bookingResponse) {
        this.bookingResponse = bookingResponse;
    }

    public DateTime getCreated() {
        return created;
    }

    public void setCreated(final DateTime created) {
        this.created = created;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(final Order order) {
        this.order = order;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        final BookingRequest that = (BookingRequest) obj;

        return Objects.equals(this.id, that.id) && Objects.equals(this.startPosition, that.startPosition) && Objects
                .equals(this.finishPosition, that.finishPosition) && Objects.equals(this.deliveryTime,
                that.deliveryTime) && Objects.equals(this.vehicleType, that.vehicleType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startPosition, finishPosition, deliveryTime, vehicleType);
    }

    public enum Status {
        ACCEPTED, REJECTED, REFUSED, EXPIRED, FAILED
    }
}
