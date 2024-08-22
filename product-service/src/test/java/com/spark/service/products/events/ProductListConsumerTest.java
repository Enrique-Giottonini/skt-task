package com.spark.service.products.events;

import com.spark.entities.domain.ProductListMessage;
import com.spark.service.products.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.DataAccessException;
import org.springframework.kafka.KafkaException;

import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class ProductListConsumerTest {

    @Mock private ProductService productService;

    @InjectMocks
    private ProductListConsumer productListConsumer;

    @Test
    public void testConsume_ListSubscribe() {
        // Arrange
        ProductListMessage payload = new ProductListMessage("list.subscribe", null);

        // Act
        productListConsumer.consume(payload);

        // Assert
        verify(productService, times(1)).resendList();
    }

    @Test
    public void testConsume_ListUpdate() {
        // Arrange
        ProductListMessage payload = new ProductListMessage("list.update", null);

        // Act
        productListConsumer.consume(payload);

        // Assert
        verify(productService, never()).resendList();
    }

    @Test
    public void testConsume_ListResend() {
        // Arrange
        ProductListMessage payload = new ProductListMessage("list.resend", null);

        // Act
        productListConsumer.consume(payload);

        // Assert
        verify(productService, never()).resendList();
    }

    @Test
    public void testConsume_NullPayload() {
        // Act
        productListConsumer.consume(null);

        // Assert
        verify(productService, never()).resendList();
    }

    @Test
    public void testConsume_UnknownAction() {
        // Arrange
        ProductListMessage payload = new ProductListMessage("please resend to me", null);

        // Act
        productListConsumer.consume(payload);

        // Assert
        verify(productService, never()).resendList();
    }

    @Test
    public void testConsume_CatchDatabaseException() {
        // Arrange
        ProductListMessage payload = new ProductListMessage("list.subscribe", null);
        doThrow(mock(DataAccessException.class)).when(productService).resendList();

        // Act
        productListConsumer.consume(payload);

        // Assert
        verify(productService, times(1)).resendList();
    }

    @Test
    public void testConsume_CatchKafkaException() {
        // Arrange
        ProductListMessage payload = new ProductListMessage("list.subscribe", null);
        doThrow(mock(KafkaException.class)).when(productService).resendList();

        // Act
        productListConsumer.consume(payload);

        // Assert
        verify(productService, times(1)).resendList();
    }
}
