package com.amsidh.mvc.productservice.util;

import com.amsidh.mvc.productservice.dto.ProductPurchaseResponse;
import com.amsidh.mvc.productservice.dto.CreateProductRequest;
import com.amsidh.mvc.productservice.dto.CreateProductResponse;
import com.amsidh.mvc.productservice.entity.Category;
import com.amsidh.mvc.productservice.entity.Product;

public interface ProductMapper {

    static Product toProduct(CreateProductRequest createProductRequest) {
        return Product.builder()
                .name(createProductRequest.name())
                .description(createProductRequest.description())
                .price(createProductRequest.price())
                .availableQuantity(createProductRequest.availableQuantity())
                .price(createProductRequest.price())
                .category(Category.builder()
                        .id(createProductRequest.categoryId())
                        .build())
                .build();
    }

    static CreateProductResponse toProductResponse(Product product) {
        return new CreateProductResponse(
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
