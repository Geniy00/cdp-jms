package com.epam.cdp.management.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.epam.cdp.core.entity.Report;

@Repository
public class ReportDao {
	
	@PersistenceContext
	EntityManager em;
	
	public void create(Report report){
		em.persist(report);
	}
	
	public void update(Report report){
		em.merge(report);
	}
	
	public void remove(Report report){
		em.remove(em.merge(report));
	}
	
	public Report find(String id){
		return em.find(Report.class, id);
	}
}
