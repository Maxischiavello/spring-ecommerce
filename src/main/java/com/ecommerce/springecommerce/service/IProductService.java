package com.ecommerce.springecommerce.service;

import com.ecommerce.springecommerce.model.Product;

import java.util.List;
import java.util.Optional;

public interface IProductService {
    public Product saveProduct(Product product);
    public Optional<Product> getProduct(Integer id);
    public void updateProduct(Product product);
    public void deleteProduct(Integer id);
    public List<Product> findAll();
}
