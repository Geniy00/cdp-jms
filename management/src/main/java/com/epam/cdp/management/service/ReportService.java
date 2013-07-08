package com.epam.cdp.management.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.cdp.core.entity.Report;
import com.epam.cdp.management.dao.ReportDao;

@Service
public class ReportService {

	@Autowired
	ReportDao reportDao;
	
	public void saveOrCreateReport(Report report){
		Report reportFromDb = reportDao.find(report.getId());
		if(reportFromDb != null){
			reportFromDb.addAllHistoryItems(report.getHistory());
			reportDao.update(reportFromDb);
		} else {
			reportDao.create(report);
		}
	}
	
	public List<Report> getAllReports(){
		return reportDao.findAll();
	}
	
}
