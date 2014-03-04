package com.epam.cdp.management.listener;

import com.epam.cdp.core.entity.Report;
import com.epam.cdp.management.service.ReportService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReportListener {

    public static final Logger LOG = Logger.getLogger(ReportListener.class);

    @Autowired
    ReportService reportService;

    public void onMessage(Report report) {
        LOG.info("Report with id " + report.getId() + " received");

        if (isCorrect(report) == false) {
            LOG.error("The report is broken");
            throw new RuntimeException();
        }

        reportService.saveOrUpdateReport(report);
    }

    protected boolean isCorrect(Report report) {
        return true;
    }

}
