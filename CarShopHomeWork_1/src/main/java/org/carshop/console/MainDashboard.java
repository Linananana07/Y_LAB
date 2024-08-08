package org.carshop.console;

import org.carshop.model.*;
import org.carshop.repositories.OrderRepository;
import org.carshop.services.AuditService;
import org.carshop.services.CarService;
import org.carshop.services.OrderService;
import org.carshop.services.UserService;

import java.util.Scanner;

/**
 * Основной класс панели управления, предоставляющий интерфейс для регистрации пользователей,
 * входа в систему и навигации по различным панелям управления в зависимости от роли пользователя.
 */
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

    /**
     * Конструктор класса MainDashboard.
     *
     * @param userService сервис для управления пользователями
     * @param carService  сервис для управления автомобилями
     * @param orderService сервис для управления заказами
     * @param auditService сервис для управления аудитом
     */
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

    /**
     * Запускает основное меню, которое позволяет пользователю выбрать опцию регистрации или входа в систему.
     */
    public void start() {
        int choice;
        while (true) {
            displayMainMenu();
            choice = getUserChoice();
            handleMainMenuChoice(choice);
        }
    }

    /**
     * Отображает основное меню с опциями регистрации и входа в систему.
     */
    private void displayMainMenu() {
        System.out.println("Добро пожаловать в Car Shop\n");
        System.out.println("1. Регистрация");
        System.out.println("2. Вход в систему");
        System.out.print("Выберите опцию: ");
    }

    /**
     * Отображает сообщение с требованиями к логину.
     */
    public void registerMassageLogin() {
        System.out.println("\nЛогин должен содержать:\n");
        System.out.println("  Буквы латинского алфавита");
        System.out.println("  Не менее 4-х символов");
        System.out.println("\nЛогин может содержать:\n");
        System.out.println("  Цифры");
        System.out.println("  Знаки препинания (. - _)\n");
    }

    /**
     * Отображает сообщение с требованиями к паролю.
     */
    public void registerMassagePassword() {
        System.out.println("\nПароль должен содержать:\n");
        System.out.println("  Буквы латинского алфавита");
        System.out.println("  Не менее 4-х символов");
        System.out.println("  Цифры");
        System.out.println("\nПароль может содержать:\n");
        System.out.println("  Знаки препинания\n");
    }

    /**
     * Обрабатывает выбор пользователя в основном меню и вызывает соответствующие методы.
     *
     * @param choice выбранная опция
     */
    private void handleMainMenuChoice(int choice) {
        switch (choice) {
            case 1:
                registerUser();
                break;
            case 2:
                loginUser();
                break;
            default:
                System.out.println("\nНеверный выбор. Попробуйте снова.\n");
        }
    }

    /**
     * Регистрирует нового пользователя, запрашивая логин и пароль.
     */
    private void registerUser() {
        String login = getValidLogin();
        String password = getValidPassword();
        Role role = Role.CLIENT; // Default role
        boolean registered = userService.registerUser(login, password, role);
        if (registered) {
            System.out.println("\nВаш аккаунт зарегистрирован!");
        } else {
            System.out.println("\nОшибка при регистрации. Попробуйте еще раз.");
        }
    }

    /**
     * Запрашивает у пользователя корректный логин, проверяя его формат и уникальность.
     *
     * @return корректный логин
     */
    public String getValidLogin() {
        String login;
        while (true) {
            registerMassageLogin();
            System.out.print("Введите логин: ");
            login = scanner.nextLine().toLowerCase();
            if (isValidLogin(login)) {
                if (userService.findByUsername(login) == null) {
                    break;
                } else {
                    System.out.println("\nЭтот логин уже существует. Пожалуйста, выберите другой.");
                }
            } else {
                System.out.println("\nНеверный формат логина, попробуйте еще раз...");
            }
        }
        return login;
    }

    /**
     * Проверяет корректность формата логина.
     *
     * @param login логин для проверки
     * @return true, если логин соответствует требованиям, иначе false
     */
    private boolean isValidLogin(String login) {
        String loginPattern = "^[a-zA-Z0-9._-]+$";
        String checkForLetters = "^[\\d._-]+$";
        return login.length() >= 4 && login.matches(loginPattern) && !login.matches(checkForLetters);
    }

    /**
     * Запрашивает у пользователя корректный пароль, проверяя его формат.
     *
     * @return корректный пароль
     */
    public String getValidPassword() {
        String password;
        while (true) {
            registerMassagePassword();
            System.out.print("Введите пароль: ");
            password = scanner.nextLine();
            if (isValidPassword(password)) {
                break;
            } else {
                System.out.println("\nНеверный формат пароля, попробуйте еще раз...");
            }
        }
        return password;
    }

    /**
     * Проверяет корректность формата пароля.
     *
     * @param password пароль для проверки
     * @return true, если пароль соответствует требованиям, иначе false
     */
    private boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d._-]+$";
        String checkForLetters = "^[\\d._-]+$";
        return password.length() >= 4 && password.matches(passwordPattern) && !password.matches(checkForLetters);
    }

    /**
     * Выполняет вход пользователя в систему, проверяя логин и пароль.
     */
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
            navigateToDashboard(user);
        } else {
            System.out.println("\nНеверный логин или пароль. Попробуйте еще раз.\n");
            loginUser();
        }
    }

    /**
     * Перенаправляет пользователя на соответствующую панель управления в зависимости от его роли.
     *
     * @param user текущий пользователь
     */
    public void navigateToDashboard(User user) {
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
    }

    /**
     * Отображает панель управления для администратора.
     *
     * @param admin текущий администратор
     */
    public void adminDashboard(User admin) {
        int choice = -1;
        while (true) {
            displayAdminMenu();
            choice = getUserChoice();
            handleAdminMenuChoice(admin, choice);
        }
    }

    /**
     * Отображает меню для администратора.
     */
    private void displayAdminMenu() {
        System.out.println("\nАдмин-панель\n");
        System.out.println("1. Управление автомобилями");
        System.out.println("2. Управление заказами");
        System.out.println("3. Управление пользователями");
        System.out.println("4. Просмотр журнала действий");
        System.out.println("0. Выйти из системы");
        System.out.print("\nВыберите опцию: ");
    }

    /**
     * Обрабатывает выбор администратора в меню панели управления.
     *
     * @param admin текущий администратор
     * @param choice выбранная опция
     */
    private void handleAdminMenuChoice(User admin, int choice) {
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
                break;
            default:
                System.out.println("\nНеверный выбор. Попробуйте снова.");
        }
    }

    /**
     * Отображает панель управления для менеджера.
     *
     * @param manager текущий менеджер
     */
    public void managerDashboard(User manager) {
        int choice = -1;
        while (true) {
            displayManagerMenu();
            choice = getUserChoice();
            handleManagerMenuChoice(manager, choice);
        }
    }

    /**
     * Отображает меню для менеджера.
     */
    private void displayManagerMenu() {
        System.out.println("\nПанель менеджера\n");
        System.out.println("1. Управление автомобилями");
        System.out.println("2. Управление заказами");
        System.out.println("0. Выйти из системы");
        System.out.print("\nВыберите опцию: ");
    }

    /**
     * Обрабатывает выбор менеджера в меню панели управления.
     *
     * @param manager текущий менеджер
     * @param choice выбранная опция
     */
    private void handleManagerMenuChoice(User manager, int choice) {
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
                break;
            default:
                System.out.println("\nНеверный выбор. Попробуйте снова.");
        }
    }

    /**
     * Отображает панель управления для клиента.
     *
     * @param client текущий клиент
     */
    public void clientDashboard(User client) {
        int choice = -1;
        while (true) {
            displayClientMenu();
            choice = getUserChoice();
            handleClientMenuChoice(client, choice);
        }
    }

    /**
     * Отображает меню для клиента.
     */
    private void displayClientMenu() {
        System.out.println("\nЛичный кабинет\n");
        System.out.println("1. Просмотреть доступные автомобили");
        System.out.println("2. Создать заказ на покупку");
        System.out.println("3. Просмотреть мои заказы");
        System.out.println("0. Выйти из системы");
        System.out.print("\nВыберите опцию: ");
    }

    /**
     * Обрабатывает выбор клиента в меню панели управления.
     *
     * @param client текущий клиент
     * @param choice выбранная опция
     */
    private void handleClientMenuChoice(User client, int choice) {
        switch (choice) {
            case 1:
                carDashboard.viewAllCars(client);
                break;
            case 2:
                orderDashboard.createNewOrder(client);
                break;
            case 3:
                orderDashboard.filterByClientId(client);
                break;
            case 0:
                System.out.println("\nВыход из системы...");
                start();
                break;
            default:
                System.out.println("\nНеверный выбор. Попробуйте снова.");
        }
    }

    /**
     * Считывает выбор пользователя и возвращает его в виде числа.
     *
     * @return выбранная опция
     */
    public int getUserChoice() {
        int choice = -1;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("\nВведите корректное число.");
        }
        return choice;
    }
}
