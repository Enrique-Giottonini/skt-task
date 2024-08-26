package com.spark.impl;

import com.spark.ProductRepository;
import com.spark.ProductService;
import com.spark.entities.domain.ProductDTO;
import com.spark.entities.domain.ProductListMessage;
import com.spark.entities.domain.ProductMessage;
import com.spark.events.KafkaCallback;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final KafkaTemplate<String, ProductMessage> productMessageKafkaTemplate;
    private final KafkaTemplate<String, ProductListMessage> productListMessageKafkaTemplate;

    public List<ProductDTO> findAll() {
        return productRepository.findAll();
    }

    public void sendToProcess(ProductDTO product) {
        try {
            ProductMessage message = new ProductMessage("product.creation", product);
            ListenableFuture<SendResult<String, ProductMessage>> future = productMessageKafkaTemplate.send("product", message);
            future.addCallback(new KafkaCallback<>(message));
            future.get();
        } catch (ExecutionException e) {
            log.error("Error sending message to Kafka: {}", e.getMessage());
            throw new KafkaException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateList(List<ProductDTO> updatedList) {
        productRepository.replaceAll(updatedList);
    }

    public void requestList() {
        try {
            ProductListMessage payload = new ProductListMessage("list.subscribe", Collections.emptyList());
            ListenableFuture<SendResult<String, ProductListMessage>> future = productListMessageKafkaTemplate.send("listOfProducts", payload);
            future.addCallback(new KafkaCallback<>(payload));
        } catch (KafkaException e) {
            log.error("Error sending message to Kafka: {}", e.getMessage());
            throw e;
        }
    }
}
