package com.spark;

import com.spark.entities.domain.ProductListMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
@Slf4j
public class OnStartup implements ApplicationListener<ContextRefreshedEvent> {

    private final ProductRepository productRepository;
    private final KafkaTemplate<String, ProductListMessage> productListMessageKafkaTemplate;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (productRepository.count() == 0) {
            requestList();
        }
    }

    public void requestList() {
        ProductListMessage payload = new ProductListMessage("list.subscribe", new ArrayList<>());
        ListenableFuture<SendResult<String, ProductListMessage>> future = productListMessageKafkaTemplate.send("listOfProducts", payload);
        future.addCallback(new ListenableFutureCallback<SendResult<String, ProductListMessage>>() {
            @Override
            public void onSuccess(SendResult<String, ProductListMessage> result) {
                log.info("Sent message=[{}] with offset=[{}]", payload, result.getRecordMetadata().offset());
            }
            @Override
            public void onFailure(Throwable ex) {
                log.info("Unable to send message=[{}] due to : {}", payload, ex.getMessage());
            }
        });
    }
}
