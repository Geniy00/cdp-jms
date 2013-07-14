package com.epam.cdp.management.service;

import java.util.List;

import com.epam.cdp.core.entity.Report;
import com.epam.cdp.core.entity.HistoryItem.ReportStatus;

public interface ReportService {

	public void saveOrUpdateReport(Report report);

	public Report find(String id);
	
	public List<Report> getAllReports();

	public List<Report> findReportWithStatus(ReportStatus reportStatus);
}