package com.epam.cdp.core.xml;

import com.epam.cdp.core.entity.BookingRequest;
import com.epam.cdp.core.entity.VehicleType;
import org.joda.time.DateTime;

/**
 * @author Geniy00
 */
public class BookingRequestMessage {

    private Long id;
    private String orderId;
    private Integer startPosition;
    private Integer finishPosition;
    private DateTime deliveryTime;
    private VehicleType vehicleType;
    private Double payment;
    private DateTime expiryTime;

    public BookingRequestMessage() {
    }

    public BookingRequestMessage(BookingRequest bookingRequest) {
        id = bookingRequest.getId();
        orderId = bookingRequest.getOrder().getId();
        startPosition = bookingRequest.getStartPosition();
        finishPosition = bookingRequest.getFinishPosition();
        deliveryTime = bookingRequest.getDeliveryTime();
        vehicleType = bookingRequest.getVehicleType();
        payment = bookingRequest.getPayment();
        expiryTime = bookingRequest.getExpiryTime();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookingRequestMessage that = (BookingRequestMessage) o;

        if (!deliveryTime.equals(that.deliveryTime)) return false;
        if (!finishPosition.equals(that.finishPosition)) return false;
        if (!id.equals(that.id)) return false;
        if (!orderId.equals(that.orderId)) return false;
        if (!payment.equals(that.payment)) return false;
        if (!startPosition.equals(that.startPosition)) return false;
        if (vehicleType != that.vehicleType) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + orderId.hashCode();
        result = 31 * result + startPosition.hashCode();
        result = 31 * result + finishPosition.hashCode();
        result = 31 * result + deliveryTime.hashCode();
        result = 31 * result + vehicleType.hashCode();
        result = 31 * result + payment.hashCode();
        return result;
    }
}
