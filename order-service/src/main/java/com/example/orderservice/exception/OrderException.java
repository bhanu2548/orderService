package com.example.orderservice.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * Created by bhanu on 16/01/23
 */

public class OrderException extends RuntimeException {
    private HttpStatus httpStatus;
    public OrderException(String message, HttpStatus httpStatus) {
        super(message);

    }
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}
    
    
}
