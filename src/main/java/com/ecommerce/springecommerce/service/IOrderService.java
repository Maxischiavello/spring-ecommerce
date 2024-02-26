package com.ecommerce.springecommerce.service;

import com.ecommerce.springecommerce.model.Order;
import com.ecommerce.springecommerce.model.User;

import java.util.List;

public interface IOrderService {
    List<Order> findAll();
    Order save(Order order);
    String generateOrderNumber();
    List<Order> findByUser(User user);
}
