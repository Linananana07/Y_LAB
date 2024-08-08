package org.carshop.model;

import lombok.Data;

/**
 * Представляет пользователя системы.
 */
@Data
public class User {
    private String id;
    private String username;
    private String password;
    private Role role;
    private int purchaseCount;

    /**
     * Создает новый объект пользователя с указанными параметрами.
     *
     * @param id уникальный идентификатор пользователя
     * @param username имя пользователя
     * @param password пароль пользователя
     * @param role роль пользователя в системе
     *             * @param purchaseCount количество покупок пользователя,
     *             по умолчанию установлено в 0
     */
    public User(String id, String username, String password, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.purchaseCount = 0;
    }
}
