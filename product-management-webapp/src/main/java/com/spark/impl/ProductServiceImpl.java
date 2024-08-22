package com.spark.impl;

import com.spark.ProductRepository;
import com.spark.ProductService;
import com.spark.entities.domain.ProductDTO;
import com.spark.entities.domain.ProductMessage;
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
    private final KafkaTemplate<String, ProductMessage> productMessageKafkaTemplate;

    public List<ProductDTO> findAll() {
        return productRepository.findAll();
    }

    public ProductDTO save(ProductDTO product) {
        // TODO: Check this async(?) flow
        ProductMessage message = new ProductMessage("product.creation", product);
        ListenableFuture<SendResult<String, ProductMessage>> future = productMessageKafkaTemplate.send("product", message);
        future.addCallback(new ListenableFutureCallback<SendResult<String, ProductMessage>>() {
            @Override
            public void onSuccess(SendResult<String, ProductMessage> result) {
                log.info("Sent message=[{}] with offset=[{}]", message, result.getRecordMetadata().offset());
            }
            @Override
            public void onFailure(Throwable ex) {
                log.error("Unable to send message=[{}] due to : {}", message, ex.getMessage());
            }
        });
        return product;
    }

    public void updateList(List<ProductDTO> updatedList) {
        productRepository.replaceAll(updatedList);
    }

}
