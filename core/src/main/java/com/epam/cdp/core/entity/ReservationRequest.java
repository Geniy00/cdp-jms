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

    @Column(name = "name")
    private String customerName;

    @Column(name = "phone")
    private String customerPhone;

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

        if (customerName != null ? !customerName.equals(that.customerName) : that.customerName != null) return false;
        if (customerPhone != null ? !customerPhone.equals(that.customerPhone) : that.customerPhone != null)
            return false;
        if (deliveryTime != null ? !deliveryTime.equals(that.deliveryTime) : that.deliveryTime != null) return false;
        if (finishPosition != null ? !finishPosition.equals(that.finishPosition) : that.finishPosition != null)
            return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (startPosition != null ? !startPosition.equals(that.startPosition) : that.startPosition != null)
            return false;
        if (vehicleType != that.vehicleType) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (customerName != null ? customerName.hashCode() : 0);
        result = 31 * result + (customerPhone != null ? customerPhone.hashCode() : 0);
        result = 31 * result + (startPosition != null ? startPosition.hashCode() : 0);
        result = 31 * result + (finishPosition != null ? finishPosition.hashCode() : 0);
        result = 31 * result + (deliveryTime != null ? deliveryTime.hashCode() : 0);
        result = 31 * result + (vehicleType != null ? vehicleType.hashCode() : 0);
        return result;
    }
}
