package com.ecommerce.springecommerce.service;

import com.ecommerce.springecommerce.model.User;
import javax.servlet.http.HttpSession;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private IUserService userService;

    @Autowired
    private BCryptPasswordEncoder bCrypt;

    @Autowired
    HttpSession session;

    private final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImp.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOGGER.info("Username: ");
        Optional<User> optionalUser = userService.findByEmail(username);
        if (optionalUser.isPresent()) {
            LOGGER.info("User ID: {}", optionalUser.get().getId());
            session.setAttribute("userId", optionalUser.get().getId());
            User user = optionalUser.get();
            return org.springframework.security.core.userdetails.User
                    .builder()
                    .username(user.getUsername())
                    .password(bCrypt.encode(user.getPassword()))
                    .roles(user.getType())
                    .build();
        } else {
            throw new UsernameNotFoundException("User not found.");
        }
    }
}
