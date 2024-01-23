package com.ecommerce.springecommerce.service;

import com.ecommerce.springecommerce.model.User;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface IUserService {
    Optional<User> findById(Integer id);
    User save(User user);
    Optional<User> findByEmail(String email);
}
