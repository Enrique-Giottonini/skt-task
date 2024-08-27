package com.spark.service.products.events;

import com.spark.entities.domain.ProductListMessage;
import com.spark.service.products.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.apache.kafka.common.KafkaException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class ProductListConsumer {

    private final ProductService productService;

    @KafkaListener(id = "productListService", topics = "${kafka.topics.list}", containerFactory = "productListKafkaListenerContainerFactory")
    public void consume(ProductListMessage payload) {
        log.info("Received payload in listOfProducts");

        if (payload == null) {
            log.warn("Received null payload");
            return;
        }

        try {
            switch (payload.getAction()) {
                case "list.subscribe":
                    log.info("Resending list");
                    productService.resendList();
                    break;
                case "list.update":
                    log.info("Received list.update action, no operation performed.");
                    break;
                case "list.resend":
                    log.info("Received list.resend action, no operation performed.");
                    break;
                default:
                    log.warn("Received unknown action: {}", payload.getAction());
                    break;
            }
        } catch (DataAccessException e) {
            log.error("Database error occurred while processing message with action {}: {}", payload.getAction(), e.getMessage(), e);
        } catch (KafkaException e) {
            log.error("Kafka error occurred while processing message with action {}: {}", payload.getAction(), e.getMessage(), e);
        }
    }
}
