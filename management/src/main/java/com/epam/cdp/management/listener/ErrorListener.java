package com.epam.cdp.management.listener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.cdp.core.entity.Report;
import com.epam.cdp.management.service.ReportService;

@Component
public class ErrorListener {

	public static final Logger LOG = Logger.getLogger(ErrorListener.class);
	
	@Autowired
	ReportService reportService;
	
	public void onMessage(Report report){
		LOG.warn("Error report with id " + report.getId() + " received");		
		
		reportService.saveOrUpdateReport(report);
		LOG.warn("Error report with id " + report.getId() + " processed");
	}
}
