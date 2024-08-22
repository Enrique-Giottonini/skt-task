package com.spark.events;

import com.spark.ProductService;
import com.spark.entities.domain.ProductListMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class ProductListConsumer {

    private final ProductService productService;

    @KafkaListener(id = "productsManagement", topics = "listOfProducts", containerFactory = "productListKafkaListenerContainerFactory")
    public void consume(@Payload ProductListMessage payload) {
        switch (payload.getAction()) {
            case "list.subscribe":
                break;
            case "list.update":
            case "list.resend":
                productService.updateList(payload.getListOfProducts());
                break;
        }
    }
}
