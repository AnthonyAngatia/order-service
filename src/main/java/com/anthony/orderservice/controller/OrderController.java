package com.anthony.orderservice.controller;

import com.anthony.orderservice.dto.OrderItemsDto;
import com.anthony.orderservice.dto.ResponseWrapper;
import com.anthony.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private ResponseEntity<?> saveOrder(@RequestBody @Valid List<OrderItemsDto> orderItemsDtoList){
        log.info("OrderItemsDtoList {}", orderItemsDtoList);
        var order = orderService.createOrder(orderItemsDtoList);
        ResponseWrapper responseWrapper = ResponseWrapper.builder()
                .statusCode(HttpStatus.CREATED.value())
                .statusDescription("Order created successfully")
                .data(order)
                .build();
        return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper> getOrders(){
        var orders = orderService.getAllRecords();
        ResponseWrapper responseWrapper = ResponseWrapper.builder()
                .statusCode(HttpStatus.OK.value())
                .statusDescription("Orders retrieved successfully")
                .data(orders)
                .build();
        return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ResponseWrapper> updateOrder(@PathVariable Long productId, @RequestBody List<OrderItemsDto> orderItemsDtoList){
        var order = orderService.updateOrder(productId, orderItemsDtoList);
        ResponseWrapper responseWrapper = ResponseWrapper.builder()
                .statusCode(HttpStatus.OK.value())
                .statusDescription("Order updated successfully")
                .data(order)
                .build();
        return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
    }
}
