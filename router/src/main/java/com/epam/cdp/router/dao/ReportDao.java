package com.epam.cdp.router.dao;


import com.epam.cdp.core.entity.Report;

public interface ReportDao {

    void create(Report report);

    void update(Report report);

    void delete(Report report);

    Report find(String id);

}
