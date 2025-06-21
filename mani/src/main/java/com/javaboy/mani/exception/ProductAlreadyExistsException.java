package com.javaboy.mani.exception;

/**
 * Custom exception thrown when attempting to add a product that already exists
 */
public class ProductAlreadyExistsException extends RuntimeException {
    public ProductAlreadyExistsException(String message) {
        super(message);
    }

    public ProductAlreadyExistsException(String name, String brand) {
        super("Product with name '" + name + "' and brand '" + brand + "' already exists.");
    }
}
