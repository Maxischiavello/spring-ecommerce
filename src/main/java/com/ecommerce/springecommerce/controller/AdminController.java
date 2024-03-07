package com.ecommerce.springecommerce.controller;

import com.ecommerce.springecommerce.model.Order;
import com.ecommerce.springecommerce.model.Product;
import com.ecommerce.springecommerce.model.User;
import com.ecommerce.springecommerce.service.IOrderDetailsService;
import com.ecommerce.springecommerce.service.IOrderService;
import com.ecommerce.springecommerce.service.IProductService;
import com.ecommerce.springecommerce.service.IUserService;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IProductService productService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IOrderDetailsService orderDetailsService;

    private final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);


    @GetMapping("")
    public String home(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        LOGGER.info("Products: {}", products);
        return "admin/home";
    }

    @GetMapping("/users")
    public String users(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        LOGGER.info("Users: {}", users);
        return "/admin/users";
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        List<Order> orders = orderService.findAll();
        model.addAttribute("orders", orders);
        LOGGER.info("Orders: {}", orders);
        return "admin/orders";
    }

    @GetMapping("/order_details/{id}")
    public String orderDetails(@PathVariable Integer id, Model model) {
        LOGGER.info("Order ID: {}", id);
        Order order = orderService.findById(id).get();
        model.addAttribute("order_details", order.getOrderDetails());
        LOGGER.info("Order details: {}", order.getOrderDetails());
        return "admin/order_details";
    }
}
