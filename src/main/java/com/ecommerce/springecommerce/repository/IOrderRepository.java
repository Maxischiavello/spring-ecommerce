package com.ecommerce.springecommerce.repository;

import com.ecommerce.springecommerce.model.Order;
import com.ecommerce.springecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUser(User user);
}
