package com.anthony.orderservice.utils;

import com.anthony.orderservice.dto.ResponseWrapper;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Date;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseWrapper> resourceNotFoundException(){
        ResponseWrapper responseWrapper = ResponseWrapper.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .statusDescription("Resource was not found")
                .build();
        return new ResponseEntity<>(responseWrapper, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseWrapper> requestMethodNotSupported(){
        var response = ResponseWrapper.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .statusDescription("Method not supported")
                .data(new Date())
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseWrapper> resourceNotFound(ResourceNotFoundException exception){
        var response = ResponseWrapper.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .statusDescription(exception.getMessage())
                .data(new Date())
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // A global exception handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseWrapper> globalExceptions(Exception exception){
        System.out.println("In this method");
        var responseWrapper = ResponseWrapper.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .statusDescription(exception.getLocalizedMessage())
                .data(null)
                .build();
        return new ResponseEntity<>(responseWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
