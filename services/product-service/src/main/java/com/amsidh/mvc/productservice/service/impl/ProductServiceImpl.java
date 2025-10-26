package com.amsidh.mvc.productservice.service.impl;

import com.amsidh.mvc.productservice.dto.ProductPurchaseRequest;
import com.amsidh.mvc.productservice.dto.ProductPurchaseResponse;
import com.amsidh.mvc.productservice.dto.CreateProductRequest;
import com.amsidh.mvc.productservice.dto.CreateProductResponse;
import com.amsidh.mvc.productservice.entity.Product;
import com.amsidh.mvc.productservice.exception.ProductPurchaseException;
import com.amsidh.mvc.productservice.repository.ProductRepository;
import com.amsidh.mvc.productservice.service.ProductService;
import com.amsidh.mvc.productservice.util.ProductMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public Integer createProduct(CreateProductRequest createProductRequest) {
        Product product = ProductMapper.toProduct(createProductRequest);
        return productRepository.save(product).getId();
    }

    @Override
    public List<ProductPurchaseResponse> purchaseProduct(
            List<ProductPurchaseRequest> productPurchaseRequests) {
        log.info("Inside purchaseProduct method of ProductServiceImpl");
        final List<Integer> productIds = productPurchaseRequests.stream()
                .map(ProductPurchaseRequest::productId)
                .toList();
        // Look whether all products are available in the store
        log.info("Look whether all products are available in the store");
        final List<Product> storeProducts = productRepository.findAllById(productIds);

        if (storeProducts.size() != productIds.size()) {
            log.error("One or more products not found");
            throw new ProductPurchaseException("One or more products not found");
        }
        // Check for the available quantity
        log.info("Check for the available quantity");
        final List<ProductPurchaseRequest> sortedProductsById = productPurchaseRequests
                .stream()
                .sorted(Comparator.comparing(ProductPurchaseRequest::productId))
                .toList();
        // Prepare the response
        log.info("Prepare ProductPurchaseResponse response");
        return sortedProductsById.stream()
                .map(productPurchaseRequest -> {
                    Product storeProduct = storeProducts.stream()
                            .filter(product -> product.getId().equals(productPurchaseRequest.productId()))
                            .findFirst()
                            .orElseThrow(() -> new ProductPurchaseException("Product not found with id: " + productPurchaseRequest.productId()));
                    if (storeProduct.getAvailableQuantity() < productPurchaseRequest.quantity()) {
                        throw new ProductPurchaseException("Insufficient quantity for product id: " + productPurchaseRequest.productId());
                    }
                    // Update the product available quantity after purchase
                    log.info("Available quantity for product id: {} is: {}", storeProduct.getId(), storeProduct.getAvailableQuantity());
                    storeProduct.setAvailableQuantity(storeProduct.getAvailableQuantity() - productPurchaseRequest.quantity());
                    final Product updateAvailableQuantityProduct = productRepository.save(storeProduct);
                    log.info("Updated available quantity for product id: {} is: {}", updateAvailableQuantityProduct.getId(), updateAvailableQuantityProduct.getAvailableQuantity());
                    return ProductMapper.toProductPurchaseResponse(storeProduct, productPurchaseRequest.quantity());
                }).toList();
    }

    @Override
    public CreateProductResponse getProductById(Integer productId) {
        log.info("Inside getProductById ProductServiceImpl");
        return productRepository.findById(productId).map(ProductMapper::toProductResponse)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));
    }

    @Override
    public List<CreateProductResponse> getAllProducts() {
        log.info("Inside getAllProducts ProductServiceImpl");
        return productRepository.findAll().stream().map(ProductMapper::toProductResponse).toList();
    }
}
