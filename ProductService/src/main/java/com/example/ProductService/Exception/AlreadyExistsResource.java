package com.example.ProductService.Exception;

public class AlreadyExistsResource extends RuntimeException {

    public AlreadyExistsResource(String message) {
        super(message);
    }

    public AlreadyExistsResource(String message, Throwable cause) {
        super(message, cause);
    }

}
