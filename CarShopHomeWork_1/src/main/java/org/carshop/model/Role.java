package org.carshop.model;

public enum Role {
    ADMIN, MANAGER, CLIENT;

    public static String getDisplayName(Role role) {
        switch (role) {
            case ADMIN:
                return "Администратор";
            case MANAGER:
                return "Менеджер";
            case CLIENT:
                return "Клиент";
            default:
                return role.name();
        }
    }
}
