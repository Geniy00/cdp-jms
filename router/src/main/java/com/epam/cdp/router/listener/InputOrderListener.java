package com.epam.cdp.router.listener;

import com.epam.cdp.core.entity.Order;
import com.epam.cdp.core.entity.Report;
import com.epam.cdp.router.dao.ReportDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.MessageListener;

@Component
public class InputOrderListener implements MessageListener {

    public static final Logger LOG = Logger.getLogger(InputOrderListener.class);

    @Autowired
    ReportDao reportDao;

    @Override
    public void onMessage(Message message) {
        if (message instanceof Order) {
            Order order = (Order) message;
            Report report = new Report(order);
            reportDao.create(report);
            LOG.info("Order with id: " + order.getId() + " was processed successfully");
        } else {
            LOG.error("JMS message must be of type Order");
            throw new IllegalArgumentException("JMS message must be of type Order");
        }

    }
}
