package org.carshop.repositoriesTest;

import org.carshop.model.*;
import org.carshop.repositories.InMemoryOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryOrderRepositoryTest {

    private InMemoryOrderRepository repository;
    private User user;
    private Car car;

    @BeforeEach
    public void setUp() {
        repository = new InMemoryOrderRepository();

        // Подготовка тестовых данных
        user = new User("1", "john_doe", "password123", Role.CLIENT);
        car = new Car("1", "Toyota", "Corolla", 2020, 20000.00, "NEW");
    }

    @Test
    public void testSaveOrder() {
        Order order = new Order("1", user, car, new Date(), OrderStatus.PENDING);
        repository.save(order);

        Order foundOrder = repository.findById("1");
        assertNotNull(foundOrder, "Order should be found after saving");
        assertEquals("1", foundOrder.getId(), "Order ID should match");
    }

    @Test
    public void testFindAllOrders() {
        Order order1 = new Order("2", user, car, new Date(), OrderStatus.COMPLETED);
        Order order2 = new Order("3", user, car, new Date(), OrderStatus.CANCELED);
        repository.save(order1);
        repository.save(order2);

        List<Order> allOrders = repository.findAll();
        assertEquals(2, allOrders.size(), "There should be 2 orders in the repository");
    }

    @Test
    public void testUpdateOrder() {
        Order order = new Order("4", user, car, new Date(), OrderStatus.PENDING);
        repository.save(order);

        Order updatedOrder = new Order("4", user, car, new Date(), OrderStatus.COMPLETED);
        repository.update(updatedOrder);

        Order foundOrder = repository.findById("4");
        assertNotNull(foundOrder, "Order should be found after update");
        assertEquals(OrderStatus.COMPLETED, foundOrder.getStatus(), "Order status should be updated");
    }

    @Test
    public void testDeleteOrder() {
        Order order = new Order("5", user, car, new Date(), OrderStatus.PENDING);
        repository.save(order);
        repository.delete("5");

        Order foundOrder = repository.findById("5");
        assertNull(foundOrder, "Order should be null after deletion");
    }

    @Test
    public void testCount() {
        Order order1 = new Order("6", user, car, new Date(), OrderStatus.PENDING);
        Order order2 = new Order("7", user, car, new Date(), OrderStatus.COMPLETED);
        repository.save(order1);
        repository.save(order2);

        long count = repository.count();
        assertEquals(2, count, "There should be 2 orders in the repository");
    }
}