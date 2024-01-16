package com.ecommerce.springecommerce.service;

import com.ecommerce.springecommerce.model.OrderDetails;

public interface IOrderDetailsService {
    OrderDetails save(OrderDetails orderDetails);
}
