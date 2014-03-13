package com.epam.cdp.core.entity;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;

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
    @Column(name = "id")
    private String id;

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

    public ReservationRequest(String id, String customerName, String customerPhone, Integer startPosition, Integer finishPosition, DateTime deliveryTime, VehicleType vehicleType) {
        this.id = id;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.startPosition = startPosition;
        this.finishPosition = finishPosition;
        this.deliveryTime = deliveryTime;
        this.vehicleType = vehicleType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReservationRequest that = (ReservationRequest) o;

        if (!customerName.equals(that.customerName)) return false;
        if (!customerPhone.equals(that.customerPhone)) return false;
        if (!deliveryTime.equals(that.deliveryTime)) return false;
        if (!finishPosition.equals(that.finishPosition)) return false;
        if (!id.equals(that.id)) return false;
        if (!startPosition.equals(that.startPosition)) return false;
        if (vehicleType != that.vehicleType) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + customerName.hashCode();
        result = 31 * result + customerPhone.hashCode();
        result = 31 * result + startPosition.hashCode();
        result = 31 * result + finishPosition.hashCode();
        result = 31 * result + deliveryTime.hashCode();
        result = 31 * result + vehicleType.hashCode();
        return result;
    }
}
