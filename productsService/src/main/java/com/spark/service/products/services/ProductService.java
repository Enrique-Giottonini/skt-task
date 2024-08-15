package com.spark.service.products.services;

import com.spark.entities.domain.Product;
import com.spark.entities.dto.ProductListMessage;
import com.spark.entities.dto.ProductMessage;
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
    private final KafkaTemplate<String, ProductListMessage> kafkaTemplate;


    public void addProduct(Product deserializePlz) {
        productList.add(deserializePlz);
        notifyUpdatedList();
    }

    public void notifyUpdatedList() {
        // TODO: Check this async(?) flow
        ProductListMessage message = new ProductListMessage("list.update", productList);
        ListenableFuture<SendResult<String, ProductListMessage>> future = kafkaTemplate.send("listOfProducts", message);
        future.addCallback(new ListenableFutureCallback<SendResult<String, ProductListMessage>>() {
            @Override
            public void onSuccess(SendResult<String, ProductListMessage> result) {
                System.out.println("Sent message=[" + message +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }
            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Unable to send message=[" + message +
                        "] due to : " + ex.getMessage());
            }
        });
    }
}
