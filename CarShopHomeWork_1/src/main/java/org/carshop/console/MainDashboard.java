package org.carshop.console;

import org.carshop.model.*;
import org.carshop.services.AuditService;
import org.carshop.services.CarService;
import org.carshop.services.OrderService;
import org.carshop.services.UserService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class MainDashboard {

    private UserService userService;
    private CarService carService;
    private OrderService orderService;
    private AuditService auditService;
    private CarDashboard carDashboard;
    private OrderDashboard orderDashboard;
    private UserDashboard userDashboard;
    private AuditDashboard auditDashboard;

    private Scanner scanner = new Scanner(System.in);

    public MainDashboard(UserService userService, CarService carService,
                         OrderService orderService, AuditService auditService) {
        this.userService = userService;
        this.carService = carService;
        this.orderService = orderService;
        this.auditService = auditService;
        this.carDashboard = new CarDashboard(userService, carService, orderService, auditService, this);
        this.orderDashboard = new OrderDashboard(userService, carService, orderService, auditService, this);
        this.userDashboard = new UserDashboard(userService, carService, orderService, auditService, this);
        this.auditDashboard = new AuditDashboard(userService, carService, orderService, auditService, this);
    }

    //Интерфейс при запуске приложения
    public void start() {
        int choice = -1;
        while (true) {
            System.out.println("Добро пожаловать в Car Shop\n");
            System.out.println("1. Регистрация");
            System.out.println("2. Вход в систему");
            System.out.print("Выберите опцию: ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("\nВведите корректное число.");
            }

            switch (choice) {
                case 1:
                    System.out.println("\nРегистрация пользователя");
                    registerUser();
                    break;
                case 2:
                    loginUser();
                    break;
                default:
                    System.out.println("\nНеверный выбор. Попробуйте снова.\n");
            }
        }
    }

    //Интерфейс регистрации пользователя
    private void registerUser() {
        String login;
        String password;
        String loginPattern = "^[a-zA-Z0-9._-]+$";
        String checkForLetters = "^[\\d._-]+$";
        String passwordPattern = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d._-]+$";
        Role role = Role.CLIENT;

        while (true) {
            System.out.println("\nЛогин должен содержать:\n");
            System.out.println("  Буквы латинского алфавита");
            System.out.println("  Не менее 4-х символов");
            System.out.println("\nЛогин может содержать:\n");
            System.out.println("  Цифры");
            System.out.println("  Знаки препинания (. - _)");
            System.out.print("Введите логин: ");
            login = scanner.nextLine().toLowerCase();

            if (login.length() < 4
                    || !login.matches(loginPattern)
                    || login.matches(checkForLetters)) {
                System.out.println("\nНеверный формат логина, попробуйте еще раз...");
            } else if (userService.findByUsername(login) != null) {
                System.out.println("\nЭтот логин уже существует. Пожалуйста, выберите другой.");
            }else {
                break;
            }
        }

        while (true) {
            System.out.println("\nПароль должен содержать:\n");
            System.out.println("  Буквы латинского алфавита");
            System.out.println("  Не менее 4-х символов");
            System.out.println("  Цифры");
            System.out.println("\nПароль может содержать:\n");
            System.out.println("  Знаки препинания");
            System.out.print("Введите пароль: ");
            password = scanner.nextLine();

            if (password.length() < 4
                    || !password.matches(passwordPattern)
                    || password.matches(checkForLetters)) {
                System.out.println("\nНеверный формат пароля, попробуйте еще раз...");
            } else {
                break;
            }
        }

        boolean registered = userService.registerUser(login, password, role);
        if (registered) {
            System.out.println("\nВаш аккаунт зарегистрирован!");
        } else {
            System.out.println("\nОшибка при регистрации. Попробуйте еще раз.");
        }
    }

    //Интерфейс входа в аккаунт
    private void loginUser() {
        String login;
        String password;

        System.out.println("\nВход в аккаунт\n");

        System.out.print("Введите логин: ");
        login = scanner.nextLine().toLowerCase();

        System.out.print("Введите пароль: ");
        password = scanner.nextLine();

        User user = userService.login(login, password);

        if (user != null) {
            userService.setCurrentUser(user);
            System.out.println("\nДобро пожаловать, " + user.getUsername() + "!");
            switch (user.getRole()) {
                case ADMIN:
                    adminDashboard(user);
                    break;
                case MANAGER:
                    managerDashboard(user);
                    break;
                case CLIENT:
                    clientDashboard(user);
                    break;
            }
        } else {
            System.out.println("\nНеверный логин или пароль. Попробуйте еще раз.\n");
            loginUser();
        }

    }

    //Интерфейс главного меню админа
    public void adminDashboard(User admin) {

        int choice = -1;
        while (true) {
            System.out.println("\nАдмин-панель\n");
            System.out.println("1. Управление автомобилями");
            System.out.println("2. Управление заказами");
            System.out.println("3. Управление пользователями");
            System.out.println("4. Просмотр журнала действий");
            System.out.println("0. Выйти из системы");
            System.out.print("\nВыберите опцию: ");

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("\nВведите корректное число.");
            }

            switch (choice) {
                case 1:
                    carDashboard.manageCars(admin);
                    break;
                case 2:
                    orderDashboard.manageOrders(admin);
                    break;
                case 3:
                    userDashboard.manageUsers(admin);
                    break;
                case 4:
                    auditDashboard.manageAudit(admin);
                    break;
                case 0:
                    System.out.println("\nВыход из системы...");
                    start();
                default:
                    System.out.println("\nНеверный выбор. Попробуйте снова.");
            }
        }
    }

    //Интерфейс главного меню менеджера
    public void managerDashboard(User manager) {

        int choice = -1;
        while (true) {
            System.out.println("\nПанель менеджера\n");
            System.out.println("1. Управление автомобилями");
            System.out.println("2. Управление заказами");
            System.out.println("0. Выйти из системы");
            System.out.print("\nВыберите опцию: ");

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("\nВведите корректное число.");
            }

            switch (choice) {
                case 1:
                    carDashboard.manageCars(manager);
                    break;
                case 2:
                    orderDashboard.manageOrders(manager);
                    break;
                case 0:
                    System.out.println("\nВыход из системы...");
                    start();
                default:
                    System.out.println("\nНеверный выбор. Попробуйте снова.");
            }
        }
    }

    //Интерфейс главного меню пользователя
    public void clientDashboard(User client) {

        List<Order> sortedOrders = new ArrayList<>(orderService.getAllOrders());
        int choice = -1;
        while (true) {
            System.out.println("\nЛичный кабинет\n");
            System.out.println("1. Просмотреть доступные автомобили");
            System.out.println("2. Создать заказ на покупку");
            System.out.println("3. Просмотреть мои заказы");
            System.out.println("0. Выйти из системы");
            System.out.print("\nВыберите опцию: ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("\nВведите корректное число.");
            }

            switch (choice) {
                case 1:
                    carDashboard.viewAllCars(client);
                    break;
                case 2:
                    orderDashboard.createNewOrder(client);
                    break;
                case 3:
                    List<Order> clientOrders = orderService.getAllOrders().stream()
                            .filter(order -> order.getClient().getUsername().equals(client.getUsername()))
                            .collect(Collectors.toList());
                    if (clientOrders.isEmpty()) {
                        System.out.println("\nУ вас нет заказов.");
                    } else {
                        orderDashboard.displayOrders(client, clientOrders);
                    }
                    break;
                case 0:
                    System.out.println("\nВыход из системы...");
                    start();
                default:
                    System.out.println("\nНеверный выбор. Попробуйте снова.");
            }
        }
    }

    public void goToMainDashboard(User currentUser) {
        switch (currentUser.getRole()) {
            case ADMIN:
                adminDashboard(currentUser);
                break;
            case MANAGER:
                managerDashboard(currentUser);
                break;
            case CLIENT:
                clientDashboard(currentUser);
                break;
            default:
                System.out.println("Неизвестная роль.");
                break;
        }
    }
}
