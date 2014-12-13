package com.epam.cdp.core.entity;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Request that is created by sender and was sent to Router through InputQueue
 *
 * @author Geniy00
 */
@Entity
@Table(name = "reservation_request")
public class ReservationRequest implements Serializable {

    private static final long serialVersionUID = 1820235672221505291L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String customerName;

    @Column(name = "phone", nullable = false)
    private String customerPhone;

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

    public ReservationRequest() {
    }

    public ReservationRequest(Long id, String customerName, String customerPhone, Integer startPosition, Integer finishPosition, DateTime deliveryTime, VehicleType vehicleType) {
        this.id = id;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.startPosition = startPosition;
        this.finishPosition = finishPosition;
        this.deliveryTime = deliveryTime;
        this.vehicleType = vehicleType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        ReservationRequest that = (ReservationRequest) obj;

        return Objects.equals(this.id, that.id)
                && Objects.equals(this.customerName, that.customerName)
                && Objects.equals(this.customerPhone, that.customerPhone)
                && Objects.equals(this.startPosition, that.startPosition)
                && Objects.equals(this.finishPosition, that.finishPosition)
                && Objects.equals(this.deliveryTime, that.deliveryTime)
                && Objects.equals(this.vehicleType, that.vehicleType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerName, customerPhone, startPosition, finishPosition, deliveryTime, vehicleType);
    }
}
