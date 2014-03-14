package ua.com.taxi.entity;

import com.epam.cdp.core.entity.VehicleType;
import com.epam.cdp.core.xml.BookingRequestMessage;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * @author Geniy00
 */
@Entity
@Table(name = "booking_request")
public class BookingRequestDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "bookingRequestId", nullable = false)
    private Long bookingRequestId;

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

    @Column(name = "orderId", nullable = false)
    private String orderId;

    public BookingRequestDetails() {
    }

    public BookingRequestDetails(BookingRequestMessage bookingRequestMessage) {
        bookingRequestId = bookingRequestMessage.getId();
        startPosition = bookingRequestMessage.getStartPosition();
        finishPosition = bookingRequestMessage.getFinishPosition();
        deliveryTime = bookingRequestMessage.getDeliveryTime();
        vehicleType = bookingRequestMessage.getVehicleType();
        payment = bookingRequestMessage.getPayment();
        expiryTime = bookingRequestMessage.getExpiryTime();
        orderId = bookingRequestMessage.getOrderId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookingRequestId() {
        return bookingRequestId;
    }

    public void setBookingRequestId(Long bookingRequestId) {
        this.bookingRequestId = bookingRequestId;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookingRequestDetails that = (BookingRequestDetails) o;

        if (!bookingRequestId.equals(that.bookingRequestId)) return false;
        if (!deliveryTime.equals(that.deliveryTime)) return false;
        if (!expiryTime.equals(that.expiryTime)) return false;
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
        result = 31 * result + bookingRequestId.hashCode();
        result = 31 * result + startPosition.hashCode();
        result = 31 * result + finishPosition.hashCode();
        result = 31 * result + deliveryTime.hashCode();
        result = 31 * result + vehicleType.hashCode();
        result = 31 * result + payment.hashCode();
        result = 31 * result + expiryTime.hashCode();
        result = 31 * result + orderId.hashCode();
        return result;
    }
}
