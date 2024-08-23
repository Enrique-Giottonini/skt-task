package com.spark.events;

import com.spark.ProductService;
import com.spark.entities.domain.ProductListMessage;
import com.spark.entities.domain.exceptions.ProductDtoValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolationException;


@RequiredArgsConstructor
@Component
@Slf4j
public class ProductListConsumer {

    private final ProductService productService;

    @KafkaListener(id = "productsManagement", topics = "listOfProducts", containerFactory = "productListKafkaListenerContainerFactory")
    public void consume(@Payload ProductListMessage payload) {
        log.info("Received payload in listOfProducts");

        if (payload == null) {
            log.warn("Received null payload");
            return;
        }

        if (payload.getListOfProducts() == null) {
            log.warn("Received a null list as an update");
            return;
        }

        try {
            switch (payload.getAction()) {
                case "list.subscribe":
                    log.info("Received list.subscribe action, no operation performed.");
                    break;
                case "list.update":
                case "list.resend":
                    log.info("Processing action: {}", payload.getAction());
                    productService.updateList(payload.getListOfProducts());
                    break;
                default:
                    log.warn("Received unknown action: {}", payload.getAction());
                    break;
            }
        } catch (ConstraintViolationException e) {
            log.error("Payload received and invalid ProductDTO: {}", e.getMessage());
            throw new ProductDtoValidationException("Invalid product data provided", e);
        }
    }
}
