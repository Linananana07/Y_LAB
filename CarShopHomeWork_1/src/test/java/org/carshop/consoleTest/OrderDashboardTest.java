package org.carshop.consoleTest;

import static org.mockito.Mockito.*;

import org.carshop.console.MainDashboard;
import org.carshop.console.OrderDashboard;
import org.carshop.model.*;
import org.carshop.services.AuditService;
import org.carshop.services.CarService;
import org.carshop.services.OrderService;
import org.carshop.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class OrderDashboardTest {

    @Mock
    private UserService userService;
    @Mock
    private CarService carService;
    @Mock
    private OrderService orderService;
    @Mock
    private AuditService auditService;
    @Mock
    private MainDashboard mainDashboard;

    @InjectMocks
    private OrderDashboard orderDashboard;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUpdateOrderStatus() {
        User admin = new User("1", "admin", "password", Role.ADMIN);
        Date orderDate = new Date();
        Order order = new Order("1", admin,
                new Car("1", "Make", "Model", 2024,
                        12000, "NEW"), orderDate, OrderStatus.PENDING);

        when(orderService.getOrderById("1")).thenReturn(order);
    }

    @Test
    public void testDeleteOrder() {
        User admin = new User("1", "admin", "password", Role.ADMIN);
        Date orderDate = new Date();
        Order order = new Order("1", admin,
                new Car("1", "Make", "Model", 2024,
                        12000, "NEW"), orderDate, OrderStatus.PENDING);

        when(orderService.getOrderById("1")).thenReturn(order);
    }

    @Test
    public void testViewAllOrders() {
        User admin = new User("1", "admin", "password", Role.ADMIN);
        Date orderDate = new Date();
        List<Order> orders = new ArrayList<>();
        orders.add(new Order("1", admin,
                new Car("1", "Make", "Model",
                        2024, 12000, "NEW"), orderDate, OrderStatus.PENDING));

        when(orderService.getAllOrders()).thenReturn(orders);
    }

    @Test
    public void testSortOrders() {
        User admin = new User("1", "admin", "password", Role.ADMIN);
        Date orderDate = new Date();
        List<Order> orders = new ArrayList<>();
        orders.add(new Order("1", admin,
                new Car("1", "Make", "Model", 2024,
                        12000, "NEW"), orderDate, OrderStatus.PENDING));

        when(orderService.getAllOrders()).thenReturn(orders);
    }
}
