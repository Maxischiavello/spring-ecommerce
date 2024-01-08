package com.ecommerce.springecommerce.controller;

import com.ecommerce.springecommerce.model.Order;
import com.ecommerce.springecommerce.model.OrderDetails;
import com.ecommerce.springecommerce.model.Product;
import com.ecommerce.springecommerce.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class HomeController {

    private final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private ProductService productService;

    private List<OrderDetails> orderDetailsList = new ArrayList<OrderDetails>();
    Order order = new Order();

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
    public String addCart(@RequestParam Integer id, @RequestParam Integer amount, Model model) {
        OrderDetails orderDetails = new OrderDetails();
        Product product = new Product();
        double total = 0;

        Optional<Product> optionalProduct = productService.getProduct(id);
        LOGGER.info("Product added to cart: {}", optionalProduct.get());
        LOGGER.info("Amount: {}", amount);
        product = optionalProduct.get();

        orderDetails.setAmount(amount);
        orderDetails.setPrice(product.getPrice());
        orderDetails.setName(product.getName());
        orderDetails.setTotal(product.getPrice() * amount);
        orderDetails.setProduct(product);

        //validar que le producto no se agregue 2 veces
        Integer idProduct = product.getId();
        boolean isAdded = orderDetailsList.stream().anyMatch(p -> p.getProduct().getId() == idProduct);

        if (!isAdded) {
            orderDetailsList.add(orderDetails);
        }

        total = orderDetailsList.stream().mapToDouble(OrderDetails::getTotal).sum();
        order.setTotal(total);
        model.addAttribute("cart", orderDetailsList);
        model.addAttribute("order", order);

        return "/user/cart";
    }

    @GetMapping("/delete/cart/{id}")
    public String deleteProductFromCart(@PathVariable Integer id, Model model) {
        List<OrderDetails> newOrderDetails = new ArrayList<>();

        for(OrderDetails od : orderDetailsList) {
            if(od.getProduct().getId() != id) {
                newOrderDetails.add(od);
            }
        }

        orderDetailsList = newOrderDetails;

        double total = 0;
        total = orderDetailsList.stream().mapToDouble(OrderDetails::getTotal).sum();
        order.setTotal(total);

        model.addAttribute("cart", orderDetailsList);
        model.addAttribute("order", order);


        return "/user/cart";
    }

    @GetMapping("/get_cart")
    public String getCart(Model model) {
        model.addAttribute("cart", orderDetailsList);
        model.addAttribute("order", order);
        return "/user/cart";
    }
}
