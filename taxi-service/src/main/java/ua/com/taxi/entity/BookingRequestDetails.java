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
    private Integer id;

    @Column(name = "bookingRequestId")
    private String bookingRequestId;

    @Column(name = "startPosition")
    private Integer startPosition;

    @Column(name = "finishPosition")
    private Integer finishPosition;

    @Column(name = "deliveryTime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime deliveryTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicleType")
    private VehicleType vehicleType;

    @Column(name = "payment")
    private Double payment;

    @Column(name = "expiryTime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime expiryTime;

    @Column(name = "orderId")
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBookingRequestId() {
        return bookingRequestId;
    }

    public void setBookingRequestId(String bookingRequestId) {
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

        if (bookingRequestId != null ? !bookingRequestId.equals(that.bookingRequestId) : that.bookingRequestId != null)
            return false;
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
        result = 31 * result + (bookingRequestId != null ? bookingRequestId.hashCode() : 0);
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
