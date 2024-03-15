package com.anthony.orderservice.service;

import com.anthony.orderservice.dto.OrderItemsDto;
import com.anthony.orderservice.dto.Product;
import com.anthony.orderservice.model.Order;
import com.anthony.orderservice.model.OrderItem;
import com.anthony.orderservice.repository.OrderRepository;
import com.anthony.orderservice.utils.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;
    private final String baseUrl = "http://localhost:8080/";

    public Order createOrder(List<OrderItemsDto> orderRequest) {
        // 1. Check if there are sufficient products to fulfill the order
//        this.baseUrl;
        int id = 0;
        var publisher = webClient.get().uri(baseUrl+"/products")
                .retrieve()
                .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
                        clientResponse -> handleErrorResponse(clientResponse.statusCode()))
                .bodyToFlux(Product.class)
                .onErrorResume(Exception.class, e -> Flux.empty());
        ArrayList<Product> arr = new ArrayList<>();
        publisher.collectList().subscribe(product -> {
            System.out.println("Retrieved product");
            System.out.println(product.toString());

        });
        log.info("Returned array is {}", arr);
        // 2. If true, fulfill the order
        // 3. If false, return a message with the number of items remaining
        Order order = new Order();
        List<OrderItem> orderItems = orderRequest
                .stream().map(this::mapDtoToOrderItem).toList();
        order.setOrderItemList(orderItems);
        log.info("Order {}", order);
        order = orderRepository.save(order);
        return order;

    }

    private Mono<? extends Throwable> handleErrorResponse(HttpStatusCode httpStatusCode) {
        System.out.println("Returning create order");
        return Mono.error(new ResourceNotFoundException("Error retrieving product details: "+ httpStatusCode));
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
