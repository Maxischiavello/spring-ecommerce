package com.ecommerce.springecommerce.controller;

import com.ecommerce.springecommerce.model.Product;
import com.ecommerce.springecommerce.model.User;
import com.ecommerce.springecommerce.service.ProductService;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    private ProductService productService;

    @GetMapping("")
    public String showProducts() {
        return "products/show";
    }

    @GetMapping("/create")
    public String creatProduct() {
        return "products/create";
    }

    @PostMapping("/save")
    public String saveProduct(Product product) {
        LOGGER.info("Product object {}", product);
        User user = new User(1, "", "", "", "", "", "", "");
        product.setUser(user);

        productService.saveProduct(product);
        return "redirect:/products";
    }
}
