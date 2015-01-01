package com.epam.cdp.core.xml;

import com.epam.cdp.core.entity.BookingRequest;
import com.epam.cdp.core.entity.VehicleType;
import org.joda.time.DateTime;

import java.util.Objects;

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

    public String getOrderId() {
        return orderId;
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        final BookingRequestMessage that = (BookingRequestMessage) obj;

        return Objects.equals(this.id, that.id)
                && Objects.equals(this.orderId, that.orderId)
                && Objects.equals(this.startPosition, that.startPosition)
                && Objects.equals(this.finishPosition, that.finishPosition)
                && Objects.equals(this.deliveryTime, that.deliveryTime)
                && Objects.equals(this.vehicleType, that.vehicleType)
                && Objects.equals(this.payment, that.payment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderId, startPosition, finishPosition, deliveryTime, vehicleType, payment);
    }
}
