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

public class Main {
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