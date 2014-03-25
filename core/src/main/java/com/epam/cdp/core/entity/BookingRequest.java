package com.epam.cdp.core.entity;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;

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

    //TODO: there is posibility where several responses have to linked to a BookingRequest (i.e. ACCEPT, REFUSE)
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
        created = new DateTime();
    }

    public void applyBookingResponse(BookingResponse bookingResponse){
        this.bookingResponse = bookingResponse;
        BookingRequestEnum.Status status = bookingResponse.getStatus();
        switch (status){
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookingRequest that = (BookingRequest) o;

        if (!deliveryTime.equals(that.deliveryTime)) return false;
        if (!finishPosition.equals(that.finishPosition)) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (!startPosition.equals(that.startPosition)) return false;
        if (vehicleType != that.vehicleType) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + startPosition.hashCode();
        result = 31 * result + finishPosition.hashCode();
        result = 31 * result + deliveryTime.hashCode();
        result = 31 * result + vehicleType.hashCode();
        return result;
    }
}
