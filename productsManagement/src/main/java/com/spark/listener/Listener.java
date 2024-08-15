package com.spark.listener;

import com.spark.entities.Product;
import com.spark.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@RequiredArgsConstructor
@Component
public class Listener {

    private final ProductService productService;

    @KafkaListener(id = "productsManagement", topics = "listAllProducts", group = "productManagementWebApp")
    public void listen(ConsumerRecord<?, ?> record) {
        System.out.println("listAllProducts: " + record);
        productService.updateList(Arrays.asList(new Product(111, record.toString(), "deserialize", 919.91)));
    }

}
