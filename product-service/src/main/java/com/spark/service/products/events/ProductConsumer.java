package com.spark.service.products.events;

import com.spark.entities.domain.ProductMessage;
import com.spark.service.products.ProductService;
import com.spark.service.products.exceptions.ProductValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.DataException;
import org.springframework.dao.DataAccessException;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductConsumer {

    private final ProductService productService;

    @KafkaListener(id = "productsService", topics = "product", containerFactory = "productKafkaListenerContainerFactory")
    public void consume(ProductMessage payload) {
        log.info("Received a new message");
        if (payload != null && "product.creation".equals(payload.getAction()) && payload.getProduct() != null) {
            try {
                productService.addProduct(payload.getProduct());
                productService.notifyUpdatedList();
            } catch (ProductValidationException e) {
                log.error("Payload received and invalid ProductDTO: {}", e.getMessage());
            } catch (DataAccessException | DataException e) {
                log.error("Database error occurred while processing message with action {}: {}", payload.getAction(), e.getMessage(), e);
            } catch (KafkaException e) {
                log.error("Kafka error occurred while processing message with action {}: {}", payload.getAction(), e.getMessage(), e);
            }
        }
    }
}
