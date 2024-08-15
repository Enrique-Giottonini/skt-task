package com.spark.service.products.listener;

import com.spark.entities.Product;
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
    public void listen(ConsumerRecord<?, ?> record) {
        System.out.println("newProduct: " + record);
        productService.addProduct(new Product(999, "temp product " + LocalDateTime.now(), "deserialize plz", 999.99));
    }

}
