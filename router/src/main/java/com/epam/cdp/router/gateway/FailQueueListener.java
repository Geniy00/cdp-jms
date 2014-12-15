package com.epam.cdp.router.gateway;

import com.epam.cdp.core.entity.FailQueueMessage;
import com.epam.cdp.router.service.OrderService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @author Geniy00
 */
@Component
public class FailQueueListener implements MessageListener {

    public static final Logger LOG = Logger.getLogger(FailQueueListener.class);

    @Autowired
    OrderService orderService;

    @Override
    public void onMessage(Message message) {
        final TextMessage textMessage = (TextMessage) message;
        final String xmlMessage;
        try {
            xmlMessage = textMessage.getText();
        } catch (final JMSException ex) {
            LOG.error("Can't get xml from received message", ex);
            return;
        }

        final FailQueueMessage failQueueMessage = new FailQueueMessage(xmlMessage);
        orderService.persistFailQueueMessage(failQueueMessage);

        LOG.warn("New FailQueueMessage:\n" + xmlMessage);
    }
}
