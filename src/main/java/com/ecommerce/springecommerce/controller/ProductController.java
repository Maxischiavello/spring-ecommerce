package com.ecommerce.springecommerce.controller;

import java.io.IOException;
import java.util.Optional;

import com.ecommerce.springecommerce.model.Product;
import com.ecommerce.springecommerce.model.User;
import com.ecommerce.springecommerce.service.ProductService;
import com.ecommerce.springecommerce.service.UploadFileService;
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
    public String saveProduct(Product product, @RequestParam("img") MultipartFile file) throws IOException {
        LOGGER.info("Product object {}", product);
        User user = new User(1, "", "", "", "", "", "", "");
        product.setUser(user);

        // image upload
        if (product.getId() == null) { // cuando se crea un producto
            String imageName = uploadFileService.saveImage(file);
            product.setImage(imageName);
        } else {
            if (file.isEmpty()) { // cuando editamos el producto pero no la imagen
                Product p = new Product();
                p = productService.getProduct(product.getId()).get();
                product.setImage(p.getImage());
            } else {
                String imageName = uploadFileService.saveImage(file);
                product.setImage(imageName);
            }
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
    public String updateProduct(Product product) {
        productService.updateProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }
}
