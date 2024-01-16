package com.ecommerce.springecommerce.service;

import com.ecommerce.springecommerce.model.Order;
import com.ecommerce.springecommerce.repository.IOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImp implements IOrderService {

    @Autowired
    private IOrderRepository orderRepository;

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    public String generateOrderNumber() {
        int number = 0;
        String orderNumber = "";
        List<Order> orders = this.findAll();
        List<Integer> numbers = new ArrayList<>();
        orders.stream().forEach(o -> numbers.add(Integer.parseInt(o.getNumber())));

        if (orders.isEmpty()) {
            number = 1;
        } else {
            number = numbers.stream().max(Integer::compare).get();
            number++;
        }

        if (number < 10) {
            orderNumber = "000000000" + String.valueOf(number);
        } else if (number < 100) {
            orderNumber = "00000000" + String.valueOf(number);
        } else if (number < 1000) {
            orderNumber = "0000000" + String.valueOf(number);
        }

        return orderNumber;
    }
}
