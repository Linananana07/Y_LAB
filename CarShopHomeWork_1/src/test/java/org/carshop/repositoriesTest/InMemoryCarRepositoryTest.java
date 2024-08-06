package org.carshop.repositoriesTest;

import org.carshop.model.Car;
import org.carshop.repositories.InMemoryCarRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryCarRepositoryTest {

    private InMemoryCarRepository carRepository;

    @Before
    public void setUp() {
        carRepository = new InMemoryCarRepository();
    }

    @Test
    public void testSave() {
        // Arrange
        Car car = new Car("1", "Toyota", "Corolla", 2020, 20000.0, "New");

        // Act
        carRepository.save(car);

        // Assert
        assertEquals(1, carRepository.count());
        assertEquals(car, carRepository.findById("1"));
    }

    @Test
    public void testFindById() {
        // Arrange
        Car car = new Car("1", "Toyota", "Corolla", 2020, 20000.0, "New");
        carRepository.save(car);

        // Act
        Car result = carRepository.findById("1");

        // Assert
        assertEquals(car, result);
    }

    @Test
    public void testFindAll() {
        // Arrange
        Car car1 = new Car("1", "Toyota", "Corolla", 2020, 20000.0, "New");
        Car car2 = new Car("2", "Honda", "Civic", 2019, 18000.0, "Used");
        carRepository.save(car1);
        carRepository.save(car2);

        // Act
        List<Car> cars = carRepository.findAll();

        // Assert
        assertEquals(2, cars.size());
        assertTrue(cars.contains(car1));
        assertTrue(cars.contains(car2));
    }

    @Test
    public void testUpdate() {
        // Arrange
        Car car = new Car("1", "Toyota", "Corolla", 2020, 20000.0, "New");
        carRepository.save(car);

        Car updatedCar = new Car("1", "Toyota", "Camry", 2021, 22000.0, "Used");

        // Act
        carRepository.update(updatedCar);

        // Assert
        Car result = carRepository.findById("1");
        assertEquals("Camry", result.getModel());
        assertEquals(2021, result.getYear());
        assertEquals(22000.0, result.getPrice());
        assertEquals("Used", result.getCondition());
    }

    @Test
    public void testDelete() {
        // Arrange
        Car car = new Car("1", "Toyota", "Corolla", 2020, 20000.0, "New");
        carRepository.save(car);

        // Act
        carRepository.delete("1");

        // Assert
        assertNull(carRepository.findById("1"));
        assertEquals(0, carRepository.count());
    }

    @Test
    public void testCount() {
        // Arrange
        Car car1 = new Car("1", "Toyota", "Corolla", 2020, 20000.0, "New");
        Car car2 = new Car("2", "Honda", "Civic", 2019, 18000.0, "Used");
        carRepository.save(car1);
        carRepository.save(car2);

        // Act
        long count = carRepository.count();

        // Assert
        assertEquals(2, count);
    }
}
