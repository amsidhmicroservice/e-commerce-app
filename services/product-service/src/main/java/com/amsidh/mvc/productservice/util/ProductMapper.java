package com.amsidh.mvc.productservice.util;

import com.amsidh.mvc.productservice.dto.ProductPurchaseResponse;
import com.amsidh.mvc.productservice.dto.ProductRequest;
import com.amsidh.mvc.productservice.dto.ProductResponse;
import com.amsidh.mvc.productservice.entity.Category;
import com.amsidh.mvc.productservice.entity.Product;

public interface ProductMapper {

    static Product toProduct(ProductRequest productRequest) {
        return Product.builder()
                .name(productRequest.name())
                .description(productRequest.description())
                .price(productRequest.price())
                .availableQuantity(productRequest.availableQuantity())
                .price(productRequest.price())
                .category(Category.builder()
                        .id(productRequest.categoryId())
                        .build())
                .build();
    }

    static ProductResponse toProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getAvailableQuantity(),
                product.getPrice(),
                product.getCategory().getId(),
                product.getCategory().getName(),
                product.getCategory().getDescription()
        );
    }

    static ProductPurchaseResponse toProductPurchaseResponse(Product storeProduct, double quantity) {
        return new ProductPurchaseResponse(
                storeProduct.getId(),
                storeProduct.getName(),
                storeProduct.getPrice(),
                quantity
        );
    }
}
