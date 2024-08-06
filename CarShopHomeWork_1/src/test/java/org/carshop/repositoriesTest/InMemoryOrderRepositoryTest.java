package org.carshop.repositoriesTest;

import org.carshop.model.*;
import org.carshop.repositories.InMemoryOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryOrderRepositoryTest {

    private InMemoryOrderRepository orderRepository;
    private User testUser;
    private Car testCar;
    private OrderStatus testStatus;
    private Date testDate;

    @BeforeEach
    void setUp() {
        orderRepository = new InMemoryOrderRepository();
        testUser = new User("user1", "TestUser", "testuser1", Role.CLIENT);
        testCar = new Car("1", "TestCar", "ModelCar", 2020, 12000, "NEW");
        testStatus = OrderStatus.PENDING;
        testDate = new Date();
    }

    @Test
    void testSave() {
        Order order = new Order("1", testUser, testCar, testDate, testStatus);
        orderRepository.save(order);

        Order foundOrder = orderRepository.findById("1");
        assertNotNull(foundOrder, "Order should be found");
        assertEquals("1", foundOrder.getId(), "Order ID should match");
        assertEquals(testStatus, foundOrder.getStatus(), "Order status should match");
        assertEquals(testUser, foundOrder.getClient(), "Order client should match");
        assertEquals(testCar, foundOrder.getCar(), "Order car should match");
        assertEquals(testDate, foundOrder.getDate(), "Order date should match");
    }

    @Test
    void testFindById() {
        Order order = new Order("2", testUser, testCar, testDate, OrderStatus.PENDING);
        orderRepository.save(order);

        Order foundOrder = orderRepository.findById("2");
        assertNotNull(foundOrder, "Order should be found");
        assertEquals("2", foundOrder.getId(), "Order ID should match");
        assertEquals(OrderStatus.PENDING, foundOrder.getStatus(), "Order status should match");
    }

    @Test
    void testFindAll() {
        Order order1 = new Order("3", testUser, testCar, testDate, OrderStatus.COMPLETED);
        Order order2 = new Order("4", testUser, testCar, testDate, OrderStatus.PENDING);
        orderRepository.save(order1);
        orderRepository.save(order2);

        assertEquals(2, orderRepository.findAll().size(), "There should be two orders");
    }

    @Test
    void testUpdate() {
        Order order = new Order("5", testUser, testCar, testDate, OrderStatus.PENDING);
        orderRepository.save(order);

        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.update(order);

        Order updatedOrder = orderRepository.findById("5");
        assertNotNull(updatedOrder, "Order should be found");
        assertEquals(OrderStatus.COMPLETED, updatedOrder.getStatus(), "Order status should be updated");
    }

    @Test
    void testDelete() {
        Order order = new Order("6", testUser, testCar, testDate, OrderStatus.CANCELED);
        orderRepository.save(order);

        orderRepository.delete("6");
        Order deletedOrder = orderRepository.findById("6");
        assertNull(deletedOrder, "Order should be deleted");
    }

    @Test
    void testCount() {
        Order order1 = new Order("7", testUser, testCar, testDate, OrderStatus.PENDING);
        Order order2 = new Order("8", testUser, testCar, testDate, OrderStatus.PENDING);
        orderRepository.save(order1);
        orderRepository.save(order2);

        assertEquals(2, orderRepository.count(), "Order count should be two");
    }
}