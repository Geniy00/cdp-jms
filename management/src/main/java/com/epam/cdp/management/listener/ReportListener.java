package com.epam.cdp.management.listener;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.epam.cdp.core.entity.Report;

@Component
public class ReportListener {
	
	public static final Logger LOG = Logger.getLogger(ReportListener.class);
	
	public void onMessage(Report report){
		LOG.info("Report with id " + report.getId() + " received");
		
		if(isCorrect(report) == false){
			LOG.error("The report is broken");
			throw new RuntimeException();
		}
		
		//send to DB
		
	}
	
	protected boolean isCorrect(Report report){
		return true;
	}
	
}
