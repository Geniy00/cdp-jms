package com.epam.cdp.core.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Geniy00
 */
@Entity
@Table(name = "report")
public class Report implements Serializable {
    private static final long serialVersionUID = 188607911440560306L;

    @Id
    @Column(name = "id")
    private String id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Order order;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "report", orphanRemoval = true)
    private List<BookingResponse> history;

    public Report() {
    }

    public Report(Order order) {
        this.id = order.getId();
        this.order = order;
        this.history = new LinkedList<BookingResponse>();
    }

    public void addHistoryItem(BookingResponse item) {

        history.add(item);
    }

    public void addAllHistoryItems(List<BookingResponse> bookingResponses) {
        for (BookingResponse item : bookingResponses) {
            addHistoryItem(item);
        }
    }

    public BookingResponse.BookingResponseStatus getReportStatus() {

        return null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<BookingResponse> getHistory() {
        return history;
    }

    public void setHistory(List<BookingResponse> history) {
        this.history = history;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "Report [id=" + id + ", order=" + order + ", history=" + history
                + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Report other = (Report) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
