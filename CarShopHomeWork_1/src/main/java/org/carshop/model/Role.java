package org.carshop.model;

/**
 * Перечисление ролей пользователей в системе.
 * Каждая роль имеет отображаемое имя для удобства использования в пользовательском интерфейсе.
 */
public enum Role {
    /**
     * Роль администратора системы.
     */
    ADMIN("Администратор"),

    /**
     * Роль менеджера системы.
     */
    MANAGER("Менеджер"),

    /**
     * Роль клиента системы.
     */
    CLIENT("Клиент");

    private final String displayName;

    /**
     * Создает объект роли с указанным отображаемым именем.
     *
     * @param displayName отображаемое имя роли
     */
    Role(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Возвращает отображаемое имя роли.
     *
     * @return отображаемое имя роли
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Возвращает отображаемое имя для указанной роли.
     * Если роль равна {@code null}, возвращается значение {@code "UNKNOWN_ROLE"}.
     *
     * @param role роль, для которой нужно получить отображаемое имя
     * @return отображаемое имя роли, или {@code "UNKNOWN_ROLE"}, если роль равна {@code null}
     */
    public static String getDisplayName(Role role) {
        return (role == null) ? "UNKNOWN_ROLE" : role.getDisplayName();
    }
}
