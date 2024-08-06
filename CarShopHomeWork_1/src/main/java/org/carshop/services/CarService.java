package org.carshop.services;

import org.carshop.model.Car;
import org.carshop.repositories.CarRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CarService {
    private CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public void addCar(String make, String model, int year, double price, String condition) {
        Car car = new Car(generateId(), make, model, year, price, condition);
        carRepository.save(car);
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Car getCarById(String id) {
        return carRepository.findById(id);
    }

    public void updateCar(Car car) {
        carRepository.update(car);
    }

    public void deleteCar(String id) {
        carRepository.delete(id);
    }

    private String generateId() {
        return String.valueOf(carRepository.count() + 1);
    }

    public List<Car> getCarsByMake(String make) {
        return carRepository.findAll().stream()
                .filter(car -> car.getMake().equalsIgnoreCase(make))
                .collect(Collectors.toList());
    }

    public List<Car> getCarsByModel(String model) {
        return carRepository.findAll().stream()
                .filter(car -> car.getModel().equalsIgnoreCase(model))
                .collect(Collectors.toList());
    }

    public List<Car> getCarsByYear(int year) {
        return carRepository.findAll().stream()
                .filter(car -> car.getYear() == year)
                .collect(Collectors.toList());
    }

    public List<Car> sortCarsByPrice(boolean ascending) {
        return carRepository.findAll().stream()
                .sorted(ascending
                        ? Comparator.comparingDouble(Car::getPrice)
                        : Comparator.comparingDouble(Car::getPrice).reversed())
                .collect(Collectors.toList());
    }
}
