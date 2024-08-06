package org.carshop.services;

import org.carshop.model.Car;
import org.carshop.model.Order;
import org.carshop.model.OrderStatus;
import org.carshop.model.User;
import org.carshop.repositories.OrderRepository;

import java.util.Date;
import java.util.List;

public class OrderService {
    private OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public boolean isCarBooked(Car car) {
        List<Order> orders = orderRepository.findAll();
        for (Order order : orders) {
            if (order.getCar().getId().equals(car.getId()) && order.getStatus() == OrderStatus.PENDING) {
                return true;
            }
        }
        return false;
    }

    public void createOrder(User client, Car car) {
        Order order = new Order(generateId(), client, car, new Date(), OrderStatus.PENDING);
        orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(String id) {
        return orderRepository.findById(id);
    }

    public void updateOrder(Order order) {
        orderRepository.update(order);
    }

    public void deleteOrder(String id) {
        orderRepository.delete(id);
    }

    private String generateId() {
        return String.valueOf(orderRepository.count() + 1);
    }
}
