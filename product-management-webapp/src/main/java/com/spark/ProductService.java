package com.spark;

import com.spark.entities.domain.ProductDTO;
import org.springframework.kafka.KafkaException;

import java.util.List;

public interface ProductService {
    /**
     * Retrieves all products from the repository.
     *
     * @return a list of all products
     */
    List<ProductDTO> findAll();

    /**
     * Sends a product to be processed by publishing it to a Kafka topic.
     *
     * @param product the product DTO containing information about the product to be processed.
     * @throws KafkaException if there is an error sending the message to Kafka.
     */
    void sendToProcess(ProductDTO product);

    /**
     * Updates the list of products in the repository with the provided list.
     *
     * @param productList the new list of products to replace the existing list.
     */
    void updateList(List<ProductDTO> productList);
}
