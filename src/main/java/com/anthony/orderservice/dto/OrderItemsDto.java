package com.anthony.orderservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemsDto {
    @NotBlank(message = "Name must not be Empty")
    String name;
    @NotBlank(message = "Description must not be Empty")
    String description;
    BigDecimal price;
}
