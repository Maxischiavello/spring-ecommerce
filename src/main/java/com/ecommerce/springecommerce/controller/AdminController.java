package com.ecommerce.springecommerce.controller;

import com.ecommerce.springecommerce.model.Product;
import com.ecommerce.springecommerce.model.User;
import com.ecommerce.springecommerce.service.IProductService;
import com.ecommerce.springecommerce.service.IUserService;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IProductService productService;

    private Logger logger = LoggerFactory.getLogger(AdminController.class);


    @GetMapping("")
    public String home(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        logger.info("Products: {}", products);
        return "admin/home";
    }

    @GetMapping("/users")
    public String users(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        logger.info("Users: {}", users);
        return "/admin/users";
    }
}
