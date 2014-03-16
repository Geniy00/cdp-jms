package com.epam.cdp.management.dao;

import com.epam.cdp.core.entity.BookingRequestEnum;
import com.epam.cdp.core.entity.Report;

import java.util.List;

public interface ReportDao {

    public abstract void create(Report report);

    public abstract void update(Report report);

    public abstract void remove(Report report);

    public abstract Report find(String id);

    public abstract List<Report> findAll();

    //TODO: fix this method!!!
    public List<Report> findFromInterval(int startIndex, int count);

    //TODO: fix this method!!!
    public List<Report> findReportWithStatus(BookingRequestEnum.Status status);

}