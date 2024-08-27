package com.spark.service.products.service;

import com.spark.entities.domain.ProductDTO;
import com.spark.entities.domain.ProductListMessage;
import com.spark.service.products.ProductRepository;
import com.spark.service.products.entities.Product;
import com.spark.service.products.events.KafkaCallback;
import com.spark.entities.domain.exceptions.ProductDtoValidationException;
import com.spark.service.products.impl.ProductServiceImpl;
import com.spark.service.products.mapper.ProductMapper;
import org.hibernate.exception.DataException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.DataAccessException;
import org.apache.kafka.common.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.concurrent.ListenableFuture;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceImplTest {

    @Mock private ProductRepository productRepository;
    @Mock private KafkaTemplate<String, ProductListMessage> kafkaTemplate;
    @Mock private ProductMapper productMapper;
    @Mock private ListenableFuture<SendResult<String, ProductListMessage>> future;
    private final String listTopic = "listOfProducts";

    @InjectMocks
    private ProductServiceImpl productService;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(productService, "listTopic", listTopic);
    }

    @Test
    public void testAddProduct_Success() {
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

    @Test(expected = ProductDtoValidationException.class)
    public void testAddProduct_ThrowsProductValidationException() {
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

    @Test
    public void testNotifyUpdatedList_Success() {
        // Arrange
        List<Product> products = Collections.singletonList(new Product());
        List<ProductDTO> productDTOs = Collections.singletonList(new ProductDTO());
        ProductListMessage message = new ProductListMessage("list.update", productDTOs);

        when(productRepository.getAllProducts()).thenReturn(products);
        when(productMapper.toProductDtoList(products)).thenReturn(productDTOs);
        when(kafkaTemplate.send(listTopic, message)).thenReturn(future);

        // Act
        productService.notifyUpdatedList();

        // Assert
        verify(productRepository, times(1)).getAllProducts();
        verify(productMapper, times(1)).toProductDtoList(products);
        verify(kafkaTemplate, times(1)).send(listTopic, message);
        verify(future, times(1)).addCallback(any(KafkaCallback.class));
    }

    @Test(expected = DataAccessException.class)
    public void testNotifyUpdatedList_DataAccessException() {
        // Arrange
        doThrow(mock(DataAccessException.class)).when(productRepository).getAllProducts();

        // Act
        productService.notifyUpdatedList();
    }

    @Test(expected = KafkaException.class)
    public void testNotifyUpdatedList_KafkaException() {
        // Arrange
        List<Product> products = Collections.singletonList(new Product());
        List<ProductDTO> productDTOs = Collections.singletonList(new ProductDTO());
        ProductListMessage message = new ProductListMessage("list.update", productDTOs);

        when(productRepository.getAllProducts()).thenReturn(products);
        when(productMapper.toProductDtoList(products)).thenReturn(productDTOs);
        doThrow(new KafkaException("Kafka error")).when(kafkaTemplate).send(listTopic, message);

        // Act
        productService.notifyUpdatedList();
    }

    @Test
    public void testResendList_Success() {
        // Arrange
        List<Product> products = Collections.singletonList(new Product());
        List<ProductDTO> productDTOs = Collections.singletonList(new ProductDTO());
        ProductListMessage message = new ProductListMessage("list.resend", productDTOs);

        when(productRepository.getAllProducts()).thenReturn(products);
        when(productMapper.toProductDtoList(products)).thenReturn(productDTOs);
        when(kafkaTemplate.send(listTopic, message)).thenReturn(future);

        // Act
        productService.resendList();

        // Assert
        verify(productRepository, times(1)).getAllProducts();
        verify(productMapper, times(1)).toProductDtoList(products);
        verify(kafkaTemplate, times(1)).send(listTopic, message);
        verify(future, times(1)).addCallback(any(KafkaCallback.class));
    }

    @Test(expected = DataAccessException.class)
    public void testResendList_DataAccessException() {
        // Arrange
        doThrow(mock(DataAccessException.class)).when(productRepository).getAllProducts();

        // Act
        productService.resendList();
    }

    @Test(expected = KafkaException.class)
    public void testResendList_KafkaException() {
        // Arrange
        List<Product> products = Collections.singletonList(new Product());
        List<ProductDTO> productDTOs = Collections.singletonList(new ProductDTO());
        ProductListMessage message = new ProductListMessage("list.resend", productDTOs);

        when(productRepository.getAllProducts()).thenReturn(products);
        when(productMapper.toProductDtoList(products)).thenReturn(productDTOs);
        doThrow(new KafkaException("Kafka error")).when(kafkaTemplate).send(listTopic, message);

        // Act
        productService.resendList();
    }
}
