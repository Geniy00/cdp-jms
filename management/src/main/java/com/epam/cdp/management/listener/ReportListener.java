package com.epam.cdp.management.listener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.cdp.core.entity.Report;
import com.epam.cdp.management.dao.ReportDao;

@Component
public class ReportListener {
	
	public static final Logger LOG = Logger.getLogger(ReportListener.class);
	
	@Autowired
	ReportDao reportDao;
	
	public void onMessage(Report report){
		LOG.info("Report with id " + report.getId() + " received");
		
		if(isCorrect(report) == false){
			LOG.error("The report is broken");
			throw new RuntimeException();
		}
		
		Report dbReport = reportDao.find(report.getId());
		if(dbReport != null){
			dbReport.addAllHistoryItems(report.getHistory());
			reportDao.update(dbReport);
		} else {
			reportDao.create(report);
		}
	}
	
	protected boolean isCorrect(Report report){
		return true;
	}
	
}
