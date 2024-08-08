package org.carshop.repositories;

import org.carshop.model.Order;

import java.util.List;

/**
 * Интерфейс для работы с репозиторием заказов.
 */
public interface OrderRepository {

    /**
     * Сохраняет заказ в репозитории.
     *
     * @param order заказ для сохранения
     */
    void save(Order order);

    /**
     * Находит заказ по его идентификатору.
     *
     * @param id идентификатор заказа
     * @return заказ с указанным идентификатором, или null, если заказ не найден
     */
    Order findById(String id);

    /**
     * Возвращает список всех заказов в репозитории.
     *
     * @return список всех заказов
     */
    List<Order> findAll();

    /**
     * Обновляет информацию о заказе в репозитории.
     *
     * @param order заказ с обновленными данными
     */
    void update(Order order);

    /**
     * Удаляет заказ из репозитория по его идентификатору.
     *
     * @param id идентификатор заказа для удаления
     */
    void delete(String id);

    /**
     * Возвращает количество заказов в репозитории.
     *
     * @return количество заказов
     */
    long count();
}
