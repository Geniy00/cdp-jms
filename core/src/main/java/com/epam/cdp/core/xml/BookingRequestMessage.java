package com.epam.cdp.core.xml;

import com.epam.cdp.core.entity.BookingRequest;
import com.epam.cdp.core.entity.VehicleType;
import org.joda.time.DateTime;

/**
 * @author Geniy00
 */
public class BookingRequestMessage {

    private String id;
    private Integer startPosition;
    private Integer finishPosition;
    private DateTime deliveryTime;
    private VehicleType vehicleType;
    private Double payment;
    private DateTime expiryTime;
    private String orderId;

    public BookingRequestMessage() {
    }

    public BookingRequestMessage(BookingRequest bookingRequest) {
        id = bookingRequest.getId();
        startPosition = bookingRequest.getStartPosition();
        finishPosition = bookingRequest.getFinishPosition();
        deliveryTime = bookingRequest.getDeliveryTime();
        vehicleType = bookingRequest.getVehicleType();
        payment = bookingRequest.getPayment();
        expiryTime = bookingRequest.getExpiryTime();
        orderId = bookingRequest.getOrder().getId();
    }

    public String getId() {
        return id;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setExpiryTime(DateTime expiryTime) {
        this.expiryTime = expiryTime;
    }

    public void setPayment(Double payment) {
        this.payment = payment;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public void setDeliveryTime(DateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public void setFinishPosition(Integer finishPosition) {
        this.finishPosition = finishPosition;
    }

    public void setStartPosition(Integer startPosition) {
        this.startPosition = startPosition;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getStartPosition() {
        return startPosition;
    }

    public Integer getFinishPosition() {
        return finishPosition;
    }

    public DateTime getDeliveryTime() {
        return deliveryTime;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public Double getPayment() {
        return payment;
    }

    public DateTime getExpiryTime() {
        return expiryTime;
    }

    public String getOrderId() {
        return orderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookingRequestMessage that = (BookingRequestMessage) o;

        if (deliveryTime != null ? !deliveryTime.equals(that.deliveryTime) : that.deliveryTime != null) return false;
        if (expiryTime != null ? !expiryTime.equals(that.expiryTime) : that.expiryTime != null) return false;
        if (finishPosition != null ? !finishPosition.equals(that.finishPosition) : that.finishPosition != null)
            return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (orderId != null ? !orderId.equals(that.orderId) : that.orderId != null) return false;
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
        result = 31 * result + (orderId != null ? orderId.hashCode() : 0);
        return result;
    }
}
