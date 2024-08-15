package com.spark.service.products.services;

import com.spark.entities.domain.Product;
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


    public void addProduct(Product deserializePlz) {
        productList.add(deserializePlz);
        notifyUpdatedList();
    }

    public void notifyUpdatedList() {
        // TODO: Check this async(?) flow
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send("listAllProducts", "product.list: " + productList);
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                System.out.println("success");
            }

            @Override
            public void onFailure(Throwable ex) {
                System.out.println("failed");
            }
        });
        System.out.println("Current products in memory: " + productList);
    }
}
