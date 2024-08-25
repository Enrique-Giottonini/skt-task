package com.spark.repository;

import com.spark.ProductRepository;
import com.spark.entities.domain.ProductDTO;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductRepositoryTest {

    private ProductRepository productRepository;

    @Before
    public void setUp() {
        productRepository = new ProductRepository();
    }

    @Test
    public void testFindAll_InitiallyEmpty() {
        // Act
        List<ProductDTO> products = productRepository.findAll();

        // Assert
        assertThat(products).isEmpty();
    }

    @Test
    public void testCount_InitiallyZero() {
        // Act
        int count = productRepository.count();

        // Assert
        assertThat(count).isEqualTo(0);
    }

    @Test
    public void testReplaceAll_UpdatesList() {
        // Arrange
        List<ProductDTO> newList = new ArrayList<>();
        ProductDTO product1 = new ProductDTO();
        product1.setId(1L);
        product1.setName("Product 1");
        product1.setDescription("Description 1");
        product1.setPrice(new BigDecimal("9.99"));

        ProductDTO product2 = new ProductDTO();
        product2.setId(2L);
        product2.setName("Product 2");
        product2.setDescription("Description 2");
        product2.setPrice(new BigDecimal("19.99"));

        newList.add(product1);
        newList.add(product2);

        // Act
        productRepository.replaceAll(newList);

        // Assert
        List<ProductDTO> products = productRepository.findAll();
        assertThat(products)
                .hasSize(2)
                .containsExactlyInAnyOrder(product1, product2);

        int count = productRepository.count();
        assertThat(count).isEqualTo(2);
    }

    @Test
    public void testReplaceAll_ClearsList() {
        // Arrange
        List<ProductDTO> initialList = new ArrayList<>();
        ProductDTO product = new ProductDTO();
        product.setId(1L);
        product.setName("Product 1");
        product.setDescription("Description 1");
        product.setPrice(new BigDecimal("9.99"));
        initialList.add(product);

        productRepository.replaceAll(initialList);

        // Act
        productRepository.replaceAll(new ArrayList<>());

        // Assert
        List<ProductDTO> products = productRepository.findAll();
        assertThat(products).isEmpty();

        int count = productRepository.count();
        assertThat(count).isEqualTo(0);
    }
}