package com.spark.impl;

import com.spark.ProductService;
import com.spark.entities.domain.Product;
import com.spark.entities.domain.ProductMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final List<Product> productList;
    private final KafkaTemplate<String, ProductMessage> productMessageKafkaTemplate;

    public List<Product> findAll() {
        return productList;
    }

    public Product save(Product product) {
        // TODO: Check this async(?) flow
        ProductMessage message = new ProductMessage("product.creation", product);
        ListenableFuture<SendResult<String, ProductMessage>> future = productMessageKafkaTemplate.send("product", message);
        future.addCallback(new ListenableFutureCallback<SendResult<String, ProductMessage>>() {
            @Override
            public void onSuccess(SendResult<String, ProductMessage> result) {
                System.out.println("Sent message=[" + message +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }
            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Unable to send message=[" + message +
                        "] due to : " + ex.getMessage());
            }
        });
        return product;
    }

    public void updateList(List<Product> deserialize) {
        productList.clear();
        productList.addAll(deserialize);
    }

}
