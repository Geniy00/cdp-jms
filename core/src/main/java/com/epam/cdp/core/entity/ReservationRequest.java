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

    @Column(name = "requestId", nullable = false)
    private Long requestId;

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

    @Column(name = "price", nullable = false)
    private Double price;

    private Boolean indicative;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "createdTimestamp", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdTimestamp;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, optional = false)
    @JoinColumn(name = "sourceSystem")
    private SourceSystem sourceSystem;

    public enum Status {
        DRAFT,
        PRICED,
        RECEIVED,
        ASSIGNING,
        ASSIGNED,
        COMPLETED,
        CLOSED,
        FAILURE
    }

    public ReservationRequest() {
    }

    public ReservationRequest(final Long requestId, final String customerName, final String customerPhone,
            final Integer startPosition, final Integer finishPosition, final DateTime deliveryTime,
            final VehicleType vehicleType, final DateTime createdTimestamp, final SourceSystem sourceSystem) {
        this.requestId = requestId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.startPosition = startPosition;
        this.finishPosition = finishPosition;
        this.deliveryTime = deliveryTime;
        this.vehicleType = vehicleType;
        this.createdTimestamp = createdTimestamp;
        this.indicative = true;
        this.status = Status.DRAFT;
        this.sourceSystem = sourceSystem;
    }

    public ReservationRequest(final ReservationRequest reservationRequest) {
        this(reservationRequest, reservationRequest.getCreatedTimestamp());
    }

    public ReservationRequest(final ReservationRequest reservationRequest, final DateTime createdTimestamp) {
        this.id = reservationRequest.getId();
        this.requestId = reservationRequest.getRequestId();
        this.customerName = reservationRequest.getCustomerName();
        this.customerPhone = reservationRequest.getCustomerPhone();
        this.startPosition = reservationRequest.getStartPosition();
        this.finishPosition = reservationRequest.getFinishPosition();
        this.deliveryTime = reservationRequest.getDeliveryTime();
        this.vehicleType = reservationRequest.getVehicleType();
        this.createdTimestamp = createdTimestamp;
        this.indicative = reservationRequest.isIndicative();
        this.status = reservationRequest.getStatus();
        this.sourceSystem = reservationRequest.getSourceSystem();
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(final Long requestId) {
        this.requestId = requestId;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(final Double price) {
        this.price = price;
    }

    public Boolean isIndicative() {
        return indicative;
    }

    public void setIndicative(final Boolean isIndicative) {
        this.indicative = isIndicative;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    public DateTime getCreatedTimestamp() {
        return createdTimestamp;
    }

    //    public void setCreatedTimestamp(final DateTime createdTimestamp) {
    //        this.createdTimestamp = createdTimestamp;
    //    }

    public SourceSystem getSourceSystem() {
        return sourceSystem;
    }

    public void setSourceSystem(final SourceSystem sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        final ReservationRequest that = (ReservationRequest) obj;

        return Objects.equals(this.customerName, that.customerName)
                && Objects.equals(this.customerPhone, that.customerPhone)
                && Objects.equals(this.startPosition, that.startPosition)
                && Objects.equals(this.finishPosition, that.finishPosition)
                && Objects.equals(this.deliveryTime, that.deliveryTime)
                && Objects.equals(this.vehicleType, that.vehicleType)
                && Objects.equals(this.status, that.status)
                && Objects.equals(this.sourceSystem, that.sourceSystem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerName, customerPhone, startPosition, finishPosition, deliveryTime, vehicleType,
                status, sourceSystem);
    }

    @Override
    public String toString() {
        return "ReservationRequest{" +
                "customerName='" + customerName + '\'' +
                ", customerPhone='" + customerPhone + '\'' +
                ", indicative=" + indicative +
                ", status=" + status +
                ", startPosition=" + startPosition +
                ", finishPosition=" + finishPosition +
                ", sourceSystem=" + sourceSystem +
                '}';
    }
}
