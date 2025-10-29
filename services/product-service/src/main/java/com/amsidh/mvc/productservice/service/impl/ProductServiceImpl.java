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
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public Integer createProduct(CreateProductRequest createProductRequest) {
        log.info("Creating new product - Name: {}, Price: {}, Quantity: {}",
                createProductRequest.name(),
                createProductRequest.price(),
                createProductRequest.availableQuantity());
        Product product = ProductMapper.toProduct(createProductRequest);
        Product savedProduct = productRepository.save(product);
        log.info("Product created successfully with ID: {}", savedProduct.getId());
        return savedProduct.getId();
    }

    /**
     * Purchase products and update inventory.
     * 
     * CRITICAL: This method uses @Transactional to ensure ACID properties during
     * inventory updates.
     * Without proper transaction isolation, concurrent purchases could cause race
     * conditions:
     * - Lost updates (two threads read same quantity, both deduct, one update is
     * lost)
     * - Negative inventory (overselling products)
     * 
     * Current implementation relies on database-level locking (REPEATABLE_READ or
     * SERIALIZABLE isolation).
     * For high-concurrency scenarios, consider:
     * 1. Pessimistic locking: @Lock(LockModeType.PESSIMISTIC_WRITE) on repository
     * method
     * 2. Optimistic locking: @Version field in Product entity
     * 3. Distributed locking: Redis-based locks for multi-instance deployments
     */
    @Transactional
    @Override
    public List<ProductPurchaseResponse> purchaseProduct(
            List<ProductPurchaseRequest> productPurchaseRequests) {
        log.info("Processing product purchase request - Total products: {}", productPurchaseRequests.size());

        final List<Integer> productIds = productPurchaseRequests.stream()
                .map(ProductPurchaseRequest::productId)
                .toList();

        // Look whether all products are available in the store
        log.debug("Validating product availability for IDs: {}", productIds);
        final List<Product> storeProducts = productRepository.findAllById(productIds);

        if (storeProducts.size() != productIds.size()) {
            log.error("Product validation failed - Requested: {}, Found: {}", productIds.size(), storeProducts.size());
            throw new ProductPurchaseException("One or more products not found");
        }
        log.info("All {} products found in inventory", storeProducts.size());

        // Check for the available quantity and prepare response
        log.debug("Validating product quantities and updating inventory");
        final List<ProductPurchaseRequest> sortedProductsById = productPurchaseRequests
                .stream()
                .sorted(Comparator.comparing(ProductPurchaseRequest::productId))
                .toList();

        // Process each product purchase
        List<ProductPurchaseResponse> responses = sortedProductsById.stream()
                .map(productPurchaseRequest -> {
                    Product storeProduct = storeProducts.stream()
                            .filter(product -> product.getId().equals(productPurchaseRequest.productId()))
                            .findFirst()
                            .orElseThrow(() -> {
                                log.error("Product not found with ID: {}", productPurchaseRequest.productId());
                                return new ProductPurchaseException(
                                        "Product not found with id: " + productPurchaseRequest.productId());
                            });

                    if (storeProduct.getAvailableQuantity() < productPurchaseRequest.quantity()) {
                        log.error("Insufficient quantity for product ID: {} - Requested: {}, Available: {}",
                                productPurchaseRequest.productId(),
                                productPurchaseRequest.quantity(),
                                storeProduct.getAvailableQuantity());
                        throw new ProductPurchaseException(
                                "Insufficient quantity for product id: " + productPurchaseRequest.productId());
                    }

                    // Update the product available quantity after purchase
                    double previousQuantity = storeProduct.getAvailableQuantity();
                    storeProduct.setAvailableQuantity(
                            storeProduct.getAvailableQuantity() - productPurchaseRequest.quantity());
                    final Product updateAvailableQuantityProduct = productRepository.save(storeProduct);
                    log.info("Inventory updated for product ID: {} - Previous: {}, Purchased: {}, Remaining: {}",
                            updateAvailableQuantityProduct.getId(),
                            previousQuantity,
                            productPurchaseRequest.quantity(),
                            updateAvailableQuantityProduct.getAvailableQuantity());
                    return ProductMapper.toProductPurchaseResponse(storeProduct, productPurchaseRequest.quantity());
                }).toList();

        log.info("Product purchase completed successfully - Total products purchased: {}", responses.size());
        return responses;
    }

    @Override
    public CreateProductResponse getProductById(Integer productId) {
        log.info("Retrieving product with ID: {}", productId);
        return productRepository.findById(productId)
                .map(product -> {
                    log.debug("Product found - ID: {}, Name: {}, Price: {}",
                            product.getId(),
                            product.getName(),
                            product.getPrice());
                    return ProductMapper.toProductResponse(product);
                })
                .orElseThrow(() -> {
                    log.error("Product not found with ID: {}", productId);
                    return new EntityNotFoundException("Product not found with id: " + productId);
                });
    }

    @Override
    public List<CreateProductResponse> getAllProducts() {
        log.info("Retrieving all products from database");
        List<CreateProductResponse> products = productRepository.findAll()
                .stream()
                .map(ProductMapper::toProductResponse)
                .toList();
        log.info("Retrieved {} products", products.size());
        return products;
    }
}
