package com.ecommerce.springecommerce.controller;

import com.ecommerce.springecommerce.model.Product;
import com.ecommerce.springecommerce.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/")
public class HomeController {

    private final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private ProductService productService;

    @GetMapping("")
    public String home(Model model) {
        model.addAttribute("products", productService.findAll());
        return "user/home";
    }

    @GetMapping("product_home/{id}")
    public String productHome(@PathVariable Integer id, Model model) {
        LOGGER.info("Product ID: {}", id);
        Product product = new Product();
        Optional<Product> productOptional = productService.getProduct(id);
        product = productOptional.get();
        model.addAttribute("product", product);
        return "user/product_home";
    }

    @PostMapping("/cart")
    public String addCart() {
        return "/user/cart";
    }
}
