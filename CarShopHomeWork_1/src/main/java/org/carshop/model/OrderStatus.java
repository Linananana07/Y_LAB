package org.carshop.model;

/**
 * Перечисление статусов заказа.
 */
public enum OrderStatus {
    PENDING, COMPLETED, CANCELED;

    /**
     * Возвращает человекочитаемое имя статуса заказа.
     *
     * @param orderStatus статус заказа
     * @return человекочитаемое имя статуса заказа
     */
    public static String getDisplayName(OrderStatus orderStatus) {
        switch (orderStatus) {
            case PENDING:
                return "В ожидании подтверждения";
            case COMPLETED:
                return "Завершенный";
            case CANCELED:
                return "Отмененный";
            default:
                return orderStatus.name();
        }
    }
}
