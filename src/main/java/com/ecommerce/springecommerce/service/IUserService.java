package com.ecommerce.springecommerce.service;

import com.ecommerce.springecommerce.model.User;

import java.util.Optional;

public interface IUserService {
    Optional<User> findById(Integer id);
}
