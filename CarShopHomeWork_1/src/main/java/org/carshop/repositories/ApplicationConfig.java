package org.carshop.repositories;

import org.carshop.model.Role;
import org.carshop.services.AuditService;
import org.carshop.services.CarService;
import org.carshop.services.OrderService;
import org.carshop.services.UserService;

/**
 * Конфигурационный класс для инициализации сервисов и репозиториев приложения.
 *
 * <p>Этот класс настраивает репозитории и сервисы для работы с пользователями, автомобилями, заказами и аудитом.
 * Также он инициализирует начальных пользователей в системе.</p>
 */
public class ApplicationConfig {

    private final UserService userService;
    private final CarService carService;
    private final OrderService orderService;
    private final AuditService auditService;

    /**
     * Конструктор класса {@code ApplicationConfig}.
     *
     * <p>Создает экземпляры репозиториев для пользователей, автомобилей и заказов,
     * и инициализирует соответствующие сервисы. Также добавляет начальных пользователей в систему.</p>
     */
    public ApplicationConfig() {
        InMemoryUserRepository userRepository = new InMemoryUserRepository();
        InMemoryCarRepository carRepository = new InMemoryCarRepository();
        InMemoryOrderRepository orderRepository = new InMemoryOrderRepository();

        userService = new UserService(userRepository, orderRepository);
        carService = new CarService(carRepository);
        orderService = new OrderService(orderRepository);
        auditService = new AuditService();

        // Инициализация пользователей
        userService.registerUser("admin", "admin", Role.ADMIN);
        userService.registerUser("manager", "manager", Role.MANAGER);
        userService.registerUser("user", "user", Role.CLIENT);
    }

    /**
     * Возвращает сервис для управления пользователями.
     *
     * @return экземпляр {@link UserService}
     */
    public UserService getUserService() {
        return userService;
    }

    /**
     * Возвращает сервис для управления автомобилями.
     *
     * @return экземпляр {@link CarService}
     */
    public CarService getCarService() {
        return carService;
    }

    /**
     * Возвращает сервис для управления заказами.
     *
     * @return экземпляр {@link OrderService}
     */
    public OrderService getOrderService() {
        return orderService;
    }

    /**
     * Возвращает сервис для работы с аудитом.
     *
     * @return экземпляр {@link AuditService}
     */
    public AuditService getAuditService() {
        return auditService;
    }
}
