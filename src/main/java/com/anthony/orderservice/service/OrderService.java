package com.anthony.orderservice.service;

import com.anthony.orderservice.dto.OrderItemsDto;
import com.anthony.orderservice.model.Order;
import com.anthony.orderservice.model.OrderItem;
import com.anthony.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    public void createOrder(List<OrderItemsDto> orderRequest){
        Order order = new Order();
        List<OrderItem> orderItems = orderRequest
                .stream().map(this::mapDtoToOrderItem).toList();
        order.setOrderItemList(orderItems);
        log.info("Order {}", order);
        order = orderRepository.save(order);
        log.info("Order afrer calling save {}", order);

    }

    public List<Order> getAllRecords(){
        return (List<Order>) orderRepository.findAll();
    }



    private OrderItem mapDtoToOrderItem(OrderItemsDto orderItemsDto) {
        OrderItem orderItem = new OrderItem();
        orderItem.setPrice(orderItemsDto.getPrice());
        orderItem.setName(orderItemsDto.getName());
        orderItem.setDescription(orderItemsDto.getDescription());
        return orderItem;
    }
}
