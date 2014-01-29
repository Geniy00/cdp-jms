package com.epam.cdp.router.dao;

import com.epam.cdp.core.entity.Report;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ReportDaoImpl implements ReportDao {

    @PersistenceContext
    EntityManager em;

    @Override
    public void create(Report report) {
        em.persist(report);
    }

    @Override
    public void update(Report report) {
        em.merge(report);
    }

    @Override
    public void delete(Report report) {
        em.remove(em.merge(report));
    }

    @Override
    public Report find(String id) {
        return em.find(Report.class, id);
    }
}
