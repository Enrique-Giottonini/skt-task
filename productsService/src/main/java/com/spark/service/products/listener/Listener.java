package com.spark.service.products.listener;

import com.spark.entities.domain.Product;
import com.spark.entities.dto.ProductListMessage;
import com.spark.entities.dto.ProductMessage;
import com.spark.service.products.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class Listener {

    private final ProductService productService;

    @KafkaListener(id = "productsService", topics = "product", containerFactory = "kafkaProductMessageListenerContainerFactory")
    public void listenProduct(ProductMessage payload) {
        System.out.println("Received new product");
        if (payload != null && "product.creation".equals(payload.getAction())) {
            productService.addProduct(payload.getProduct());
        }
    }

    @KafkaListener(id = "productListService", topics = "listOfProducts", containerFactory = "kafkaProductListMessageListenerContainerFactory")
    public void listenProductListEvents(ProductListMessage payload) {
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
