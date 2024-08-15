package com.spark.listener;

import com.spark.entities.dto.ProductListMessage;
import com.spark.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class Listener {

    private final ProductService productService;

    @KafkaListener(id = "productsManagement", topics = "listOfProducts", group = "productManagementWebApp")
    public void listen(ConsumerRecord<String, ProductListMessage> record) {
        ProductListMessage message = record.value();
        System.out.println("Received ProductListMessage: " + message);
        productService.updateList(message.getListOfProducts());
    }

}
