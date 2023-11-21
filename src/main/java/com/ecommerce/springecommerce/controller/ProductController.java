package com.ecommerce.springecommerce.controller;

import java.util.Optional;
import com.ecommerce.springecommerce.model.Product;
import com.ecommerce.springecommerce.model.User;
import com.ecommerce.springecommerce.service.ProductService;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    private ProductService productService;

    @GetMapping("")
    public String showProducts(Model model) {
        model.addAttribute("products", productService.findAll());
        return "products/show";
    }

    @GetMapping("/create")
    public String createProduct() {
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

    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable Integer id, Model model) {
        Product product = new Product();
        Optional<Product> optionalProduct = productService.getProduct(id);
        product = optionalProduct.get();

        LOGGER.info("Searched product: {}", product);
        model.addAttribute("product", product);

        return "products/edit";
    }

    @PostMapping("/update")
    public String updateProduct(Product product) {
        productService.updateProduct(product);
        return "redirect:/products";
    }
}
