package org.carshop.repositories;

import org.carshop.model.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryOrderRepository implements OrderRepository{

    private List<Order> orders = new ArrayList<>();
    @Override
    public void save(Order order) {
        orders.add(order);
    }

    @Override
    public Order findById(String id) {
        return orders.stream().filter(order -> order.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(orders);
    }

    @Override
    public void update(Order order) {
        Optional<Order> existingOrder = orders.stream().filter(o -> o.getId().equals(order.getId())).findFirst();
        existingOrder.ifPresent(value -> {
            value.setStatus(order.getStatus());
        });
    }

    @Override
    public void delete(String id) {
        orders.removeIf(order -> order.getId().equals(id));
    }

    @Override
    public long count() {
        return orders.size();
    }
}
