package com.spark.service.products.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

public class Listener {

    @KafkaListener(id = "productsService", topics = "newProduct", group = "group1")
    public void listen(ConsumerRecord<?, ?> record) {
        System.out.println("newProduct: " + record);
    }

}
