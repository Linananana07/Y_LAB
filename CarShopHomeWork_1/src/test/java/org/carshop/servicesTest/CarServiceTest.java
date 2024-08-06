package org.carshop.servicesTest;

import org.carshop.model.Car;
import org.carshop.repositories.CarRepository;
import org.carshop.services.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarService carService;

    private Car car1;
    private Car car2;
    private List<Car> carList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        car1 = new Car("1", "Toyota", "Camry", 2022, 30000, "NEW");
        car2 = new Car("2", "Honda", "Civic", 2021, 25000, "USED");
        carList = new ArrayList<>();
        carList.add(car1);
        carList.add(car2);
    }

    @Test
    void testAddCar() {
        carService.addCar("Ford", "Focus", 2023, 22000, "DEMO");
        verify(carRepository, times(1)).save(any(Car.class));
    }

    @Test
    void testGetAllCars() {
        when(carRepository.findAll()).thenReturn(carList);
        List<Car> cars = carService.getAllCars();
        assertEquals(2, cars.size());
        assertTrue(cars.contains(car1));
        assertTrue(cars.contains(car2));
    }

    @Test
    void testGetCarById() {
        when(carRepository.findById("1")).thenReturn(car1);
        Car car = carService.getCarById("1");
        assertNotNull(car);
        assertEquals("Toyota", car.getMake());
    }

    @Test
    void testUpdateCar() {
        Car updatedCar = new Car("1", "Toyota", "Camry", 2022, 29000, "USED");
        doNothing().when(carRepository).update(updatedCar);
        carService.updateCar(updatedCar);
        verify(carRepository, times(1)).update(updatedCar);
    }

    @Test
    void testDeleteCar() {
        doNothing().when(carRepository).delete("1");
        carService.deleteCar("1");
        verify(carRepository, times(1)).delete("1");
    }

    @Test
    void testGetCarsByMake() {
        when(carRepository.findAll()).thenReturn(carList);
        List<Car> toyotaCars = carService.getCarsByMake("Toyota");
        assertEquals(1, toyotaCars.size());
        assertEquals("Toyota", toyotaCars.get(0).getMake());
    }

    @Test
    void testGetCarsByModel() {
        when(carRepository.findAll()).thenReturn(carList);
        List<Car> civicCars = carService.getCarsByModel("Civic");
        assertEquals(1, civicCars.size());
        assertEquals("Civic", civicCars.get(0).getModel());
    }

    @Test
    void testGetCarsByYear() {
        when(carRepository.findAll()).thenReturn(carList);
        List<Car> cars2021 = carService.getCarsByYear(2021);
        assertEquals(1, cars2021.size());
        assertEquals(2021, cars2021.get(0).getYear());
    }

    @Test
    void testSortCarsByPriceAscending() {
        when(carRepository.findAll()).thenReturn(carList);
        List<Car> sortedCars = carService.sortCarsByPrice(true);
        assertEquals(2, sortedCars.size());
        assertEquals("Civic", sortedCars.get(0).getModel()); // Civic should be first because it has a lower price
    }

    @Test
    void testSortCarsByPriceDescending() {
        when(carRepository.findAll()).thenReturn(carList);
        List<Car> sortedCars = carService.sortCarsByPrice(false);
        assertEquals(2, sortedCars.size());
        assertEquals("Camry", sortedCars.get(0).getModel()); // Camry should be first because it has a higher price
    }
}
