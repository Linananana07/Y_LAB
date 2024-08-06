package org.carshop.repositories;

import org.carshop.model.Car;

import java.util.List;

public interface CarRepository {
    void save(Car car);
    Car findById(String id);
    List<Car> findAll();
    void update(Car car);
    void delete(String id);
    long count();
}
