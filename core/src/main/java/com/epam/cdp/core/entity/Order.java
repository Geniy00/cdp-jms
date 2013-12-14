package com.epam.cdp.core.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
@Table(name="ord")
public class Order implements Serializable {

	private static final long serialVersionUID = 1820235678421505291L;
	
	@Id
	@Column(name="id")
	private String id;
	
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	private Customer customer;
	
	@Column(name="startPosition")
	private Integer startPosition;
	
	@Column(name="finishPosition")
	private Integer finishPosition;

	@Column(name="dateTime")
	@Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
	DateTime dateTime;

	@Enumerated(EnumType.STRING)
	@Column(name="orderType")
	OrderType orderType;
	
	@Column(name="price")
	Double price;

	public enum OrderType {
		PASSENGER, CARGO
	}
	
	public Order() {
	}

	public Order(String id, Customer customer, Integer startPosition,
			Integer finishPosition, DateTime dateTime, OrderType orderType) {
		super();
		this.id = id;
		this.customer = customer;
		this.startPosition = startPosition;
		this.finishPosition = finishPosition;
		this.dateTime = dateTime;
		this.orderType = orderType;
		calculatePrice();
	}

	public void calculatePrice(){
		this.price = Math.abs((double)startPosition - finishPosition);
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Customer getCustomer() { 
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
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

	public DateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(DateTime dateTime) {
		this.dateTime = dateTime;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	@Override
	public String toString() {
		return "Order [id=" + id + ", customer=" + customer + ", dateTime="
				+ dateTime + ", price=" + price + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
