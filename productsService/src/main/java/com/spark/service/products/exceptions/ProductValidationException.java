package com.spark.service.products.exceptions;

public class ProductValidationException extends RuntimeException {
    public ProductValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}