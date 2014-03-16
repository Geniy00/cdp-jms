package com.epam.cdp.management.service;

import com.epam.cdp.core.entity.BookingRequestEnum;
import com.epam.cdp.core.entity.Report;
import com.epam.cdp.management.dao.ReportDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    ReportDao reportDao;

    @Override
    public void saveOrUpdateReport(Report report) {
        Report reportFromDb = reportDao.find(report.getId());
        if (reportFromDb != null) {
            reportFromDb.addAllHistoryItems(report.getHistory());
            reportDao.update(reportFromDb);
        } else {
            reportDao.create(report);
        }
    }

    @Override
    public Report find(String id) {
        return reportDao.find(id);
    }

    @Override
    public List<Report> getAllReports() {
        return reportDao.findAll();
    }

    @Override
    public List<Report> findReportWithStatus(BookingRequestEnum.Status status) {
        return reportDao.findReportWithStatus(status);
    }
}
