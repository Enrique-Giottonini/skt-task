package com.spark;

import com.spark.entities.domain.ProductDTO;
import org.apache.kafka.common.KafkaException;

import java.util.List;

public interface ProductService {
    /**
     * Retrieves all products from the repository.
     *
     * @return a list of all products
     */
    List<ProductDTO> findAll();

    /**
     * Sends a product to be processed by publishing it to a Kafka topic, using the action "product.create";
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

    /**
     * Requests the current list of products by sending a subscribe message to a Kafka topic.
     *
     * This method creates a message with the action "list.subscribe" and sends it to the Kafka topic
     * "listOfProducts" to request the current list of products.
     *
     * The success callback logs the successful sending of the message, and the failure callback logs
     * any errors that occur during the sending process.
     *
     * @throws KafkaException if there is an error sending the message to Kafka.
     */
    void requestList();
}
