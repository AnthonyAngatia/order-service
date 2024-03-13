package com.anthony.orderservice.controller;

import com.anthony.orderservice.dto.OrderItemsDto;
import com.anthony.orderservice.dto.ResponseWrapper;
import com.anthony.orderservice.model.Order;
import com.anthony.orderservice.model.OrderItem;
import com.anthony.orderservice.repository.OrderRepository;
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
    private final OrderRepository orderRepository;

    public OrderController(OrderService orderService, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
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
    public ResponseEntity<ResponseWrapper> updateOrder(@PathVariable Long productId, @RequestBody List<OrderItem> orderItems){
        Order existingOrder = orderRepository.findById(productId)
                .orElseThrow(()-> new RuntimeException("Resource with id: "+ productId + " was not found"));
        existingOrder.setOrderItemList(orderItems);
        orderRepository.save(existingOrder);
        ResponseWrapper responseWrapper = ResponseWrapper.builder()
                .statusCode(HttpStatus.OK.value())
                .statusDescription("Order updated Successfully")
                .data(existingOrder)
                .build();
        return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<ResponseWrapper> deleteOrder(@PathVariable Long orderId){
        Order existingOrder = orderRepository.findById(orderId)
                        .orElseThrow(()-> new RuntimeException("Resource not found"));
        orderRepository.deleteById(orderId);
        ResponseWrapper responseWrapper = ResponseWrapper.builder()
                .statusCode(HttpStatus.OK.value())
                .statusDescription("Order updated Successfully")
                .build();
        return new ResponseEntity<>(responseWrapper, HttpStatus.NO_CONTENT);
    }
}
