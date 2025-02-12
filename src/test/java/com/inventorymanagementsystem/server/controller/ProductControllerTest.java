package com.inventorymanagementsystem.server.controller;

import com.inventorymanagementsystem.server.entities.Product;
import com.inventorymanagementsystem.server.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddProduct_Success() {
        Product product = new Product();
        when(productService.addProduct(product)).thenReturn(product);

        ResponseEntity<Product> response = productController.addProduct(product);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(product, response.getBody());
        verify(productService, times(1)).addProduct(product);
    }

    @Test
    public void testAddProduct_Failure() {
        Product product = new Product();
        when(productService.addProduct(product)).thenThrow(new RuntimeException("Error adding product"));

        ResponseEntity<Product> response = productController.addProduct(product);

        assertEquals(500, response.getStatusCodeValue());
        verify(productService, times(1)).addProduct(product);
    }

    @Test
    public void testAddProducts_Success() {
        List<Product> products = Arrays.asList(new Product(), new Product());
        when(productService.addProducts(products)).thenReturn(products);

        ResponseEntity<List<Product>> response = productController.addProducts(products);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(products, response.getBody());
        verify(productService, times(1)).addProducts(products);
    }

    @Test
    public void testAddProducts_Failure() {
        List<Product> products = Arrays.asList(new Product(), new Product());
        when(productService.addProducts(products)).thenThrow(new RuntimeException("Error adding products"));

        ResponseEntity<List<Product>> response = productController.addProducts(products);

        assertEquals(500, response.getStatusCodeValue());
        verify(productService, times(1)).addProducts(products);
    }

    @Test
    public void testUpdateProductBySkuCode_Success() {
        Product product = new Product();
        when(productService.updateProductBySkuCode("sku123", product)).thenReturn(product);

        ResponseEntity<Product> response = productController.updateProductBySkuCode("sku123", product);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(product, response.getBody());
        verify(productService, times(1)).updateProductBySkuCode("sku123", product);
    }

    @Test
    public void testUpdateProductBySkuCode_Failure() {
        Product product = new Product();
        when(productService.updateProductBySkuCode("sku123", product)).thenThrow(new RuntimeException("Error updating product"));

        ResponseEntity<Product> response = productController.updateProductBySkuCode("sku123", product);

        assertEquals(500, response.getStatusCodeValue());
        verify(productService, times(1)).updateProductBySkuCode("sku123", product);
    }

    @Test
    public void testUpdateProductByName_Success() {
        Product product = new Product();
        when(productService.updateProductByName("productName", product)).thenReturn(product);

        ResponseEntity<Product> response = productController.updateProductByName("productName", product);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(product, response.getBody());
        verify(productService, times(1)).updateProductByName("productName", product);
    }

    @Test
    public void testUpdateProductByName_Failure() {
        Product product = new Product();
        when(productService.updateProductByName("productName", product)).thenThrow(new RuntimeException("Error updating product"));

        ResponseEntity<Product> response = productController.updateProductByName("productName", product);

        assertEquals(500, response.getStatusCodeValue());
        verify(productService, times(1)).updateProductByName("productName", product);
    }

    @Test
    public void testUpdateProductByCategory_Success() {
        Product product = new Product();
        when(productService.updateProductByCategory("category", product)).thenReturn(product);

        ResponseEntity<Product> response = productController.updateProductByCategory("category", product);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(product, response.getBody());
        verify(productService, times(1)).updateProductByCategory("category", product);
    }

    @Test
    public void testUpdateProductByCategory_Failure() {
        Product product = new Product();
        when(productService.updateProductByCategory("category", product)).thenThrow(new RuntimeException("Error updating product"));

        ResponseEntity<Product> response = productController.updateProductByCategory("category", product);

        assertEquals(500, response.getStatusCodeValue());
        verify(productService, times(1)).updateProductByCategory("category", product);
    }

    @Test
    public void testGetProductById_Success() {
        Product product = new Product();
        when(productService.getProductById("1")).thenReturn(product);

        ResponseEntity<Product> response = productController.getProductById("1");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(product, response.getBody());
        verify(productService, times(1)).getProductById("1");
    }

    @Test
    public void testGetProductById_Failure() {
        when(productService.getProductById("1")).thenThrow(new RuntimeException("Error fetching product"));

        ResponseEntity<Product> response = productController.getProductById("1");

        assertEquals(500, response.getStatusCodeValue());
        verify(productService, times(1)).getProductById("1");
    }

    @Test
    public void testGetProductBySkuCode_Success() {
        List<Product> products = Arrays.asList(new Product(), new Product());
        when(productService.getProductBySkuCode("sku123")).thenReturn(products);

        ResponseEntity<List<Product>> response = productController.getProductBySkuCode("sku123");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(products, response.getBody());
        verify(productService, times(1)).getProductBySkuCode("sku123");
    }

    @Test
    public void testGetProductBySkuCode_Failure() {
        when(productService.getProductBySkuCode("sku123")).thenThrow(new RuntimeException("Error fetching products"));

        ResponseEntity<List<Product>> response = productController.getProductBySkuCode("sku123");

        assertEquals(500, response.getStatusCodeValue());
        verify(productService, times(1)).getProductBySkuCode("sku123");
    }

    @Test
    public void testGetProductByName_Success() {
        List<Product> products = Arrays.asList(new Product(), new Product());
        when(productService.getProductByName("productName")).thenReturn(products);

        ResponseEntity<List<Product>> response = productController.getProductByName("productName");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(products, response.getBody());
        verify(productService, times(1)).getProductByName("productName");
    }

    @Test
    public void testGetProductByName_Failure() {
        when(productService.getProductByName("productName")).thenThrow(new RuntimeException("Error fetching products"));

        ResponseEntity<List<Product>> response = productController.getProductByName("productName");

        assertEquals(500, response.getStatusCodeValue());
        verify(productService, times(1)).getProductByName("productName");
    }

    @Test
    public void testGetProductByCategory_Success() {
        List<Product> products = Arrays.asList(new Product(), new Product());
        when(productService.getProductByCategory("category")).thenReturn(products);

        ResponseEntity<List<Product>> response = productController.getProductByCategory("category");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(products, response.getBody());
        verify(productService, times(1)).getProductByCategory("category");
    }

    @Test
    public void testGetProductByCategory_Failure() {
        when(productService.getProductByCategory("category")).thenThrow(new RuntimeException("Error fetching products"));

        ResponseEntity<List<Product>> response = productController.getProductByCategory("category");

        assertEquals(500, response.getStatusCodeValue());
        verify(productService, times(1)).getProductByCategory("category");
    }

    @Test
    public void testGetAllProducts_Success() {
        List<Product> products = Arrays.asList(new Product(), new Product());
        when(productService.getAllProducts()).thenReturn(products);

        ResponseEntity<List<Product>> response = productController.getAllProducts();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(products, response.getBody());
        verify(productService, times(1)).getAllProducts();
    }

    @Test
    public void testGetAllProducts_Failure() {
        when(productService.getAllProducts()).thenThrow(new RuntimeException("Error fetching products"));

        ResponseEntity<List<Product>> response = productController.getAllProducts();

        assertEquals(500, response.getStatusCodeValue());
        verify(productService, times(1)).getAllProducts();
    }

    @Test
    public void testDeleteProductById_Success() {
        doNothing().when(productService).deleteProductById("1");

        ResponseEntity<Void> response = productController.deleteProductById("1");

        assertEquals(204, response.getStatusCodeValue());
        verify(productService, times(1)).deleteProductById("1");
    }

    @Test
    public void testDeleteProductById_Failure() {
        doThrow(new RuntimeException("Error deleting product")).when(productService).deleteProductById("1");

        ResponseEntity<Void> response = productController.deleteProductById("1");

        assertEquals(500, response.getStatusCodeValue());
        verify(productService, times(1)).deleteProductById("1");
    }

    @Test
    public void testDeleteProductBySkuCode_Success() {
        doNothing().when(productService).deleteProductBySkuCode("sku123");

        ResponseEntity<Void> response = productController.deleteProductBySkuCode("sku123");

        assertEquals(204, response.getStatusCodeValue());
        verify(productService, times(1)).deleteProductBySkuCode("sku123");
    }

    @Test
    public void testDeleteProductBySkuCode_Failure() {
        doThrow(new RuntimeException("Error deleting product")).when(productService).deleteProductBySkuCode("sku123");

        ResponseEntity<Void> response = productController.deleteProductBySkuCode("sku123");

        assertEquals(500, response.getStatusCodeValue());
        verify(productService, times(1)).deleteProductBySkuCode("sku123");
    }

    @Test
    public void testDeleteProductByName_Success() {
        doNothing().when(productService).deleteProductByName("productName");

        ResponseEntity<Void> response = productController.deleteProductByName("productName");

        assertEquals(204, response.getStatusCodeValue());
        verify(productService, times(1)).deleteProductByName("productName");
    }

    @Test
    public void testDeleteProductByName_Failure() {
        doThrow(new RuntimeException("Error deleting product")).when(productService).deleteProductByName("productName");

        ResponseEntity<Void> response = productController.deleteProductByName("productName");

        assertEquals(500, response.getStatusCodeValue());
        verify(productService, times(1)).deleteProductByName("productName");
    }

    @Test
    public void testDeleteProductByCategory_Success() {
        doNothing().when(productService).deleteProductByCategory("category");

        ResponseEntity<Void> response = productController.deleteProductByCategory("category");

        assertEquals(204, response.getStatusCodeValue());
        verify(productService, times(1)).deleteProductByCategory("category");
    }

    @Test
    public void testDeleteProductByCategory_Failure() {
        doThrow(new RuntimeException("Error deleting product")).when(productService).deleteProductByCategory("category");

        ResponseEntity<Void> response = productController.deleteProductByCategory("category");

        assertEquals(500, response.getStatusCodeValue());
        verify(productService, times(1)).deleteProductByCategory("category");
    }
}