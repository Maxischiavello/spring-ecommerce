package com.ecommerce.springecommerce.controller;

import com.ecommerce.springecommerce.model.User;
import com.ecommerce.springecommerce.service.IUserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService userService;

    @GetMapping("/register")
    public String register() {
        return "user/register";
    }

    @PostMapping("/save")
    public String register(User user) {
        LOGGER.info("Registered user: {}", user);
        user.setType("USER");
        userService.save(user);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @PostMapping("/login")
    public String login(User user, HttpSession session) {
        Optional<User> loggedInUser = userService.findByEmail(user.getEmail());
        //LOGGER.info("Logged in user: {}", loggedInUser.get());

        if (loggedInUser.isPresent()) {
            session.setAttribute("userId", loggedInUser.get().getId());
            if (loggedInUser.get().getType().equals("ADMIN")) {
                return "redirect:/admin";
            }
        } else {
            LOGGER.error("User not found");
        }

        return "redirect:/";
    }

    @GetMapping("/shop")
    public String shop(Model model, HttpSession session) {
        model.addAttribute("session", session.getAttribute("userId"));

        return "user/shop";
    }
}
