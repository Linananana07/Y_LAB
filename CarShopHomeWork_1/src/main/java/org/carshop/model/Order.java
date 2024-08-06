package org.carshop.model;

import java.util.Date;

public class Order {
    private String id;
    private User client;
    private Car car;
    private Date date;
    private OrderStatus status;

    public Order(String id, User client, Car car, Date date, OrderStatus status) {
        this.id = id;
        this.client = client;
        this.car = car;
        this.date = date;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
