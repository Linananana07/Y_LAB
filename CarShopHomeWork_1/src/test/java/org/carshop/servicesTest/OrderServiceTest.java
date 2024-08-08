package org.carshop.servicesTest;

import org.carshop.model.*;
import org.carshop.repositories.OrderRepository;
import org.carshop.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private Car car1;
    private User client1;
    private Order order1;
    private Order order2;
    private List<Order> orderList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        car1 = new Car("1", "Toyota", "Camry", 2022, 30000, "NEW");
        client1 = new User("1", "JohnDoe", "John1", Role.CLIENT);
        order1 = new Order("1", client1, car1, new Date(), OrderStatus.PENDING);
        order2 = new Order("2", client1, car1, new Date(), OrderStatus.COMPLETED);
        orderList = new ArrayList<>();
        orderList.add(order1);
        orderList.add(order2);
    }

    @Test
    void testIsCarBooked() {
        when(orderRepository.findAll()).thenReturn(orderList);
        assertTrue(orderService.isCarBooked(car1));
        assertFalse(orderService.isCarBooked(new Car("3", "Honda", "Civic", 2022, 28000, "USED")));
    }

    @Test
    void testCreateOrder() {
        doNothing().when(orderRepository).save(any(Order.class));
        orderService.createOrder(client1, car1);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testGetAllOrders() {
        when(orderRepository.findAll()).thenReturn(orderList);
        List<Order> orders = orderService.getAllOrders();
        assertEquals(2, orders.size());
        assertTrue(orders.contains(order1));
        assertTrue(orders.contains(order2));
    }

    @Test
    void testGetOrderById() {
        when(orderRepository.findById("1")).thenReturn(order1);
        Order order = orderService.getOrderById("1");
        assertNotNull(order);
        assertEquals("1", order.getId());
    }

    @Test
    void testUpdateOrder() {
        doNothing().when(orderRepository).update(any(Order.class));
        order1.setStatus(OrderStatus.COMPLETED);
        orderService.updateOrder(order1);
        verify(orderRepository, times(1)).update(order1);
    }

    @Test
    void testDeleteOrder() {
        doNothing().when(orderRepository).delete("1");
        orderService.deleteOrder("1");
        verify(orderRepository, times(1)).delete("1");
    }
}
