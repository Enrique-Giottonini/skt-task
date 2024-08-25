package com.spark.impl;

import com.spark.ProductRepository;
import com.spark.ProductService;
import com.spark.entities.domain.ProductDTO;
import com.spark.entities.domain.ProductMessage;
import com.spark.entities.domain.exceptions.ProductDtoValidationException;
import com.spark.events.KafkaCallback;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolationException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final KafkaTemplate<String, ProductMessage> productMessageKafkaTemplate;

    public List<ProductDTO> findAll() {
        return productRepository.findAll();
    }

    public void sendToProcess(ProductDTO product) {
        try {
            ProductMessage message = new ProductMessage("product.creation", product);
            ListenableFuture<SendResult<String, ProductMessage>> future = productMessageKafkaTemplate.send("product", message);
            future.addCallback(new KafkaCallback<>(message));
        } catch (KafkaException e) {
            log.error("Error sending message to Kafka: {}", e.getMessage());
            throw e;
        }
    }

    public void updateList(List<ProductDTO> updatedList) {
        productRepository.replaceAll(updatedList);
    }
}
