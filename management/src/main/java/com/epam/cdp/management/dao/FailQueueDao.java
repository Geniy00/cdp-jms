package com.epam.cdp.management.dao;

import com.epam.cdp.core.entity.FailQueueMessage;

import java.util.List;

/**
 * @author Geniy00
 */
public interface FailQueueDao {

    FailQueueMessage saveOrUpdate(FailQueueMessage failQueueMessage);

    void delete(FailQueueMessage failQueueMessage);

    FailQueueMessage find(Long id);

    List<FailQueueMessage> findAll();

}
