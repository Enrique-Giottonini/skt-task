package com.spark.service.products.impl;

import com.spark.entities.domain.ProductDTO;
import com.spark.entities.domain.ProductListMessage;
import com.spark.service.products.ProductRepository;
import com.spark.service.products.ProductService;
import com.spark.service.products.entities.Product;
import com.spark.service.products.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final KafkaTemplate<String, ProductListMessage> kafkaTemplate;

    public void addProduct(ProductDTO dto) {
        productRepository.insertProduct(productMapper.toProduct(dto));
    }

    public void notifyUpdatedList() {
        // TODO: Check this async(?) flow
        List<Product> products = productRepository.getAllProducts();
        List<ProductDTO> productDTOs = productMapper.toProductDtoList(products);
        ProductListMessage message = new ProductListMessage("list.update", productDTOs);
        ListenableFuture<SendResult<String, ProductListMessage>> future = kafkaTemplate.send("listOfProducts", message);
        future.addCallback(new ListenableFutureCallback<SendResult<String, ProductListMessage>>() {
            @Override
            public void onSuccess(SendResult<String, ProductListMessage> result) {
                log.info("Sent message=[{}] with offset=[{}]", message, result.getRecordMetadata().offset());
            }
            @Override
            public void onFailure(Throwable ex) {
                log.error("Unable to send message=[{}] due to : {}", message, ex.getMessage());
            }
        });
    }

    public void resendList() {
        // TODO: Check this async(?) flow
        List<Product> products = productRepository.getAllProducts();
        List<ProductDTO> productDTOs = productMapper.toProductDtoList(products);
        ProductListMessage message = new ProductListMessage("list.resend", productDTOs);
        ListenableFuture<SendResult<String, ProductListMessage>> future = kafkaTemplate.send("listOfProducts", message);
        future.addCallback(new ListenableFutureCallback<SendResult<String, ProductListMessage>>() {
            @Override
            public void onSuccess(SendResult<String, ProductListMessage> result) {
                log.info("Sent message=[{}] with offset=[{}]", message, result.getRecordMetadata().offset());
            }
            @Override
            public void onFailure(Throwable ex) {
                log.error("Unable to send message=[{}] due to : {}", message, ex.getMessage());
            }
        });
    }

}
