package com.inventorymanagementsystem.server.service;

import java.util.List;

import com.inventorymanagementsystem.server.entities.Product;

public interface ProductService {
    Product addProduct(Product product);

    Product updateProductBySkuCode(String skuCode, Product product);
    Product updateProductByName(String productName, Product product);
    Product updateProductByCategory(String productCategory, Product product);

    Product getProductById(String id);
    List<Product> getProductBySkuCode(String skuCode);
    List<Product> getProductByName(String productName);
    List<Product> getProductByCategory(String productCategory);
    List<Product> getAllProducts();

    void deleteProductBySkuCode(String skuCode);
    void deleteProductByName(String productName);
    void deleteProductByCategory(String productCategory);

    void checkProductQuantity(Product product);
}
