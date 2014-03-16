package com.epam.cdp.management.dao;

import com.epam.cdp.core.entity.BookingRequestEnum.Status;
import com.epam.cdp.core.entity.BookingResponse;
import com.epam.cdp.core.entity.Report;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ReportDaoImpl implements ReportDao {

    @PersistenceContext
    EntityManager em;

    @Override
    public void create(Report report) {
        em.merge(report);
        for (BookingResponse item : report.getHistory()) {
            if (item.getReport() == null) {
                item.setReport(report);
            }
        }
    }

    @Override
    public void update(Report report) {
        em.merge(report);
    }

    @Override
    public void remove(Report report) {
        em.remove(em.merge(report));
    }

    @Override
    public Report find(String id) {
        return em.find(Report.class, id);
    }

    @Override
    public List<Report> findAll() {

        TypedQuery<Report> query =
                em.createQuery("SELECT r FROM Report r", Report.class);
        return query.getResultList();
    }

    public List<Report> findFromInterval(int startIndex, int count) {
        TypedQuery<Report> query =
                em.createQuery("SELECT r FROM Report r LIMIT :startIndex, :coun", Report.class)
                        .setParameter("startIndex", startIndex)
                        .setParameter("coun", count);
        return query.getResultList();
    }

    @Override
    public List<Report> findReportWithStatus(Status status) {
        TypedQuery<Report> query =
                em.createQuery("SELECT r FROM Report r JOIN r.history hi  WHERE r.id = hi.report.id AND hi.reportStatus = 'failure'", Report.class);
        return query.getResultList();
    }
}
