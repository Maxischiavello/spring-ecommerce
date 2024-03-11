package com.ecommerce.springecommerce.controller;

import com.ecommerce.springecommerce.model.Order;
import com.ecommerce.springecommerce.model.User;
import com.ecommerce.springecommerce.service.IOrderService;
import com.ecommerce.springecommerce.service.IUserService;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService userService;

    @Autowired
    private IOrderService orderService;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/register")
    public String register() {
        return "user/register";
    }

    @PostMapping("/save")
    public String register(User user) {
        LOGGER.info("Registered user: {}", user);
        user.setUsername(user.getEmail());
        user.setType("USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "user/login";
    }

    @GetMapping("/log_in")
    public String login(HttpSession session) {
        Optional<User> loggedInUser;
        loggedInUser = userService.findById(Integer.parseInt(session.getAttribute("userId").toString()));

        if (loggedInUser.isPresent()) {
            session.setAttribute("userId", loggedInUser.get().getId());
            if (loggedInUser.get().getType().equals("ADMIN")) {
                LOGGER.info("User type: {}", loggedInUser.get().getType());
                return "redirect:/";
            } else {
                LOGGER.info("User type: {}", loggedInUser.get().getType());
                return "redirect:/";
            }
        } else {
            LOGGER.error("User not found");
        }

        return "redirect:/";
    }

    @GetMapping("/purchases")
    public String purchases(Model model, HttpSession session) {
        model.addAttribute("session", session.getAttribute("userId"));
        User user = userService.findById(Integer.parseInt(session.getAttribute("userId").toString())).get();
        List<Order> orders = orderService.findByUser(user);
        LOGGER.info("Orders: {}", orders);
        model.addAttribute("orders", orders);
        return "user/purchases";
    }

    @GetMapping("/purchase_details/{id}")
    public String purchaseDetails(@PathVariable Integer id, HttpSession session, Model model) {
        model.addAttribute("session", session.getAttribute("userId"));
        LOGGER.info("Order ID: {}", id);
        Optional<Order> order = orderService.findById(id);
        model.addAttribute("purchase_details", order.get().getOrderDetails());
        return "user/purchase_details";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("userId");
        LOGGER.info("Session userId: {}", session.getAttribute("userId"));
        return "redirect:/";
    }
}
