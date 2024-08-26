package com.spark.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
public class KafkaCallback<T> implements ListenableFutureCallback<SendResult<String, T>> {

    private final T message;

    public KafkaCallback(T message) {
        this.message = message;
    }

    @Override
    public void onSuccess(SendResult<String, T> result) {
        log.info("Sent message=[{}] with offset=[{}]", message, result.getRecordMetadata().offset());
    }

    @Override
    public void onFailure(Throwable ex) {
        log.error("Unable to send message=[{}] due to : {}", message, ex.getMessage());
        throw new KafkaException(ex.getMessage());
    }
}