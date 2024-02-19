package com.ecommerce.springecommerce.controller;

import java.io.IOException;
import java.util.Optional;

import com.ecommerce.springecommerce.model.Product;
import com.ecommerce.springecommerce.model.User;
import com.ecommerce.springecommerce.service.IProductService;
import com.ecommerce.springecommerce.service.IUserService;
import com.ecommerce.springecommerce.service.UploadFileService;
import com.ecommerce.springecommerce.service.UserServiceImp;
import jakarta.servlet.http.HttpSession;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    private UploadFileService uploadFileService;
    @Autowired
    private IProductService productService;

    @Autowired
    private IUserService userService;

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
    public String saveProduct(Product product, @RequestParam("img") MultipartFile file, HttpSession session) throws IOException {
        LOGGER.info("Product object {}", product);
        User user = userService.findById(Integer.parseInt(session.getAttribute("userId").toString())).get();
        product.setUser(user);

        // image upload
        if (product.getId() == null) {
            String imageName = uploadFileService.saveImage(file);
            product.setImage(imageName);
        }

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
    public String updateProduct(Product product, @RequestParam("img") MultipartFile file) throws IOException {
        Product p = new Product();
        p = productService.getProduct(product.getId()).get();

        if (file.isEmpty()) {
            product.setImage(p.getImage());
        } else {
            if (!p.getImage().equals("default.jpg")) {
                uploadFileService.deleteImage(p.getImage());
            }
            String imageName = uploadFileService.saveImage(file);
            product.setImage(imageName);
        }
        product.setUser(p.getUser());
        productService.updateProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Integer id) {
        Product product = new Product();
        product = productService.getProduct(id).get();

        if (!product.getImage().equals("default.jpg")) {
            uploadFileService.deleteImage(product.getImage());
        }

        productService.deleteProduct(id);
        return "redirect:/products";
    }
}
