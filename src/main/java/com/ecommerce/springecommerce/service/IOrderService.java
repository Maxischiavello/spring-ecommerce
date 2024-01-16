package com.ecommerce.springecommerce.service;

import com.ecommerce.springecommerce.model.Order;

import java.util.List;

public interface IOrderService {
    List<Order> findAll();
    Order save(Order order);
    String generateOrderNumber();
}
