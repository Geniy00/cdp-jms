package ua.com.taxi.entity;

import com.epam.cdp.core.entity.Order;

public class Report {

    private String id;
    private Order order;
    private ReportStatus status;
    private String reason;

    public Report(Order order, ReportStatus status, String reason) {
        this.id = order.getId();
        this.order = order;
        this.status = status;
        this.reason = reason;
    }

    public enum ReportStatus {
        ACCEPTED, REJECTED, REFUSED, UNKNOWN
    }

    public String getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
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
