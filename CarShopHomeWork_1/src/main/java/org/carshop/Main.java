package org.carshop;

import org.carshop.console.MainDashboard;
import org.carshop.model.ConditionCar;
import org.carshop.model.Role;
import org.carshop.repositories.ApplicationConfig;
import org.carshop.services.CarService;
import org.carshop.services.OrderService;
import org.carshop.services.UserService;
import org.carshop.services.AuditService;
import org.carshop.repositories.InMemoryCarRepository;
import org.carshop.repositories.InMemoryOrderRepository;
import org.carshop.repositories.InMemoryUserRepository;

/**
 * Главный класс приложения.
 * <p>Этот класс содержит метод {@code main}, который запускает приложение, создавая необходимые сервисы и отображая главный экран.</p>
 */
public class Main {

    /**
     * Главный метод приложения.
     * <p>Создает конфигурацию приложения, инициализирует {@link MainDashboard} с сервисами,
     * полученными из конфигурации, и запускает главный экран.</p>
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        ApplicationConfig config = new ApplicationConfig();

        MainDashboard mainDashboard = new MainDashboard(
                config.getUserService(),
                config.getCarService(),
                config.getOrderService(),
                config.getAuditService()
        );

        mainDashboard.start();
    }
}