package com.epam.cdp.management.dao;

import java.util.List;

import com.epam.cdp.core.entity.BookingResponse;
import com.epam.cdp.core.entity.Report;

public interface ReportDao {

	public abstract void create(Report report);

	public abstract void update(Report report);

	public abstract void remove(Report report);

	public abstract Report find(String id);

	public abstract List<Report> findAll();

	//TODO: fix this method!!!
	public List<Report> findFromInterval(int startIndex, int count);

	//TODO: fix this method!!!
	public List<Report> findReportWithStatus(BookingResponse.BookingResponseStatus bookingResponseStatus);

}