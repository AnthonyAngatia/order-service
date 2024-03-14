package com.anthony.orderservice.utils;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String msg){
        super(msg);

    }
}
