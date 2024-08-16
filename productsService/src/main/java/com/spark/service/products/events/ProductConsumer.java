package com.spark.service.products.events;

import com.spark.entities.domain.ProductMessage;
import com.spark.service.products.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProductConsumer {

    private final ProductService productService;

    @KafkaListener(id = "productsService", topics = "product", containerFactory = "productKafkaListenerContainerFactory")
    public void consume(ProductMessage payload) {
        System.out.println("Received new product");
        if (payload != null && "product.creation".equals(payload.getAction())) {
            productService.addProduct(payload.getProduct());
        }
    }
}
