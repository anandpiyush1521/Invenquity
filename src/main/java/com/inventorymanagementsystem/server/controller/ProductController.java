package com.inventorymanagementsystem.server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventorymanagementsystem.server.entities.Product;
import com.inventorymanagementsystem.server.service.ProductService;

@RestController
@RequestMapping("/api/invenquity/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        try {
            Product createdProduct = productService.addProduct(product);
            return ResponseEntity.ok(createdProduct);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/sku/{skuCode}")
    public ResponseEntity<Product> updateProductBySkuCode(@PathVariable String skuCode, @RequestBody Product product) {
        try {
            Product updatedProduct = productService.updateProductBySkuCode(skuCode, product);

            if(updatedProduct.getQuantity() <= updatedProduct.getMinimumProducts()){
                productService.notifyAdmins(updatedProduct);
            }

            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/name/{productName}")
    public ResponseEntity<Product> updateProductByName(@PathVariable String productName, @RequestBody Product product) {
        try {
            Product updatedProduct = productService.updateProductByName(productName, product);

            if(updatedProduct.getQuantity() <= updatedProduct.getMinimumProducts()){
                productService.notifyAdmins(updatedProduct);
            }

            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/category/{productCategory}")
    public ResponseEntity<Product> updateProductByCategory(@PathVariable String productCategory, @RequestBody Product product) {
        try {
            Product updatedProduct = productService.updateProductByCategory(productCategory, product);

            if(updatedProduct.getQuantity() <= updatedProduct.getMinimumProducts()){
                productService.notifyAdmins(updatedProduct);
            }
            
            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) { // Updated to use String
        try {
            Product product = productService.getProductById(id);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/sku/{skuCode}")
    public ResponseEntity<List<Product>> getProductBySkuCode(@PathVariable String skuCode) {
        try {
            List<Product> products = productService.getProductBySkuCode(skuCode);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/name/{productName}")
    public ResponseEntity<List<Product>> getProductByName(@PathVariable String productName) {
        try {
            List<Product> products = productService.getProductByName(productName);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/category/{productCategory}")
    public ResponseEntity<List<Product>> getProductByCategory(@PathVariable String productCategory) {
        try {
            List<Product> products = productService.getProductByCategory(productCategory);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/sku/{skuCode}")
    public ResponseEntity<Void> deleteProductBySkuCode(@PathVariable String skuCode) {
        try {
            productService.deleteProductBySkuCode(skuCode);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/name/{productName}")
    public ResponseEntity<Void> deleteProductByName(@PathVariable String productName) {
        try {
            productService.deleteProductByName(productName);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/category/{productCategory}")
    public ResponseEntity<Void> deleteProductByCategory(@PathVariable String productCategory) {
        try {
            productService.deleteProductByCategory(productCategory);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}
