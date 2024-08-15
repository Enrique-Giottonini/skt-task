package com.spark.services;

import com.spark.entities.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final List<Product> productList;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public List<Product> findAll() {
        return productList;
    }

    public Product save(Product product) {
        // TODO: Check this async(?) flow
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send("product", "product.creation: " + product);
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                System.out.println("success");
                productList.add(product);
            }

            @Override
            public void onFailure(Throwable ex) {
                System.out.println("failed");
            }
        });
        return product;
    }

    public void updateList(List<Product> deserialize) {
        productList.clear();
        productList.addAll(deserialize);
    }
}
