package com.spark.listener;

import com.spark.entities.dto.ProductListMessage;
import com.spark.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class Listener {

    private final ProductService productService;

    @KafkaListener(id = "productsManagement", topics = "listOfProducts", containerFactory = "kafkaListenerContainerFactory")
    public void listenProductListEvents(@Payload ProductListMessage payload) {
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
