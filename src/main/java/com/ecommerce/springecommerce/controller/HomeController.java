package com.ecommerce.springecommerce.controller;

import com.ecommerce.springecommerce.model.Order;
import com.ecommerce.springecommerce.model.OrderDetails;
import com.ecommerce.springecommerce.model.Product;
import com.ecommerce.springecommerce.model.User;
import com.ecommerce.springecommerce.service.IOrderDetailsService;
import com.ecommerce.springecommerce.service.IOrderService;
import com.ecommerce.springecommerce.service.IProductService;
import com.ecommerce.springecommerce.service.IUserService;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class HomeController {

    private final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private IProductService productService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IOrderDetailsService orderDetailsService;

    List<OrderDetails> orderDetailsList = new ArrayList<OrderDetails>();

    Order order = new Order();

    @GetMapping("")
    public String home(Model model, HttpSession session) {
        model.addAttribute("products", productService.findAll());
        model.addAttribute("session", session.getAttribute("userId"));

        try {
            LOGGER.info("User ID from session: {}", session.getAttribute("userId"));
            User user = userService.findById(Integer.parseInt(session.getAttribute("userId").toString())).get();
            if(user.getType().equals("ADMIN")) {
                return "/admin/home";
            }
        } catch(Exception e) {
            LOGGER.info("User ID is null");
        }

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
    public String addCart(@RequestParam Integer id, @RequestParam Integer amount, Model model, HttpSession session) {
        if(session.getAttribute("userId")==null) {
            return "redirect:/user/login";
        }

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

        for (OrderDetails od : orderDetailsList) {
            if (od.getProduct().getId() != id) {
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
    public String getCart(Model model, HttpSession session) {
        if(session.getAttribute("userId")==null) {
            return "redirect:/user/login";
        }
        model.addAttribute("cart", orderDetailsList);
        model.addAttribute("order", order);
        model.addAttribute("session", session.getAttribute("userId"));
        return "/user/cart";
    }

    @GetMapping("/order")
    public String order(Model model, HttpSession session) {
        User user = userService.findById(Integer.parseInt(session.getAttribute("userId").toString())).get();

        model.addAttribute("cart", orderDetailsList);
        model.addAttribute("order", order);
        model.addAttribute("user", user);

        return "user/order_details";
    }

    @GetMapping("/save_order")
    public String saveOrder(HttpSession session) {
        Date creationDate = new Date();
        order.setCreationDate(creationDate);
        order.setNumber(orderService.generateOrderNumber());
        User user = userService.findById(Integer.parseInt(session.getAttribute("userId").toString())).get();
        order.setUser(user);
        orderService.save(order);

        for (OrderDetails od : orderDetailsList) {
            od.setOrder(order);
            orderDetailsService.save(od);
        }

        order = new Order();
        orderDetailsList.clear();

        return "redirect:/";
    }

    @PostMapping("/search")
    public String searchProduct(@RequestParam String productName, Model model) {
        LOGGER.info("Searched product: {}", productName);
        List<Product> products = productService.findAll().stream()
                .filter(p -> p.getName().contains(productName)).toList();

        model.addAttribute("products", products);
        return "user/home";
    }
}
