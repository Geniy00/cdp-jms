package ua.com.taxi.service;

import com.epam.cdp.core.entity.Order;
import com.epam.cdp.library.logic.OrderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.taxi.bean.HistoryList;
import ua.com.taxi.entity.Report;
import ua.com.taxi.entity.Report.ReportStatus;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderManager orderManager;

    @Autowired
    HistoryList historyList;

    @Override
    public Order peekOrder() {
        return orderManager.peekOrder();
    }

    @Override
    public Order acceptOrder(String id) {
        Order order = orderManager.acceptOrder(id);

        Report report = new Report(order, ReportStatus.ACCEPTED, "order accepted");
        historyList.add(report);

        return order;
    }

    @Override
    public Order rejectOrder(String id) {
        Order order = orderManager.rejectOrder(id);

        Report report = new Report(order, ReportStatus.REJECTED, "order rejected");
        historyList.add(report);

        return order;
    }

    @Override
    public Order refuseOrder(String id, String reason) {
        Order order = historyList.getOrderById(id);
        if (order == null) return null;

        orderManager.refuseOrder(order, reason);

        Report report = new Report(order, ReportStatus.REFUSED, reason);
        historyList.add(report);

        return order;
    }

    @Override
    public int getQueueSize() {
        return orderManager.getQueueSize();
    }
}
