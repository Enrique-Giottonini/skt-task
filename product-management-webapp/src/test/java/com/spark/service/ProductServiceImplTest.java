package com.spark.service;

import com.spark.ProductRepository;
import com.spark.entities.domain.ProductDTO;
import com.spark.entities.domain.ProductMessage;
import com.spark.impl.ProductServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceImplTest {

    @Mock private ProductRepository productRepository;
    @Mock private KafkaTemplate<String, ProductMessage> kafkaTemplate;
    @Mock private ListenableFuture<SendResult<String, ProductMessage>> future;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    public void testUpdateList_Success() {
        // Arrange
        List<ProductDTO> productList = Collections.singletonList(new ProductDTO());

        // Act
        productService.updateList(productList);

        // Assert
        verify(productRepository, times(1)).replaceAll(productList);
    }

}
