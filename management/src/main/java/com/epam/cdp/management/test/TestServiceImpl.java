package com.epam.cdp.management.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {
	
	@Autowired
	TestDao testDao;
	
	@Override
	public void createTestEntity(TestEntity testEntity){
		testDao.create(testEntity);
	}
	
}
