package com.epam.cdp.management.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {

    @Autowired
    TestService service;

    @RequestMapping("/doaction")
    public String doAction() {
        TestEntity entity = new TestEntity("test");
        service.createTestEntity(entity);
        return "index";
    }

}
