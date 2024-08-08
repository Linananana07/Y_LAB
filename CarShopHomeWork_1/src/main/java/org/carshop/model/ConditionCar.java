package org.carshop.model;

/**
 * Перечисление состояний автомобилей.
 */
public enum ConditionCar {
    NEW, USED, PRE_OWNED, REPUBLIC, REPAIRED, SALVAGE, DEMO, TEST_VEHICLE;

    /**
     * Возвращает человекочитаемое имя состояния автомобиля.
     *
     * @param condition состояние автомобиля
     * @return человекочитаемое имя состояния автомобиля
     */
    public static String getDisplayName(ConditionCar condition) {
        switch (condition) {
            case NEW:
                return "Новый";
            case USED:
                return "Поддержанный";
            case PRE_OWNED:
                return "С пробегом";
            case REPUBLIC:
                return "Восстановленный";
            case REPAIRED:
                return "Ремонтированный";
            case SALVAGE:
                return "Для восстановления";
            case DEMO:
                return "Демонстрационный";
            case TEST_VEHICLE:
                return "Тестовый";
            default:
                return condition.name();
        }
    }
}
