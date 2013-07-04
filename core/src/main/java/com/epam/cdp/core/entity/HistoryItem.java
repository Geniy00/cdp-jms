package com.epam.cdp.core.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
@Table(name="historyItem")
public class HistoryItem implements Serializable{
	private static final long serialVersionUID = -4490123034598037678L;
	
	private long id;
	private ReportStatus reportStatus;
	private String reason;
	private String taxiId;
	private DateTime dateTime;
	
	@ManyToOne
	@JoinColumn(name="reportId")
	private Report report;
	
	public enum ReportStatus{
		ACCEPTED, REJECTED, REFUSED, FAILURE
	}

	public HistoryItem() { }
	
	public HistoryItem(ReportStatus reportStatus, String reason,
			String taxiId) {
		super();
		this.reportStatus = reportStatus;
		this.reason = reason;
		this.taxiId = taxiId;
		this.dateTime = new DateTime();
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Enumerated(EnumType.STRING)
	@Column(name="reportStatus")
	public ReportStatus getReportStatus() {
		return reportStatus;
	}

	public void setReportStatus(ReportStatus reportStatus) {
		this.reportStatus = reportStatus;
	}

	@Column(name="reason")
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Column(name="taxiId")
	public String getTaxiId() {
		return taxiId;
	}

	public void setTaxiId(String taxiId) {
		this.taxiId = taxiId;
	}

	@Column(name="dateTime")
	@Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
	public DateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(DateTime dateTime) {
		this.dateTime = dateTime;
	}

	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		HistoryItem other = (HistoryItem) obj;
		if (id != other.id)
			return false;
		return true;
	}
}