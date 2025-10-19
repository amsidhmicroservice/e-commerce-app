package com.amsidh.mvc.productservice.service;

import com.amsidh.mvc.productservice.dto.ProductPurchaseRequest;
import com.amsidh.mvc.productservice.dto.ProductPurchaseResponse;
import com.amsidh.mvc.productservice.dto.ProductRequest;
import com.amsidh.mvc.productservice.dto.ProductResponse;

import java.util.List;

public interface ProductService {
    Integer createProduct(ProductRequest productRequest);

    List<ProductPurchaseResponse> purchaseProduct(List<ProductPurchaseRequest> productPurchaseRequests);

    ProductResponse getProductById(Integer productId);

    List<ProductResponse> getAllProducts();
}
