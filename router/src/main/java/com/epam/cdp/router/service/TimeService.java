package com.epam.cdp.router.service;

import org.joda.time.DateTime;

/**
 * @author Geniy00
 */
public class TimeService {

    public static DateTime getCurrentTimestamp() {
        return new DateTime();
    }
}
