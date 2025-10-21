package com.amsidh.mvc.productservice.controller;

import com.amsidh.mvc.productservice.dto.ProductPurchaseRequest;
import com.amsidh.mvc.productservice.dto.ProductPurchaseResponse;
import com.amsidh.mvc.productservice.dto.CreateProductRequest;
import com.amsidh.mvc.productservice.dto.CreateProductResponse;
import com.amsidh.mvc.productservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Integer> createProduct(@RequestBody @Valid CreateProductRequest createProductRequest) {
        log.info("Creating product");
        Integer productId = productService.createProduct(createProductRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(productId);
    }

    @PostMapping("/purchase")
    public ResponseEntity<List<ProductPurchaseResponse>> purchaseProducts(@RequestBody @Valid List<ProductPurchaseRequest> productPurchaseRequests) {
        log.info("Purchasing products: {}", productPurchaseRequests);
        return ResponseEntity.ok(productService.purchaseProduct(productPurchaseRequests));
    }

    @GetMapping("/{product-id}")
    public ResponseEntity<CreateProductResponse> getProductById(@PathVariable("product-id") Integer productId) {
        log.info("Getting product by id: {}", productId);
        CreateProductResponse createProductResponse = productService.getProductById(productId);
        return ResponseEntity.ok(createProductResponse);
    }

    @GetMapping
    public ResponseEntity<List<CreateProductResponse>> getAllProducts() {
        log.info("Getting all products");
        return ResponseEntity.ok(productService.getAllProducts());
    }

}
