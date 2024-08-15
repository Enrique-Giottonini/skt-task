package com.spark.service.products.listener;

import com.spark.entities.domain.Product;
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

    @KafkaListener(id = "productsService", topics = "product", group = "productDb")
    public void listen(ConsumerRecord<String, ProductMessage> record) {
        ProductMessage message = record.value();
        System.out.println("Received ProductMessage: " + message);
        if (message != null && "product.creation".equals(message.getAction())) {
            productService.addProduct(message.getProduct());
        }
    }
}
