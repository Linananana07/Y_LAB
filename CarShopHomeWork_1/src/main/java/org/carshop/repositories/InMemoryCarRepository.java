package org.carshop.repositories;

import org.carshop.model.Car;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryCarRepository implements CarRepository{

    private List<Car> cars = new ArrayList<>();
    @Override
    public void save(Car car) {
        cars.add(car);
    }

    @Override
    public Car findById(String id) {
        return cars.stream().filter(car -> car.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<Car> findAll() {
        return new ArrayList<>(cars);
    }

    @Override
    public void update(Car car) {
        Optional<Car> existingCar = cars.stream().filter(c -> c.getId().equals(car.getId())).findFirst();
        existingCar.ifPresent(value -> {
            value.setMake(car.getMake());
            value.setModel(car.getModel());
            value.setYear(car.getYear());
            value.setPrice(car.getPrice());
            value.setCondition(car.getCondition());
        });
    }

    @Override
    public void delete(String id) {
        cars.removeIf(car -> car.getId().equals(id));
    }

    @Override
    public long count() {
        return cars.size();
    }
}
