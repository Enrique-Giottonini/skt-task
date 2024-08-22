package com.spark.service.products.service;

import com.spark.entities.domain.ProductDTO;
import com.spark.entities.domain.ProductListMessage;
import com.spark.service.products.ProductRepository;
import com.spark.service.products.entities.Product;
import com.spark.service.products.impl.ProductServiceImpl;
import com.spark.service.products.mapper.ProductMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceImplTest {

    @Mock private ProductRepository productRepository;
    @Mock private KafkaTemplate<String, ProductListMessage> kafkaTemplate;
    @Mock private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    public void testAddValidProduct() {
        // Arrange
        ProductDTO dto = new ProductDTO();
        dto.setId(0);
        dto.setName("TestName");
        dto.setDescription("TestDescription");
        dto.setPrice(new BigDecimal(99.99));

        Product product = new Product();
        product.setId(101L);
        product.setName("TestName");
        product.setDescription("TestDescription");
        product.setPrice(new BigDecimal(99.99));

        Mockito.when(productMapper.toProduct(dto)).thenReturn(product);
        Mockito.when(productRepository.insertProduct(product)).thenReturn(product);

        // Act
        productService.addProduct(dto);

        // Assert
        verify(productRepository, times(1)).insertProduct(product); // Interaction with DB
    }
}
