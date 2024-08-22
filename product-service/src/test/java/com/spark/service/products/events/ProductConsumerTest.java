package com.spark.service.products.events;

import com.spark.entities.domain.ProductDTO;
import com.spark.entities.domain.ProductMessage;
import com.spark.service.products.ProductService;
import com.spark.service.products.exceptions.ProductValidationException;
import org.springframework.kafka.KafkaException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.DataAccessException;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class ProductConsumerTest {

    @Mock private ProductService productService;

    @InjectMocks
    private ProductConsumer productConsumer;

    @Test
    public void testConsume_ProductCreation() {
        // Arrange
        ProductMessage payload = new ProductMessage("product.creation", new ProductDTO());

        // Act
        productConsumer.consume(payload);

        // Assert
        verify(productService, times(1)).addProduct(payload.getProduct());
        verify(productService, times(1)).notifyUpdatedList();
    }

    @Test
    public void testConsume_NullPayload() {
        // Act
        productConsumer.consume(null);

        // Assert
        verify(productService, never()).addProduct(null);
        verify(productService, never()).notifyUpdatedList();
    }

    @Test
    public void testConsume_UnknownProductAction() {
        // Arrange
        ProductMessage payload = new ProductMessage("add this product please", null);

        // Act
        productConsumer.consume(payload);

        // Assert
        verify(productService, never()).addProduct(null);
        verify(productService, never()).notifyUpdatedList();
    }

    @Test
    public void testConsume_NullProductInPayload() {
        // Arrange
        ProductMessage payload = new ProductMessage("product.creation", null);

        // Act
        productConsumer.consume(payload);

        // Assert
        verify(productService, never()).addProduct(null);
        verify(productService, never()).notifyUpdatedList();
    }

    @Test
    public void testConsume_InvalidProductInPayload() {
        // Arrange
        ProductDTO embeddedProduct = new ProductDTO();
        embeddedProduct.setName("Invalid Product");
        embeddedProduct.setPrice(new BigDecimal(-1)); // Can't be negative
        ProductMessage payload = new ProductMessage("product.creation", embeddedProduct);

        doThrow(new ProductValidationException("Invalid product data provided", null))
                .when(productService)
                .addProduct(payload.getProduct());

        // Act
        productConsumer.consume(payload);

        // Assert
        verify(productService, times(1)).addProduct(payload.getProduct());
        verify(productService, never()).notifyUpdatedList();
    }

    @Test
    public void testConsume_CatchDatabaseException() {
        // Arrange
        ProductMessage payload = new ProductMessage("product.creation", new ProductDTO());

        doThrow(mock(DataAccessException.class)).when(productService).addProduct(payload.getProduct());

        // Act
        productConsumer.consume(payload);

        // Assert
        verify(productService, times(1)).addProduct(payload.getProduct());
        verify(productService, never()).notifyUpdatedList();
    }

    @Test
    public void testConsume_CatchKafkaException() {
        // Arrange
        ProductMessage payload = new ProductMessage("product.creation", new ProductDTO());

        doThrow(mock(KafkaException.class)).when(productService).notifyUpdatedList();

        // Act
        productConsumer.consume(payload);

        // Assert
        verify(productService, times(1)).addProduct(payload.getProduct());
        verify(productService, times(1)).notifyUpdatedList();
    }
}
