package com.inventorymanagementsystem.server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.inventorymanagementsystem.server.entities.Product;

public interface ProductRepo extends JpaRepository<Product, String> {
    @Query("SELECT p FROM Product p WHERE LOWER(p.skuCode) = LOWER(:skuCode)")
    List<Product> findBySkuCodeIgnoreCase(@Param("skuCode") String skuCode);

    @Query("SELECT p FROM Product p WHERE LOWER(p.productName) = LOWER(:productName)")
    List<Product> findByProductNameIgnoreCase(@Param("productName") String productName);

    @Query("SELECT p FROM Product p WHERE LOWER(p.productCategory) = LOWER(:productCategory)")
    List<Product> findByProductCategoryIgnoreCase(@Param("productCategory") String productCategory);
}

