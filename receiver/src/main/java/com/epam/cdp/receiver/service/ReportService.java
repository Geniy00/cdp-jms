package com.epam.cdp.receiver.service;

import com.epam.cdp.core.entity.Report;


public interface ReportService {
	
	void sendReport(Report report);
	
}