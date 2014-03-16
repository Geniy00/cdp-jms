package com.epam.cdp.management.service;

import com.epam.cdp.core.entity.BookingRequestEnum;
import com.epam.cdp.core.entity.Report;

import java.util.List;

public interface ReportService {

    public void saveOrUpdateReport(Report report);

    public Report find(String id);

    public List<Report> getAllReports();

    public List<Report> findReportWithStatus(BookingRequestEnum.Status status);
}