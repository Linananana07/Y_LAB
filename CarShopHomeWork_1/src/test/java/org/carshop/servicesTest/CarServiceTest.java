package org.carshop.servicesTest;

import org.carshop.model.Car;
import org.carshop.repositories.CarRepository;
import org.carshop.services.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CarServiceTest {

    private CarRepository carRepository;
    private CarService carService;

    @BeforeEach
    public void setUp() {
        carRepository = mock(CarRepository.class);
        carService = new CarService(carRepository);
    }

    @Test
    public void testAddCar() {
        carService.addCar("Toyota", "Corolla", 2020, 20000, "New");

        verify(carRepository).save(any(Car.class));
    }

    @Test
    public void testGetAllCars() {
        Car car1 = new Car("1", "Toyota", "Corolla", 2020, 20000, "New");
        Car car2 = new Car("2", "Honda", "Civic", 2019, 18000, "Used");
        List<Car> cars = Arrays.asList(car1, car2);

        when(carRepository.findAll()).thenReturn(cars);

        List<Car> result = carService.getAllCars();

        assertEquals(cars, result);
    }

    @Test
    public void testGetCarById() {
        String carId = "1";
        Car car = new Car(carId, "Toyota", "Corolla", 2020, 20000, "New");

        when(carRepository.findById(carId)).thenReturn(car);

        Car result = carService.getCarById(carId);

        assertEquals(car, result);
    }

    @Test
    public void testUpdateCar() {
        Car car = new Car("1", "Toyota", "Corolla", 2020, 20000, "New");

        carService.updateCar(car);

        verify(carRepository).update(car);
    }

    @Test
    public void testDeleteCar() {
        String carId = "1";

        carService.deleteCar(carId);

        verify(carRepository).delete(carId);
    }

    @Test
    public void testGetCarsByMake() {
        Car car1 = new Car("1", "Toyota", "Corolla", 2020, 20000, "New");
        Car car2 = new Car("2", "Toyota", "Camry", 2019, 22000, "Used");
        List<Car> cars = Arrays.asList(car1, car2);

        when(carRepository.findAll()).thenReturn(cars);

        List<Car> result = carService.getCarsByMake("Toyota");

        assertEquals(cars, result);
    }

    @Test
    public void testGetCarsByModel() {
        Car car1 = new Car("1", "Toyota", "Corolla", 2020, 20000, "New");
        Car car2 = new Car("2", "Honda", "Civic", 2019, 18000, "Used");
        List<Car> cars = Collections.singletonList(car1);

        when(carRepository.findAll()).thenReturn(cars);

        List<Car> result = carService.getCarsByModel("Corolla");

        assertEquals(cars, result);
    }

    @Test
    public void testGetCarsByYear() {
        Car car1 = new Car("1", "Toyota", "Corolla", 2020, 20000, "New");
        List<Car> cars = Collections.singletonList(car1);

        when(carRepository.findAll()).thenReturn(cars);

        List<Car> result = carService.getCarsByYear(2020);

        assertEquals(cars, result);
    }

    @Test
    public void testSortCarsByPriceAscending() {
        Car car1 = new Car("1", "Toyota", "Corolla", 2020, 20000, "New");
        Car car2 = new Car("2", "Honda", "Civic", 2019, 18000, "Used");
        List<Car> cars = Arrays.asList(car2, car1);

        when(carRepository.findAll()).thenReturn(Arrays.asList(car1, car2));

        List<Car> result = carService.sortCarsByPrice(true);

        assertEquals(cars, result);
    }

    @Test
    public void testSortCarsByPriceDescending() {
        Car car1 = new Car("1", "Toyota", "Corolla", 2020, 20000, "New");
        Car car2 = new Car("2", "Honda", "Civic", 2019, 18000, "Used");
        List<Car> cars = Arrays.asList(car1, car2);

        when(carRepository.findAll()).thenReturn(Arrays.asList(car1, car2));

        List<Car> result = carService.sortCarsByPrice(false);

        assertEquals(cars, result);
    }

    @Test
    public void testUpdateCarIndices() {
        Car car1 = new Car("1", "Toyota", "Corolla", 2020, 20000, "New");
        Car car2 = new Car("2", "Honda", "Civic", 2019, 18000, "Used");
        List<Car> cars = Arrays.asList(car1, car2);

        when(carRepository.findAll()).thenReturn(cars);

        carService.updateCarIndices();

        verify(carRepository).update(car1);
        verify(carRepository).update(car2);
    }
}
