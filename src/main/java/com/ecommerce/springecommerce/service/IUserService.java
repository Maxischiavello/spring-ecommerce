package com.ecommerce.springecommerce.service;

import com.ecommerce.springecommerce.model.User;
import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<User> findAll();
    Optional<User> findById(Integer id);
    User save(User user);
    Optional<User> findByEmail(String email);
}
