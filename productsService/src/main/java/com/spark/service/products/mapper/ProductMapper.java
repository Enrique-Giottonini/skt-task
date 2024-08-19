package com.spark.service.products.mapper;

import com.spark.entities.domain.ProductDTO;
import com.spark.service.products.entities.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {
    public Product toProduct(ProductDTO dto) {
        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        return product;
    }

    public ProductDTO toProductDto(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        return dto;
    }

    public List<ProductDTO> toProductDtoList(List<Product> productList) {
        return productList.stream().map(this::toProductDto).collect(Collectors.toList());
    }
}
