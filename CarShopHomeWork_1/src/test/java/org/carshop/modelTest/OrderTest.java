package org.carshop.modelTest;

import org.carshop.model.*;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderTest {

    @Test
    public void testOrderConstructorAndGetters() {
        User client = new User("1", "username", "password", Role.CLIENT);
        Car car = new Car("1", "Make", "Model", 2022, 20000.0, "New");
        Date date = new Date();
        OrderStatus status = OrderStatus.PENDING;

        Order order = new Order("1", client, car, date, status);

        assertEquals("1", order.getId());
        assertEquals(client, order.getClient());
        assertEquals(car, order.getCar());
        assertEquals(date, order.getDate());
        assertEquals(status, order.getStatus());
    }

    @Test
    public void testSetters() {
        User client = new User("1", "username", "password", Role.CLIENT);
        Car car = new Car("1", "Make", "Model", 2022, 20000.0, "New");
        Date date = new Date();
        OrderStatus status = OrderStatus.PENDING;

        Order order = new Order("1", client, car, date, status);

        User newClient = new User("2", "newUsername", "newPassword", Role.MANAGER);
        Car newCar = new Car("2", "NewMake", "NewModel", 2023, 30000.0, "Used");
        Date newDate = new Date(date.getTime() + 1000000); // just to ensure it's a different date
        OrderStatus newStatus = OrderStatus.COMPLETED;

        order.setId("2");
        order.setClient(newClient);
        order.setCar(newCar);
        order.setDate(newDate);
        order.setStatus(newStatus);

        assertEquals("2", order.getId());
        assertEquals(newClient, order.getClient());
        assertEquals(newCar, order.getCar());
        assertEquals(newDate, order.getDate());
        assertEquals(newStatus, order.getStatus());
    }
}
