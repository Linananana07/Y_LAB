package org.carshop.console;

import org.carshop.model.Role;
import org.carshop.model.User;
import org.carshop.services.AuditService;
import org.carshop.services.CarService;
import org.carshop.services.OrderService;
import org.carshop.services.UserService;

import java.util.List;
import java.util.Scanner;

/**
 * Класс, предоставляющий интерфейс для управления пользователями.
 * Позволяет просматривать, обновлять, добавлять и удалять пользователей.
 */
public class UserDashboard {

    private UserService userService;
    private CarService carService;
    private OrderService orderService;
    private AuditService auditService;
    private MainDashboard mainDashboard;
    private Scanner scanner = new Scanner(System.in);

    /**
     * Конструктор класса UserDashboard.
     *
     * @param userService Сервис для работы с пользователями
     * @param carService Сервис для работы с автомобилями
     * @param orderService Сервис для работы с заказами
     * @param auditService Сервис для аудита
     * @param mainDashboard Главное меню
     */
    public UserDashboard(UserService userService, CarService carService,
                         OrderService orderService, AuditService auditService,
                         MainDashboard mainDashboard) {
        this.userService = userService;
        this.carService = carService;
        this.orderService = orderService;
        this.auditService = auditService;
        this.mainDashboard = mainDashboard;
    }

    /**
     * Интерфейс управления пользователями. Отображает меню управления пользователями и обрабатывает выбор пользователя.
     *
     * @param currentUser Текущий пользователь
     */
    protected void manageUsers(User currentUser) {
        while (true) {
            displayUserManagementMenu();
            int choice = getValidatedChoice();
            handleUserManagementChoice(choice, currentUser);
        }
    }

    /**
     * Отображает меню управления пользователями.
     */
    private void displayUserManagementMenu() {
        System.out.println("\nУправление пользователями\n");
        System.out.println("1. Просмотреть всех пользователей");
        System.out.println("2. Обновление информации о пользователях");
        System.out.println("3. Добавить нового пользователя");
        System.out.println("4. Удалить пользователя");
        System.out.println("0. Назад");
        System.out.print("\nВыберите опцию: ");
    }

    /**
     * Получает и возвращает валидированный выбор пользователя.
     *
     * @return Выбор пользователя
     */
    private int getValidatedChoice() {
        while (!scanner.hasNextInt()) {
            System.out.println("\nВведите корректное число.");
            scanner.next();
        }
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }

    /**
     * Обрабатывает выбор пользователя в меню управления пользователями.
     *
     * @param choice Выбор пользователя
     * @param currentUser Текущий пользователь
     */
    private void handleUserManagementChoice(int choice, User currentUser) {
        switch (choice) {
            case 1:
                handleViewAllUsers(currentUser);
                break;
            case 2:
                handleUpdateUser(currentUser);
                break;
            case 3:
                handleAddNewUser(currentUser);
                break;
            case 4:
                handleDeleteUser(currentUser);
                break;
            case 0:
                mainDashboard.navigateToDashboard(currentUser);
                return;
            default:
                System.out.println("\nНеверный выбор. Попробуйте снова.");
        }
    }

    /**
     * Отображает и обрабатывает меню просмотра всех пользователей.
     *
     * @param currentUser Текущий пользователь
     */
    private void handleViewAllUsers(User currentUser) {
        List<User> users = userService.getAllUsers();
        displayUsers(users);

        while (true) {
            displayViewAllUsersMenu();
            int choice = getValidatedChoice();
            handleViewAllUsersChoice(choice, currentUser);
        }
    }

    /**
     * Отображает меню для просмотра всех пользователей.
     */
    private void displayViewAllUsersMenu() {
        System.out.println("\n1. Отсортировать");
        System.out.println("0. Назад");
        System.out.print("\nВыберите опцию: ");
    }

    /**
     * Обрабатывает выбор пользователя в меню просмотра всех пользователей.
     *
     * @param choice Выбор пользователя
     * @param currentUser Текущий пользователь
     */
    private void handleViewAllUsersChoice(int choice, User currentUser) {
        switch (choice) {
            case 1:
                handleFilterAndSortUsers(currentUser);
                break;
            case 0:
                manageUsers(currentUser);
                return;
            default:
                System.out.println("\nНеверный выбор. Попробуйте снова.");
        }
    }

    /**
     * Запрашивает у пользователя параметры фильтрации и сортировки пользователей.
     *
     * @param currentUser Текущий пользователь
     */
    private void handleFilterAndSortUsers(User currentUser) {
        while (true) {
            displayFilterAndSortUsersMenu();
            int choice = getValidatedChoice();
            handleFilterAndSortUsersChoice(choice, currentUser);
        }
    }

    /**
     * Отображает меню фильтрации и сортировки пользователей.
     */

    private void displayFilterAndSortUsersMenu() {
        System.out.println("\nФильтрация и сортировка пользователей\n");
        System.out.println("1. Фильтрация по имени");
        System.out.println("2. Фильтрация по роли");
        System.out.println("3. Сортировка по количеству покупок");
        System.out.println("0. Назад");
        System.out.print("\nВыберите опцию: ");
    }

    /**
     * Обрабатывает выбор пользователя в меню фильтрации и сортировки пользователей.
     *
     * @param choice Выбор пользователя
     * @param currentUser Текущий пользователь
     */
    private void handleFilterAndSortUsersChoice(int choice, User currentUser) {
        switch (choice) {
            case 1:
                handleFilterUsersByName(currentUser);
                break;
            case 2:
                handleFilterUsersByRole(currentUser);
                break;
            case 3:
                handleSortUsersByPurchases(currentUser);
                break;
            case 0:
                handleViewAllUsers(currentUser);
                return;
            default:
                System.out.println("\nНеверный выбор. Попробуйте снова.");
        }
    }

    /**
     * Фильтрует пользователей по имени.
     *
     * @param currentUser Текущий пользователь
     */
    private void handleFilterUsersByName(User currentUser) {
        System.out.print("\nВведите имя пользователя для фильтрации: ");
        String name = scanner.nextLine();
        List<User> filteredUsers = userService.getUsersByName(name);
        displayUsers(filteredUsers);
    }

    /**
     * Фильтрует пользователей по роли.
     *
     * @param currentUser Текущий пользователь
     */
    private void handleFilterUsersByRole(User currentUser) {
        System.out.println("\nДоступные роли:");
        for (Role role : Role.values()) {
            System.out.println(role.ordinal() + 1 + ". " + Role.getDisplayName(role));
        }
        System.out.print("\nВыберите роль для фильтрации: ");
        int choice = getValidatedChoice() - 1;

        if (choice >= 0 && choice < Role.values().length) {
            Role role = Role.values()[choice];
            List<User> filteredUsers = userService.getUsersByRole(role);
            displayUsers(filteredUsers);
        } else {
            System.out.println("\nНеверный выбор роли.");
        }
    }

    /**
     * Сортирует пользователей по количеству покупок.
     *
     * @param currentUser Текущий пользователь
     */
    private void handleSortUsersByPurchases(User currentUser) {
        displaySortUsersByPurchasesMenu();
        int choice = getValidatedChoice();
        List<User> sortedUsers = userService.sortUsersByPurchases(choice == 1);
        displayUsers(sortedUsers);
    }

    /**
     * Отображает меню сортировки пользователей по количеству покупок.
     */
    private void displaySortUsersByPurchasesMenu() {
        System.out.println("\nСортировка пользователей по количеству покупок\n");
        System.out.println("1. По возрастанию");
        System.out.println("2. По убыванию");
        System.out.print("\nВыберите опцию: ");
    }

    /**
     * Отображает список пользователей.
     *
     * @param users Список пользователей для отображения
     */
    private void displayUsers(List<User> users) {
        if (users.isEmpty()) {
            System.out.println("\nНет доступных пользователей.");
        } else {
            for (User user : users) {
                System.out.println("ID: " + user.getId());
                System.out.println("Логин: " + user.getUsername());
                System.out.println("Роль: " + user.getRole());
                System.out.println("Количество покупок: " + user.getPurchaseCount());
                System.out.println();
            }
        }
    }

    /**
     * Добавляет нового пользователя.
     *
     * @param currentUser Текущий пользователь
     */
    private void handleAddNewUser(User currentUser) {
        String login = getValidatedLogin();
        String password = getValidatedPassword();
        Role role = getRoleFromInput();

        boolean registered = userService.registerUser(login, password, role);
        if (registered) {
            System.out.println("\nПользователь добавлен.");
            auditService.logAction(currentUser, "Добавлен новый пользователь: " + login);
        } else {
            System.out.println("\nПользователь с таким логином уже существует.");
        }
    }

    /**
     * Получает и валидирует логин нового пользователя.
     *
     * @return Логин нового пользователя
     */
    private String getValidatedLogin() {
        String login = mainDashboard.getValidLogin();
        return login;
    }

    /**
     * Получает и валидирует пароль нового пользователя.
     *
     * @return Пароль нового пользователя
     */
    private String getValidatedPassword() {
        String password = mainDashboard.getValidPassword();
        return password;
    }

    /**
     * Получает роль пользователя от ввода.
     *
     * @return Роль пользователя
     */
    private Role getRoleFromInput() {
        Role role = null;
        while (role == null) {
            System.out.println("\nВыберите роль пользователя:");
            for (Role r : Role.values()) {
                System.out.println(r.ordinal() + 1 + ". " + Role.getDisplayName(r));
            }
            System.out.print("\nВыберите роль (1-" + Role.values().length + "): ");
            int choice = getValidatedChoice() - 1;

            if (choice >= 0 && choice < Role.values().length) {
                role = Role.values()[choice];
            } else {
                System.out.println("\nНеверный выбор. Попробуйте снова.");
            }
        }
        return role;
    }

    /**
     * Удаляет пользователя.
     *
     * @param currentUser Текущий пользователь
     */
    private void handleDeleteUser(User currentUser) {
        System.out.print("\nВведите ID пользователя для удаления: ");
        String userId = scanner.nextLine();

        if (!canDeleteUser(currentUser, userId)) {
            manageUsers(currentUser);
        }

        User userToDelete = userService.findById(userId);

        if (userToDelete != null) {
            userService.delete(userId);
            System.out.println("\nПользователь удален.");
            auditService.logAction(currentUser, "Удален пользователь: " + userToDelete.getUsername());
            userService.updateUserIndices();
        } else {
            System.out.println("\nПользователь с ID " + userId + " не найден.");
        }
    }

    /**
     * Проверяет, что текущий пользователь не пытается удалить себя.
     *
     * @param currentUser Текущий пользователь
     * @param userId ID пользователя для удаления
     * @return true, если пользователь может быть удален, иначе false
     */
    private boolean canDeleteUser(User currentUser, String userId) {
        if (currentUser.getId().equals(userId)) {
            System.out.println("\nВы не можете удалить сами себя.");
            return false;
        }
        return true;
    }


    /**
     * Обновляет информацию о пользователе.
     *
     * @param currentUser Текущий пользователь
     */
    private void handleUpdateUser(User currentUser) {
        System.out.print("\nВведите ID пользователя для обновления: ");
        String userId = scanner.nextLine(); // Получаем ID пользователя
        User userToUpdate = userService.findById(userId); // Используем findById для поиска пользователя

        if (userToUpdate != null) {
            boolean isCurrentUser = userToUpdate.getId().equals(currentUser.getId());

            System.out.println("\nВыберите, что обновить:");
            System.out.println("1. Логин");
            System.out.println("2. Пароль");
            System.out.println("3. Роль");
            System.out.print("\nВыберите опцию: ");

            int choice = getValidatedChoice();

            switch (choice) {
                case 1:
                    userToUpdate.setUsername(getValidatedLogin());
                    break;
                case 2:
                    userToUpdate.setPassword(getValidatedPassword());
                    break;
                case 3:
                    if (!isCurrentUser) {
                        userToUpdate.setRole(getRoleFromInput());
                    } else {
                        System.out.println("\nВы не можете обновить свою роль.");
                        return;
                    }
                    break;
                default:
                    System.out.println("\nНеверный выбор.");
                    return;
            }

            userService.update(userToUpdate); // Обновляем пользователя

            System.out.println("\nПользователь обновлен.");
            auditService.logAction(currentUser, "Обновлен пользователь: " + userId);
        } else {
            System.out.println("\nПользователь не найден.");
        }
    }
}
