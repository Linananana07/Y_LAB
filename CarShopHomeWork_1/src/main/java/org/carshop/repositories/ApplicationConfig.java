package org.carshop.repositories;

import org.carshop.model.Role;
import org.carshop.services.AuditService;
import org.carshop.services.CarService;
import org.carshop.services.OrderService;
import org.carshop.services.UserService;

public class ApplicationConfig {

    private final UserService userService;
    private final CarService carService;
    private final OrderService orderService;
    private final AuditService auditService;

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

    public UserService getUserService() {
        return userService;
    }

    public CarService getCarService() {
        return carService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public AuditService getAuditService() {
        return auditService;
    }
}
