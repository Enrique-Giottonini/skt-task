package com.spark.entities.domain.exceptions;

public class ProductDtoValidationException extends RuntimeException {
    public ProductDtoValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}