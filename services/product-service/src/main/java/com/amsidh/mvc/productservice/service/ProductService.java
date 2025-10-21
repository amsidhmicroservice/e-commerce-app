package com.amsidh.mvc.productservice.service;

import com.amsidh.mvc.productservice.dto.ProductPurchaseRequest;
import com.amsidh.mvc.productservice.dto.ProductPurchaseResponse;
import com.amsidh.mvc.productservice.dto.CreateProductRequest;
import com.amsidh.mvc.productservice.dto.CreateProductResponse;

import java.util.List;

public interface ProductService {
    Integer createProduct(CreateProductRequest createProductRequest);

    List<ProductPurchaseResponse> purchaseProduct(List<ProductPurchaseRequest> productPurchaseRequests);

    CreateProductResponse getProductById(Integer productId);

    List<CreateProductResponse> getAllProducts();
}
