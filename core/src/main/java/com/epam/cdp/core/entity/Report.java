package com.epam.cdp.core.entity;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;

public class Report implements Serializable {
	
	private static final long serialVersionUID = 188607911440560306L;
	
	private String id;
	private Order order;
	private List<HistoryItem> history;
	
	public Report(Order order){
		this.id = order.getId();
		this.order = order;
		history = new LinkedList<Report.HistoryItem>();
	}
	
	public enum ReportStatus{
		ACCEPTED, REJECTED, REFUSED, FAILURE
	}
	
	public static class HistoryItem implements Serializable{
		private static final long serialVersionUID = -4490123034598037678L;
		
		private ReportStatus reportStatus;
		private String reason;
		private String taxiId;
		private DateTime dateTime;
		
		public HistoryItem(ReportStatus reportStatus, String reason,
				String taxiId) {
			super();
			this.reportStatus = reportStatus;
			this.reason = reason;
			this.taxiId = taxiId;
			this.dateTime = new DateTime();
		}
		public ReportStatus getReportStatus() {
			return reportStatus;
		}
		public String getTaxiId() {
			return taxiId;
		}
		public DateTime getDateTime() {
			return dateTime;
		}
		public String getReason() {
			return reason;
		}
	}

	
	public void addHistoryItem(HistoryItem item){
		history.add(item);
	}
	
	public String getId() {
		return id;
	}

	public List<HistoryItem> getHistory() {
		return history;
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
