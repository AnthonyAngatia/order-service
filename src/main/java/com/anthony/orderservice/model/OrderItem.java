package com.anthony.orderservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    Long orderItemID;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    @JsonIgnore
    Order order;

    String name;
    String description;
    BigDecimal price;
}
