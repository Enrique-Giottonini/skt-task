package com.spark.service.products;

import com.spark.entities.domain.ProductDTO;
import com.spark.service.products.entities.Product;
import com.spark.service.products.exceptions.ProductValidationException;
import org.hibernate.exception.DataException;
import org.springframework.dao.DataAccessException;
import org.springframework.kafka.KafkaException;

public interface ProductService {
    /**
     * Adds a new product to the database.
     *
     * This method validates the provided {@link ProductDTO} and converts it to a {@link Product} entity before saving it to the database.
     * If the validation fails, a {@link ProductValidationException} is thrown. If there is a database-related error during the save operation,
     * a {@link DataAccessException} or {@link DataException} is thrown.
     *
     * @param product the product dto containing the information of the product to add
     * @throws ProductValidationException if the product data is invalid
     * @throws DataAccessException if there is an issue accessing the database
     * @throws DataException if the product could not be saved to the database
     */
    void addProduct(ProductDTO product);

    /**
     * Notifies subscribers of an update to the product list.
     *
     * This method retrieves the current list of products from the database, converts it to a list of {@link ProductDTO},
     * and sends a message with the type "list.update" to the Kafka topic "listOfProducts". The method logs
     * any exceptions that occur during database access or Kafka message sending.
     *
     * @throws DataAccessException if there is an issue accessing the database
     * @throws KafkaException if there is an error sending the message to Kafka
     */
    void notifyUpdatedList();

    /**
     * Resends the entire product list to subscribers.
     *
     * This method is similar to {@link #notifyUpdatedList()}, but it sends a message with the type "list.resend"
     * to the Kafka topic "listOfProducts". It is used to resend the current state of the product list
     * to subscribers who may have missed previous updates.
     *
     * @throws DataAccessException if there is an issue accessing the database
     * @throws KafkaException if there is an error sending the message to Kafka
     */
    void resendList();
}
