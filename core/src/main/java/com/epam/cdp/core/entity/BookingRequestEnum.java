package com.epam.cdp.core.entity;

/**
 * @author Geniy00
 */
public class BookingRequestEnum {

    public enum Action {
        ACCEPT, REJECT, REFUSE, FAIL
    }

    public enum Status {
        ACCEPTED, REJECTED, REFUSED, EXPIRED, FAILED
    }

}
