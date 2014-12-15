package com.epam.cdp.management.service;

import com.epam.cdp.core.entity.FailQueueMessage;
import com.epam.cdp.management.dao.FailQueueDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Geniy00
 */
@Service
@Transactional
public class FailQueueServiceImpl implements FailQueueService {

    @Autowired
    FailQueueDao failQueueDao;

    @Override
    public FailQueueMessage update(final FailQueueMessage failQueueMessage) {
        return failQueueDao.saveOrUpdate(failQueueMessage);
    }

    @Override
    public void delete(final Long id) {
        final FailQueueMessage failQueueMessage = failQueueDao.find(id);
        failQueueDao.delete(failQueueMessage);
    }

    @Override
    public FailQueueMessage find(final Long id) {
        return failQueueDao.find(id);
    }

    @Override
    public List<FailQueueMessage> findAll() {
        return failQueueDao.findAll();
    }
}
