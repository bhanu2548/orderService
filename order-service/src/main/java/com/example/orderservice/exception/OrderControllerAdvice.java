package com.example.orderservice.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.*;


@ControllerAdvice
public class OrderControllerAdvice {
  
	
	
    @ExceptionHandler(OrderException.class)
    public ResponseEntity handleOrderException(OrderException e){
    	Map<String, String> hashmap  = new HashMap<String, String>();
    	//hashmap.put("message", e.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(hashmap.put("message", e.getMessage()));
    }
}
