package com.spark.service.products.impl;

import com.spark.entities.domain.ProductDTO;
import com.spark.entities.domain.ProductListMessage;
import com.spark.service.products.ProductRepository;
import com.spark.service.products.ProductService;
import com.spark.service.products.entities.Product;
import com.spark.service.products.events.KafkaCallback;
import com.spark.entities.domain.exceptions.ProductDtoValidationException;
import com.spark.service.products.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.apache.kafka.common.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolationException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final KafkaTemplate<String, ProductListMessage> kafkaTemplate;

    @Value("${kafka.topics.list}")
    private String listTopic;

    public void addProduct(ProductDTO dto) {
        try {
            productRepository.insertProduct(productMapper.toProduct(dto));
        } catch (ConstraintViolationException e) {
            log.error("Invalid ProductDTO: {}", e.getMessage());
            throw new ProductDtoValidationException("Invalid product data provided", e);
        } catch (DataAccessException | DataException e) {
            log.error("Product could not be saved in database: {}", e.getMessage());
            throw e;
        }
    }

    public void notifyUpdatedList() {
        sendProductListMessage("list.update");
    }

    public void resendList() {
        sendProductListMessage("list.resend");
    }

    private void sendProductListMessage(String messageType) {
        try {
            List<Product> products = productRepository.getAllProducts();
            List<ProductDTO> productDTOs = productMapper.toProductDtoList(products);
            ProductListMessage message = new ProductListMessage(messageType, productDTOs);
            ListenableFuture<SendResult<String, ProductListMessage>> future = kafkaTemplate.send(listTopic, message);
            future.addCallback(new KafkaCallback<>(message)); // For logging
        } catch (DataAccessException e) {
            log.error("Error accessing data from the repository: {}", e.getMessage());
            throw e;
        } catch (KafkaException e) {
            log.error("Error sending message to Kafka: {}", e.getMessage());
            throw e;
        }
    }
}
