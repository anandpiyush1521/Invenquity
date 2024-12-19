package com.inventorymanagementsystem.server.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventorymanagementsystem.server.entities.Product;
import com.inventorymanagementsystem.server.entities.User;
import com.inventorymanagementsystem.server.helper.ProductIdGenerator;
import com.inventorymanagementsystem.server.helper.ResourceNotFoundException;
import com.inventorymanagementsystem.server.repositories.ProductRepo;
import com.inventorymanagementsystem.server.repositories.UserRepo;
import com.inventorymanagementsystem.server.service.ProductService;
import com.inventorymanagementsystem.server.service.UserService;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;

    @Override
    public Product addProduct(Product product) {
        String generatedId;
        do {
            generatedId = ProductIdGenerator.generateProductId();
        } while (productRepo.existsById(generatedId));
        product.setId(generatedId);

        // Ensure the user is set
        if (product.getUser() == null) {
            User currentUser = userService.getCurrentUser();
            product.setUser(currentUser);
        } else if (product.getUser().getId() != null) {
            User user = userRepo.findById(product.getUser().getId()).orElseThrow(() -> new RuntimeException("User not found"));
            product.setUser(user);
        }

        return productRepo.save(product);
    }

    @Override
    public Product updateProductBySkuCode(String skuCode, Product product) {
        List<Product> products = productRepo.findBySkuCodeIgnoreCase(skuCode);
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("Product not found");
        }
        Product existingProduct = products.get(0);
        updateProductDetails(existingProduct, product);
        return productRepo.save(existingProduct);
    }

    @Override
    public Product updateProductByName(String productName, Product product) {
        List<Product> products = productRepo.findByProductNameIgnoreCase(productName);
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("Product not found");
        }
        Product existingProduct = products.get(0);
        updateProductDetails(existingProduct, product);
        return productRepo.save(existingProduct);
    }

    @Override
    public Product updateProductByCategory(String productCategory, Product product) {
        List<Product> products = productRepo.findByProductCategoryIgnoreCase(productCategory);
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("Product not found");
        }
        Product existingProduct = products.get(0);
        updateProductDetails(existingProduct, product);
        return productRepo.save(existingProduct);
    }

    @Override
    public Product getProductById(String id) {
        return productRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    @Override
    public List<Product> getProductBySkuCode(String skuCode) {
        return productRepo.findBySkuCodeIgnoreCase(skuCode);
    }

    @Override
    public List<Product> getProductByName(String productName) {
        return productRepo.findByProductNameIgnoreCase(productName);
    }

    @Override
    public List<Product> getProductByCategory(String productCategory) {
        return productRepo.findByProductCategoryIgnoreCase(productCategory);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    @Override
    public void deleteProductBySkuCode(String skuCode) {
        List<Product> products = productRepo.findBySkuCodeIgnoreCase(skuCode);
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("Product not found");
        }
        productRepo.delete(products.get(0));
    }

    @Override
    public void deleteProductByName(String productName) {
        List<Product> products = productRepo.findByProductNameIgnoreCase(productName);
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("Product not found");
        }
        productRepo.delete(products.get(0));
    }

    @Override
    public void deleteProductByCategory(String productCategory) {
        List<Product> products = productRepo.findByProductCategoryIgnoreCase(productCategory);
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("Product not found");
        }
        productRepo.delete(products.get(0));
    }

    @Override
    public void checkProductQuantity(Product product) {
        if (product.getQuantity() < product.getMinimumProducts()) {
            System.out.println("Alert: The quantity of the product " + product.getProductName() + " is less than the minimum required products.");
        }
    }    

    private void updateProductDetails(Product existingProduct, Product newProduct) {
        existingProduct.setProductCategory(newProduct.getProductCategory());
        existingProduct.setProductName(newProduct.getProductName());
        existingProduct.setSkuCode(newProduct.getSkuCode());
        existingProduct.setRating(newProduct.getRating());
        existingProduct.setQuality(newProduct.getQuality());
        existingProduct.setQuantity(newProduct.getQuantity());
        existingProduct.setMinimumProducts(newProduct.getMinimumProducts());
        existingProduct.setPrice(newProduct.getPrice());
        existingProduct.setDescription(newProduct.getDescription());
        existingProduct.setUser(newProduct.getUser());
    }
}