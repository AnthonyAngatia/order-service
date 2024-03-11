package com.anthony.orderservice.service;

import com.anthony.orderservice.dto.OrderItemsDto;
import com.anthony.orderservice.model.Order;
import com.anthony.orderservice.model.OrderItem;
import com.anthony.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    public Order createOrder(List<OrderItemsDto> orderRequest) {
        Order order = new Order();
        List<OrderItem> orderItems = orderRequest
                .stream().map(this::mapDtoToOrderItem).toList();
        order.setOrderItemList(orderItems);
        log.info("Order {}", order);
        order = orderRepository.save(order);
        return order;

    }

    public List<Order> getAllRecords() {
        return (List<Order>) orderRepository.findAll();
    }


    private OrderItem mapDtoToOrderItem(OrderItemsDto orderItemsDto) {
        OrderItem orderItem = new OrderItem();
        orderItem.setPrice(orderItemsDto.getPrice());
        orderItem.setName(orderItemsDto.getName());
        orderItem.setDescription(orderItemsDto.getDescription());
        return orderItem;
    }

    public Order updateOrder(Long orderId, List<OrderItemsDto> orderItemsDtoList) {
        log.info("Updating order "+ orderId, orderItemsDtoList);
        log.info("Order id " + orderId);
        var order = orderRepository.findById(orderId);
        log.info("Optional object: {}", order);

        if (order.isPresent()) {
            log.info("Order is: {}", order.get().getOrderID());
            List<OrderItem> orderItems = orderItemsDtoList.stream().map(this::mapDtoToOrderItem).toList();
            order.get().setOrderItemList(orderItems);
            log.info("Updating this object: {}", order.get());
            return orderRepository.save(order.get());
        }
        log.info("Order is empty");
        return null;
    }
}
