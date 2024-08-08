package org.carshop.services;

import org.carshop.model.Car;
import org.carshop.model.Order;
import org.carshop.model.OrderStatus;
import org.carshop.model.User;
import org.carshop.repositories.OrderRepository;

import java.util.Date;
import java.util.List;

/**
 * Сервис для управления заказами.
 * <p>Этот сервис предоставляет методы для создания, получения, обновления и удаления заказов.
 * Также включает функцию проверки наличия автомобиля в заказах.</p>
 */
public class OrderService {
    private OrderRepository orderRepository;

    /**
     * Конструктор класса {@code OrderService}.
     *
     * @param orderRepository репозиторий для управления заказами
     */
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Проверяет, забронирован ли автомобиль.
     *
     * @param car автомобиль, для которого нужно проверить бронирование
     * @return {@code true}, если автомобиль забронирован, {@code false} в противном случае
     */
    public boolean isCarBooked(Car car) {
        List<Order> orders = orderRepository.findAll();
        for (Order order : orders) {
            if (order.getCar().getId().equals(car.getId()) && order.getStatus() == OrderStatus.PENDING) {
                return true;
            }
        }
        return false;
    }

    /**
     * Создает новый заказ.
     *
     * @param client клиент, который делает заказ
     * @param car автомобиль, который заказан
     */
    public void createOrder(User client, Car car) {
        Order order = new Order(generateId(), client, car, new Date(), OrderStatus.PENDING);
        orderRepository.save(order);
    }

    /**
     * Возвращает список всех заказов.
     *
     * @return список всех заказов
     */
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    /**
     * Возвращает заказ по его идентификатору.
     *
     * @param id идентификатор заказа
     * @return заказ с указанным идентификатором, или {@code null}, если заказ не найден
     */
    public Order getOrderById(String id) {
        return orderRepository.findById(id);
    }

    /**
     * Обновляет информацию о заказе.
     *
     * @param order заказ с обновленной информацией
     */
    public void updateOrder(Order order) {
        orderRepository.update(order);
    }

    /**
     * Удаляет заказ по его идентификатору.
     *
     * @param id идентификатор заказа
     */
    public void deleteOrder(String id) {
        orderRepository.delete(id);
    }

    /**
     * Генерирует уникальный идентификатор для нового заказа.
     *
     * @return уникальный идентификатор
     */
    private String generateId() {
        return String.valueOf(orderRepository.count() + 1);
    }
}
