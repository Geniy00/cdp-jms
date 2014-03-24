package com.epam.cdp.router.gateway;

import com.epam.cdp.core.entity.FailQueueMessage;
import com.epam.cdp.router.service.OrderService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Geniy00
 */
@Component
public class FailQueueListener implements MessageListener{

    public static final Logger LOG = Logger.getLogger(FailQueueListener.class);

    @Autowired
    OrderService orderService;

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        String xmlMessage = null;
        try {
            xmlMessage = textMessage.getText();
        } catch (JMSException e) {
            LOG.error("Can't get xml from received message");
            e.printStackTrace();
        }

        FailQueueMessage failQueueMessage = new FailQueueMessage(xmlMessage);
        orderService.persistFailQueueMessage(failQueueMessage);

        LOG.info("New FailQueueMessage:\n" + xmlMessage);
    }
}
