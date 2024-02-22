package com.anthony.orderservice.controller;

import com.anthony.orderservice.dto.OrderItemsDto;
import com.anthony.orderservice.model.Order;
import com.anthony.orderservice.repository.OrderRepository;
import com.anthony.orderservice.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderController {

    private final OrderRepository orderRepository;
    private final OrderService orderService;

    public OrderController(OrderRepository orderRepository, OrderService orderService) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private ResponseEntity<?> saveOrder(@RequestBody List<OrderItemsDto> orderItemsDtoList){
        log.info("OrderItemsDtoList {}", orderItemsDtoList);
        orderService.createOrder(orderItemsDtoList);
        return new ResponseEntity<>("Order created successfully", HttpStatus.OK);
    }

    @GetMapping
    public List<Order> getOrders(){
        return orderService.getAllRecords();
    }
}
