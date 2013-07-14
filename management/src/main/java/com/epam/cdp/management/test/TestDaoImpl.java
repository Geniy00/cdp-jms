package com.epam.cdp.management.test;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

@Repository
public class TestDaoImpl implements TestDao {
	
	@PersistenceContext
	EntityManager em;
	
	public void create(TestEntity testEntity){
		em.persist(testEntity);
		em.flush();
	}
	
}
