package com.ecommerce.springecommerce.service;

import com.ecommerce.springecommerce.model.Order;
import com.ecommerce.springecommerce.repository.IOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImp implements IOrderService {

    @Autowired
    private IOrderRepository orderRepository;

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }
}
