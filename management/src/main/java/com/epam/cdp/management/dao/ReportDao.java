package com.epam.cdp.management.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

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
	
	public List<Report> findAll(){
		TypedQuery<Report> query = 
				em.createQuery("SELECT r FROM Report r", Report.class);
		return query.getResultList();
	}
}
