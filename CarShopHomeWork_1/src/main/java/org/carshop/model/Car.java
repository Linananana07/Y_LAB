package org.carshop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Представляет автомобиль в системе.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Car {
    private String id;
    private String make;
    private String model;
    private int year;
    private double price;
    private String condition;
}
