package com.spark.service.products.service;

import com.spark.entities.domain.ProductDTO;
import com.spark.entities.domain.ProductListMessage;
import com.spark.service.products.ProductRepository;
import com.spark.service.products.entities.Product;
import com.spark.service.products.exceptions.ProductValidationException;
import com.spark.service.products.impl.ProductServiceImpl;
import com.spark.service.products.mapper.ProductMapper;
import org.hibernate.exception.DataException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.kafka.core.KafkaTemplate;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.util.Collections;

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

    @Test(expected = ProductValidationException.class)
    public void testAddInvalidProductFromDto() {
        // Arrange
        ProductDTO dto = new ProductDTO();
        dto.setId(0);
        dto.setName("InvalidPrice");
        dto.setDescription("TestDescription");
        dto.setPrice(new BigDecimal(-1)); // Invalid price (negative, violates @Min)

        Mockito.when(productMapper.toProduct(dto)).thenThrow(new ConstraintViolationException(Collections.emptySet()));

        // Act
        productService.addProduct(dto);
    }

    @Test(expected = DataException.class)
    public void testAddProduct_ThrowsDataException() {
        // Arrange
        ProductDTO dto = new ProductDTO();
        dto.setId(1);
        dto.setName("Test Product");
        dto.setDescription("Test Description");
        dto.setPrice(new BigDecimal(99.99));

        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(new BigDecimal(99.99));

        Mockito.when(productMapper.toProduct(dto)).thenReturn(product);

        // Simulate DataException being thrown when inserting the product
        Mockito.doThrow(new DataException("Database error", null)).when(productRepository).insertProduct(product);

        // Act
        productService.addProduct(dto);
        }
}
