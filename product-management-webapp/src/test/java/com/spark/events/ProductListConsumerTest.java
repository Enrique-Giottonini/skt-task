package com.spark.events;

import com.spark.ProductService;
import com.spark.entities.domain.ProductDTO;
import com.spark.entities.domain.ProductListMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductListConsumerTest {

    @Mock private ProductService productService;

    @InjectMocks
    private ProductListConsumer productListConsumer;

    @Test
    public void testConsume_ListSubscribe() {
        // Arrange
        ProductListMessage payload = new ProductListMessage("list.subscribe", Collections.emptyList());

        // Act
        productListConsumer.consume(payload);

        // Assert
        verify(productService, never()).updateList(anyList());
    }

    @Test
    public void testConsume_ListUpdate() {
        // Arrange
        List<ProductDTO> products = Collections.singletonList(new ProductDTO());
        ProductListMessage payload = new ProductListMessage("list.update", products);

        // Act
        productListConsumer.consume(payload);

        // Assert
        verify(productService, times(1)).updateList(products);
    }

    @Test
    public void testConsume_ListResend() {
        // Arrange
        List<ProductDTO> products = Collections.singletonList(new ProductDTO());
        ProductListMessage payload = new ProductListMessage("list.resend", products);

        // Act
        productListConsumer.consume(payload);

        // Assert
        verify(productService, times(1)).updateList(products);
    }

    @Test
    public void testConsume_NullPayload() {
        // Act
        productListConsumer.consume(null);

        // Assert
        verify(productService, never()).updateList(anyList());
    }

    @Test
    public void testConsume_UnknownAction() {
        // Arrange
        ProductListMessage payload = new ProductListMessage("unknown.action", Collections.emptyList());

        // Act
        productListConsumer.consume(payload);

        // Assert
        verify(productService, never()).updateList(anyList());
    }

}
