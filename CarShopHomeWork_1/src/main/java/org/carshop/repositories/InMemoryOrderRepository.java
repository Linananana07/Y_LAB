package org.carshop.repositories;

import org.carshop.model.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для хранения заказов в памяти.
 */
public class InMemoryOrderRepository implements OrderRepository {

    private List<Order> orders = new ArrayList<>();

    /**
     * Сохраняет заказ в репозитории.
     *
     * @param order заказ для сохранения
     */
    @Override
    public void save(Order order) {
        orders.add(order);
    }

    /**
     * Находит заказ по его идентификатору.
     *
     * @param id идентификатор заказа
     * @return заказ с указанным идентификатором, или null, если заказ не найден
     */
    @Override
    public Order findById(String id) {
        return orders.stream().filter(order -> order.getId().equals(id)).findFirst().orElse(null);
    }

    /**
     * Возвращает список всех заказов в репозитории.
     *
     * @return список всех заказов
     */
    @Override
    public List<Order> findAll() {
        return new ArrayList<>(orders);
    }

    /**
     * Обновляет информацию о заказе в репозитории.
     *
     * @param order заказ с обновленными данными
     */
    @Override
    public void update(Order order) {
        Optional<Order> existingOrder = orders.stream().filter(o -> o.getId().equals(order.getId())).findFirst();
        existingOrder.ifPresent(value -> {
            value.setStatus(order.getStatus());
        });
    }

    /**
     * Удаляет заказ из репозитория по его идентификатору.
     *
     * @param id идентификатор заказа для удаления
     */
    @Override
    public void delete(String id) {
        orders.removeIf(order -> order.getId().equals(id));
    }

    /**
     * Возвращает количество заказов в репозитории.
     *
     * @return количество заказов
     */
    @Override
    public long count() {
        return orders.size();
    }
}
