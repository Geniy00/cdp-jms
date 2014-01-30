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
    @Column(name="id")
    private String id;

    @Column(name="startPosition")
    private Integer startPosition;

    @Column(name="finishPosition")
    private Integer finishPosition;

    @Column(name="deliveryTime")
    @Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
    private DateTime deliveryTime;

    @Enumerated(EnumType.STRING)
    @Column(name="vehicleType")
    private VehicleType vehicleType;

    @Column(name="payment")
    private Double payment;

    @Column(name="expiryTime")
    @Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
    private DateTime expiryTime;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Order order;

    public BookingRequest() {
    }

    public BookingRequest(Order order, ReservationRequest reservationRequest, Double payment, DateTime expiryTime) {
        this.id = order.getId();
        this.startPosition = reservationRequest.getStartPosition();
        this.finishPosition = reservationRequest.getFinishPosition();
        this.deliveryTime = reservationRequest.getDeliveryTime();
        this.vehicleType = reservationRequest.getVehicleType();

        this.payment = payment;
        this.expiryTime = expiryTime;
        this.order = order;
        this.customer = order.getCustomer();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

        if (customer != null ? !customer.equals(that.customer) : that.customer != null) return false;
        if (deliveryTime != null ? !deliveryTime.equals(that.deliveryTime) : that.deliveryTime != null) return false;
        if (expiryTime != null ? !expiryTime.equals(that.expiryTime) : that.expiryTime != null) return false;
        if (finishPosition != null ? !finishPosition.equals(that.finishPosition) : that.finishPosition != null)
            return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (order != null ? !order.equals(that.order) : that.order != null) return false;
        if (payment != null ? !payment.equals(that.payment) : that.payment != null) return false;
        if (startPosition != null ? !startPosition.equals(that.startPosition) : that.startPosition != null)
            return false;
        if (vehicleType != that.vehicleType) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (startPosition != null ? startPosition.hashCode() : 0);
        result = 31 * result + (finishPosition != null ? finishPosition.hashCode() : 0);
        result = 31 * result + (deliveryTime != null ? deliveryTime.hashCode() : 0);
        result = 31 * result + (vehicleType != null ? vehicleType.hashCode() : 0);
        result = 31 * result + (payment != null ? payment.hashCode() : 0);
        result = 31 * result + (expiryTime != null ? expiryTime.hashCode() : 0);
        result = 31 * result + (customer != null ? customer.hashCode() : 0);
        result = 31 * result + (order != null ? order.hashCode() : 0);
        return result;
    }
}
