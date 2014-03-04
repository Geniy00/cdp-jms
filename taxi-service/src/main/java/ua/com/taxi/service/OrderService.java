package ua.com.taxi.service;

import com.epam.cdp.core.entity.Order;

public interface OrderService {

    public Order peekOrder();

    public Order acceptOrder(String id);

    public Order rejectOrder(String id);

    public Order refuseOrder(String id, String reason);

    public int getQueueSize();

}