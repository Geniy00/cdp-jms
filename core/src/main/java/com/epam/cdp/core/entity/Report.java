package com.epam.cdp.core.entity;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.epam.cdp.core.entity.HistoryItem.ReportStatus;

@Entity
@Table(name="report")
public class Report implements Serializable {
	private static final long serialVersionUID = 188607911440560306L;

	@Id
	@Column(name="id")
	private String id;

	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true)
	private Order order;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy="report", orphanRemoval=true)
	private List<HistoryItem> history;
	
	public Report(Order order){
		this.id = order.getId();
		this.order = order;
		history = new LinkedList<HistoryItem>();
	}
	
	public void addHistoryItem(HistoryItem item){
		history.add(item);
	}
	
	public void addAllHistoryItems(List<HistoryItem> historyItems){
		history.addAll(historyItems);
	}
	
	public ReportStatus getReportStatus(){
		HistoryItem historyItem = history.get(history.size() - 1);
		return historyItem.getReportStatus();
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public List<HistoryItem> getHistory() {
		return history;
	}
	
	public void setHistory(List<HistoryItem> history) {
		this.history = history;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
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
		Report other = (Report) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
