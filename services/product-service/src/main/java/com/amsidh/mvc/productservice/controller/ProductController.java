package com.amsidh.mvc.productservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amsidh.mvc.productservice.dto.CreateProductRequest;
import com.amsidh.mvc.productservice.dto.CreateProductResponse;
import com.amsidh.mvc.productservice.dto.ProductPurchaseRequest;
import com.amsidh.mvc.productservice.dto.ProductPurchaseResponse;
import com.amsidh.mvc.productservice.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for product operations.
 *
 * Provides endpoints to create products, purchase products and retrieve product
 * details.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/products")
@CrossOrigin(origins = "*")
public class ProductController {

    // Injects the ProductService dependency via constructor
    private final ProductService productService;

    /**
     * Create a new product.
     * 
     * @param createProductRequest payload containing product details
     * @return id of created product with HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<Integer> createProduct(@RequestBody @Valid CreateProductRequest createProductRequest) {
        log.info("Creating product");
        Integer productId = productService.createProduct(createProductRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(productId);
    }

    /**
     * Purchase one or more products.
     * 
     * @param productPurchaseRequests list of product purchase requests
     * @return list of purchase responses for each requested product
     */
    @PostMapping("/purchase")
    public ResponseEntity<List<ProductPurchaseResponse>> purchaseProducts(
            @RequestBody @Valid List<ProductPurchaseRequest> productPurchaseRequests) {
        log.info("Purchasing products: {}", productPurchaseRequests);
        return ResponseEntity.ok(productService.purchaseProduct(productPurchaseRequests));
    }

    /**
     * Retrieve a product by its id.
     * 
     * @param productId product id
     * @return product details
     */
    @GetMapping("/{product-id}")
    public ResponseEntity<CreateProductResponse> getProductById(@PathVariable("product-id") Integer productId) {
        log.info("Getting product by id: {}", productId);
        CreateProductResponse createProductResponse = productService.getProductById(productId);
        return ResponseEntity.ok(createProductResponse);
    }

    /**
     * Retrieve all products.
     * 
     * @return list of all products
     */
    @GetMapping
    public ResponseEntity<List<CreateProductResponse>> getAllProducts() {
        log.info("Getting all products");
        return ResponseEntity.ok(productService.getAllProducts());
    }

}
