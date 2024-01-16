package com.ecommerce.springecommerce.service;

import com.ecommerce.springecommerce.model.OrderDetails;
import com.ecommerce.springecommerce.repository.IOrderDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailsServiceImp implements IOrderDetailsService {

    @Autowired
    private IOrderDetailsRepository orderDetailsRepository;

    @Override
    public OrderDetails save(OrderDetails orderDetails) {
        return orderDetailsRepository.save(orderDetails);
    }
}
