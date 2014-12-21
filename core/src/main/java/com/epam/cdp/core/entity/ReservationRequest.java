package com.epam.cdp.core.entity;

import org.hibernate.annotations.Immutable;
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
@Immutable
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

    public ReservationRequest(final Long id, final String customerName, final String customerPhone,
            final Integer startPosition, final Integer finishPosition, final DateTime deliveryTime,
            final VehicleType vehicleType) {
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

    public void setId(final Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(final String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(final String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public Integer getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(final Integer startPosition) {
        this.startPosition = startPosition;
    }

    public Integer getFinishPosition() {
        return finishPosition;
    }

    public void setFinishPosition(final Integer finishPosition) {
        this.finishPosition = finishPosition;
    }

    public DateTime getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(final DateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(final VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        final ReservationRequest that = (ReservationRequest) obj;

        return Objects.equals(this.id, that.id) && Objects.equals(this.customerName, that.customerName) && Objects
                .equals(this.customerPhone, that.customerPhone) && Objects.equals(this.startPosition,
                that.startPosition) && Objects.equals(this.finishPosition, that.finishPosition) && Objects.equals(
                this.deliveryTime, that.deliveryTime) && Objects.equals(this.vehicleType, that.vehicleType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerName, customerPhone, startPosition, finishPosition, deliveryTime, vehicleType);
    }
}
