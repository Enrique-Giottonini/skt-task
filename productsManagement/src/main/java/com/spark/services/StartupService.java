package com.spark.services;

import com.spark.entities.domain.Product;
import com.spark.entities.dto.ProductListMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class StartupService implements ApplicationListener<ContextRefreshedEvent> {

    private final List<Product> productList;
    private final KafkaTemplate<String, ProductListMessage> productListMessageKafkaTemplate;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (productList.size() == 0) {
            requestList();
        }
    }

    public void requestList() {
        ProductListMessage payload = new ProductListMessage("list.subscribe", new ArrayList<>());
        ListenableFuture<SendResult<String, ProductListMessage>> future = productListMessageKafkaTemplate.send("listOfProducts", payload);
        future.addCallback(new ListenableFutureCallback<SendResult<String, ProductListMessage>>() {
            @Override
            public void onSuccess(SendResult<String, ProductListMessage> result) {
                System.out.println("Sent message=[" + payload +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }
            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Unable to send message=[" + payload +
                        "] due to : " + ex.getMessage());
            }
        });
    }
}
