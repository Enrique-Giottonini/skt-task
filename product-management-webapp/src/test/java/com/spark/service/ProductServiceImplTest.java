package com.spark.service;

import com.spark.ProductRepository;
import com.spark.entities.domain.ProductDTO;
import com.spark.entities.domain.ProductListMessage;
import com.spark.entities.domain.ProductMessage;
import com.spark.events.KafkaCallback;
import com.spark.impl.ProductServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.apache.kafka.common.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.concurrent.ListenableFuture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceImplTest {

    @Mock private ProductRepository productRepository;
    @Mock private KafkaTemplate<String, Object> kafkaTemplate;
    @Mock private ListenableFuture<SendResult<String, Object>> future;
    private final String listTopic = "listOfProducts";
    private final String productTopic = "product";

    @InjectMocks
    private ProductServiceImpl productService;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(productService, "listTopic", listTopic);
        ReflectionTestUtils.setField(productService, "productTopic", productTopic);
    }

    @Test
    public void testFindAll_Success() {
        // Arrange
        List<ProductDTO> productList = Collections.singletonList(new ProductDTO());

        when(productRepository.findAll()).thenReturn(productList);

        // Act
        List<ProductDTO> products = productService.findAll();

        // Assert
        assertThat(products).isEqualTo(productList);
    }

    @Test
    public void testUpdateList_Success() {
        // Arrange
        List<ProductDTO> productList = Collections.singletonList(new ProductDTO());

        // Act
        productService.updateList(productList);

        // Assert
        verify(productRepository, times(1)).replaceAll(productList);
    }

    @Test
    public void testSendToProcess_Success() {
        // Arrange
        ProductDTO product = new ProductDTO();
        product.setId(1L);
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(new BigDecimal("9.99"));

        ProductMessage message = new ProductMessage("product.creation", product);
        when(kafkaTemplate.send(productTopic, message)).thenReturn(future);

        // Act
        productService.sendToProcess(product);

        // Assert
        verify(kafkaTemplate, times(1)).send(eq(productTopic), eq(message));
        verify(future, times(1)).addCallback(any(KafkaCallback.class));
    }

    @Test(expected = KafkaException.class)
    public void testSendToProcess_ThrowsKafkaException() {
        // Arrange
        ProductDTO product = new ProductDTO();
        ProductMessage message = new ProductMessage("product.creation", product);
        doThrow(KafkaException.class).when(kafkaTemplate).send("product", message);

        // Act
        productService.sendToProcess(product);
    }


    @Test
    public void testRequestList_Success() {
        // Arrange
        ProductListMessage message = new ProductListMessage("list.subscribe", Collections.emptyList());
        when(kafkaTemplate.send(listTopic, message))
                .thenReturn(future);
        // Act
        productService.requestList();

        // Assert
        verify(kafkaTemplate, times(1)).send(eq(listTopic), eq(message));
        verify(future, times(1)).addCallback(any(KafkaCallback.class));
    }

    @Test(expected = KafkaException.class)
    public void testRequestList_ThrowsKafkaException() {
        // Arrange
        ProductListMessage message = new ProductListMessage("list.subscribe", Collections.emptyList());
        doThrow(mock(KafkaException.class)).when(kafkaTemplate).send(listTopic, message);
        // Act
        productService.requestList();
    }
}
