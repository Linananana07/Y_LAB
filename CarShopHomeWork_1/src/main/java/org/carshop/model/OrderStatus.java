package org.carshop.model;

public enum OrderStatus {
    PENDING, COMPLETED, CANCELED;

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
