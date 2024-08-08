package org.carshop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Представляет заказ на покупку автомобиля.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private String id;
    private User client;
    private Car car;
    private Date date;
    private OrderStatus status;
}
