package com.epam.cdp.management.service;

import com.epam.cdp.core.entity.FailQueueMessage;

import java.util.List;

/**
 * @author Geniy00
 */
public interface FailQueueService {

    FailQueueMessage update(FailQueueMessage failQueueMessage);

    void delete(Long id);

    FailQueueMessage find(Long id);

    List<FailQueueMessage> findAll();
}
