package com.spark.service.products.impl;

import com.spark.entities.domain.ProductDTO;
import com.spark.entities.domain.ProductListMessage;
import com.spark.service.products.ProductRepository;
import com.spark.service.products.ProductService;
import com.spark.service.products.entities.Product;
import com.spark.service.products.mapper.ProductMapper;
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

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final KafkaTemplate<String, ProductListMessage> kafkaTemplate;


    public void addProduct(ProductDTO dto) {
        productRepository.save(productMapper.toProduct(dto));
        notifyUpdatedList();
    }

    public void notifyUpdatedList() {
        // TODO: Check this async(?) flow
        List<Product> products = productRepository.findAll();
        List<ProductDTO> productDTOs = productMapper.toProductDtoList(products);
        ProductListMessage message = new ProductListMessage("list.update", productDTOs);
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

    public void resendList() {
        // TODO: Check this async(?) flow
        List<Product> products = productRepository.findAll();
        List<ProductDTO> productDTOs = productMapper.toProductDtoList(products);
        ProductListMessage message = new ProductListMessage("list.resend", productDTOs);
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
