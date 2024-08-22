package com.spark.service.products.events;

import com.spark.entities.domain.ProductMessage;
import com.spark.service.products.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductConsumer {

    private final ProductService productService;

    @KafkaListener(id = "productsService", topics = "product", containerFactory = "productKafkaListenerContainerFactory")
    public void consume(ProductMessage payload) {
        log.info("Received new message");
        if (payload != null && "product.creation".equals(payload.getAction())) {
            productService.addProduct(payload.getProduct());
            productService.notifyUpdatedList();
        }
    }
}
