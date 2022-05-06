package com.micropos.delivery.service;

import com.micropos.delivery.model.Order;
import com.micropos.delivery.repository.DeliveryOrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class DeliveryServiceImpl implements DeliveryService {
    private DeliveryOrderRepository orderRepository;

    public void setOrderRepository(@Autowired DeliveryOrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> orders() {
        return orderRepository.allOrders();
    }

    @Override
    public List<Order> ordersByUserID(String userID) {
        return orderRepository.listOrdersbyUserID(userID);
    }

    @Override
    public boolean addOrder(Order order) {
        log.info("add order: {}", order);
        return orderRepository.addOrder(order);
    }
}
