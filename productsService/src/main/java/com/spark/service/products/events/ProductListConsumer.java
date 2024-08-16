package com.spark.service.products.events;

import com.spark.entities.domain.ProductListMessage;
import com.spark.service.products.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProductListConsumer {

    private final ProductService productService;

    @KafkaListener(id = "productListService", topics = "listOfProducts", containerFactory = "productListKafkaListenerContainerFactory")
    public void consume(ProductListMessage payload) {
        switch (payload.getAction()) {
            case "list.subscribe":
                productService.resendList();
                break;
            case "list.update":
            case "list.resend":
                break;
        }
    }
}
