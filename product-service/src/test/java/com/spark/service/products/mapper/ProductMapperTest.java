package com.spark.service.products.mapper;

import com.spark.entities.domain.ProductDTO;
import com.spark.service.products.entities.Product;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductMapperTest {

    private final ProductMapper productMapper = new ProductMapper();

    @Test
    public void testToProduct_Success() {
        // Arrange
        ProductDTO dto = new ProductDTO();
        dto.setId(1L);
        dto.setName("Test Product");
        dto.setDescription("Test Description");
        dto.setPrice(new BigDecimal("9.99"));

        // Act
        Product product = productMapper.toProduct(dto);

        // Assert
        assertThat(product).isEqualToComparingFieldByFieldRecursively(dto);
    }

    @Test
    public void testToProductDto_Success() {
        // Arrange
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(new BigDecimal("9.99"));

        // Act
        ProductDTO dto = productMapper.toProductDto(product);

        // Assert
        assertThat(dto).isEqualToComparingFieldByFieldRecursively(product);
    }

    @Test
    public void testToProductDtoList_Success() {
        // Arrange
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product 1");
        product1.setDescription("Description 1");
        product1.setPrice(new BigDecimal("9.99"));

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");
        product2.setDescription("Description 2");
        product2.setPrice(new BigDecimal("19.99"));

        List<Product> productList = Arrays.asList(product1, product2);

        // Act
        List<ProductDTO> dtoList = productMapper.toProductDtoList(productList);

        // Assert
        assertThat(dtoList).hasSize(2);
        assertThat(dtoList.get(0)).isEqualToComparingFieldByFieldRecursively(product1);
        assertThat(dtoList.get(1)).isEqualToComparingFieldByFieldRecursively(product2);
    }
}
