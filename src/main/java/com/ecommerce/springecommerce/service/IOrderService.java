package com.ecommerce.springecommerce.service;

import com.ecommerce.springecommerce.model.Order;
import com.ecommerce.springecommerce.model.User;

import java.util.List;
import java.util.Optional;

public interface IOrderService {
    List<Order> findAll();
    Optional<Order> findById(Integer id);
    Order save(Order order);
    String generateOrderNumber();
    List<Order> findByUser(User user);
}
