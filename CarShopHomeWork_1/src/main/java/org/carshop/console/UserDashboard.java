package org.carshop.console;

import org.carshop.model.Role;
import org.carshop.model.User;
import org.carshop.services.AuditService;
import org.carshop.services.CarService;
import org.carshop.services.OrderService;
import org.carshop.services.UserService;

import java.util.List;
import java.util.Scanner;

public class UserDashboard {

    private UserService userService;
    private CarService carService;
    private OrderService orderService;
    private AuditService auditService;
    private MainDashboard mainDashboard;
    private Scanner scanner = new Scanner(System.in);

    public UserDashboard(UserService userService, CarService carService,
                        OrderService orderService, AuditService auditService,
                        MainDashboard mainDashboard) {
        this.userService = userService;
        this.carService = carService;
        this.orderService = orderService;
        this.auditService = auditService;
        this.mainDashboard = mainDashboard;
    }

    //Интерфейс управление пользователями
    protected void manageUsers(User currentUser) {
        int choice;
        while (true) {
            System.out.println("\nУправление пользователями\n");
            System.out.println("1. Просмотреть всех пользователей");
            System.out.println("2. Обновление информации о пользователях");
            System.out.println("3. Добавить нового пользователя");
            System.out.println("4. Удалить пользователя");
            System.out.println("0. Назад");
            System.out.print("\nВыберите опцию: ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("\nВведите корректное число.");
                scanner.next();
                continue;
            }

            switch (choice) {
                case 1:
                    viewAllUsers(currentUser);
                    break;
                case 2:
                    updateUser(currentUser);
                    break;
                case 3:
                    addNewUser(currentUser);
                    break;
                case 4:
                    deleteUser(currentUser);
                    break;
                case 0:
                    mainDashboard.goToMainDashboard(currentUser);
                default:
                    System.out.println("\nНеверный выбор. Попробуйте снова.");
            }
        }
    }

    //Интерфейс всех пользователей
    private void viewAllUsers(User currentUser) {
        int choice;
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("\nНет доступных пользователей.");
        } else {
            for (User user : users) {
                System.out.println("ID: " + user.getId());
                System.out.println("Логин: " + user.getUsername());
                System.out.println("Роль: " + user.getRole());
                System.out.println();
            }
        }
        while (true) {
            System.out.println("\n1. Отсортировать");
            System.out.println("0. Назад");
            System.out.print("\nВыберите опцию: ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("\nВведите корректное число.");
                scanner.next();
                continue;
            }

            switch (choice) {
                case 1:
                    filterAndSortUsers(currentUser);
                case 0:
                    manageUsers(currentUser);
                default:
                    System.out.println("\nНеверный выбор. Попробуйте снова.");
            }
        }
    }

    private void filterAndSortUsers(User currentUser) {

        int choice;

        while (true) {
            System.out.println("\nФильтрация и сортировка пользователей\n");
            System.out.println("1. Фильтрация по имени");
            System.out.println("2. Фильтрация по роли");
            System.out.println("3. Сортировка по количеству покупок");
            System.out.println("0. Назад");
            System.out.print("\nВыберите опцию: ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("\nВведите корректное число.");
                scanner.next();
                continue;
            }

            switch (choice) {
                case 1:
                    filterUsersByName(currentUser);
                    break;
                case 2:
                    filterUsersByRole(currentUser);
                    break;
                case 3:
                    sortUsersByPurchases(currentUser);
                    break;
                case 0:
                    viewAllUsers(currentUser);
                    return;
                default:
                    System.out.println("\nНеверный выбор. Попробуйте снова.");
            }
        }
    }

    // Фильтрация пользователей по имени
    private void filterUsersByName(User currentUser) {
        System.out.print("\nВведите имя пользователя для фильтрации: ");
        String name = scanner.nextLine();
        List<User> filteredUsers = userService.getUsersByName(name);
        displayUsers(currentUser, filteredUsers);
    }

    // Фильтрация пользователей по роли
    private void filterUsersByRole(User currentUser) {
        int choice;
        while (true) {

            System.out.println("\nДоступные роли:");
            for (Role role : Role.values()) {
                System.out.println(role.ordinal() + 1 + ". " + Role.getDisplayName(role));
            }
            System.out.print("\nВыберите роль для фильтрации: ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("\nВведите корректное число.");
                scanner.next();
                continue;
            }

            Role role;
            switch (choice) {
                case 1:
                    role = Role.ADMIN;
                    break;
                case 2:
                    role = Role.MANAGER;
                    break;
                case 3:
                    role = Role.CLIENT;
                    break;
                default:
                    System.out.println("\nНеверный выбор роли.");
                    return;
            }
            List<User> filteredUsers = userService.getUsersByRole(role);
            displayUsers(currentUser, filteredUsers);
        }
    }

    // Сортировка пользователей по количеству покупок
    private void sortUsersByPurchases(User currentUser) {

        int choice;

        while (true) {

            System.out.println("\nСортировка пользователей по количеству покупок\n");
            System.out.println("1. По возрастанию");
            System.out.println("2. По убыванию");
            System.out.print("\nВыберите опцию: ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("\nВведите корректное число.");
                scanner.next();
                continue;
            }

            List<User> sortedUsers = userService.sortUsersByPurchases(choice == 1);
            displayUsers(currentUser, sortedUsers);
        }
    }

    // Отображение списка пользователей
    private void displayUsers(User currentUser, List<User> users) {
        int choice = -1;

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
        while (true) {
            System.out.println("0. Назад");
            System.out.print("\nВыберите опцию: ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("\nВведите корректное число.");
                scanner.next();
                continue;
            }
            switch (choice) {
                case 0:
                    filterAndSortUsers(currentUser);
                default:
                    System.out.println("\nНеверный выбор. Попробуйте снова.");
            }
        }
    }

    //Интерфейс добавления нового пользователя
    private void addNewUser(User currentUser) {

        String login;
        String password;
        String loginPattern = "^[a-zA-Z0-9._-]+$";
        String checkForLetters = "^[\\d._-]+$";
        String passwordPattern = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d._-]+$";

        while (true) {
            System.out.println("\nЛогин должен содержать:\n");
            System.out.println("  Буквы латинского алфавита");
            System.out.println("  Не менее 4-х символов");
            System.out.println("\nЛогин может содержать:\n");
            System.out.println("  Цифры");
            System.out.println("  Знаки препинания (. - _)");
            System.out.print("\nВведите логин: ");
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
            System.out.print("\nВведите пароль: ");
            password = scanner.nextLine().toLowerCase();

            if (password.length() < 4
                    || !password.matches(passwordPattern)
                    || password.matches(checkForLetters)) {
                System.out.println("\nНеверный формат пароля, попробуйте еще раз...");
            } else {
                break;
            }
        }

        System.out.println("Доступные роли:");
        for (Role role : Role.values()) {
            System.out.println(role.ordinal() + 1 + ". " + Role.getDisplayName(role));
        }
        int roleChoice = Integer.parseInt(scanner.nextLine());

        Role userRole;
        switch (roleChoice) {
            case 1:
                userRole = Role.ADMIN;
                break;
            case 2:
                userRole = Role.MANAGER;
                break;
            case 3:
                userRole = Role.CLIENT;
                break;
            default:
                System.out.println("\nНеверный выбор роли.");
                return;
        }

        boolean registered = userService.registerUser(login, password, userRole);
        if (registered) {
            System.out.println("\nПользователь добавлен.");
            auditService.logAction(currentUser, "Добавлен новый пользователь: " + login
                    + ", Пароль: " + password);
        } else {
            System.out.println("\nПользователь с таким логином уже существует.");
        }
    }
    private void updateUser(User currentUser) {
        System.out.print("\nВведите ID пользователя для обновления: ");
        String id = scanner.nextLine();
        User user = userService.findById(id);

        if (user != null) {
            // Обновление логина
            System.out.print("Введите новый логин (оставьте пустым для пропуска): ");
            String username = scanner.nextLine().toLowerCase();
            if (!username.isEmpty()) {
                if (!username.chars().allMatch(Character::isDigit)) {
                    user.setUsername(username);
                } else {
                    System.out.println("\nЛогин не должен состоять только из цифр.");
                }
            }

            // Обновление пароля
            System.out.print("Введите новый пароль (оставьте пустым для пропуска): ");
            String password = scanner.nextLine();
            if (!password.isEmpty()) {
                user.setPassword(password);
            }

            // Обновление роли
            if (!currentUser.getId().equals(id)) { // Проверка, чтобы пользователь не менял свою роль
                System.out.println("Выберите новую роль (оставьте пустым для пропуска):");
                Role[] roles = Role.values();
                for (int i = 0; i < roles.length; i++) {
                    System.out.println((i + 1) + ". " + Role.getDisplayName(roles[i]));
                }

                String roleChoiceStr = scanner.nextLine();
                if (!roleChoiceStr.isEmpty()) {
                    try {
                        int roleChoice = Integer.parseInt(roleChoiceStr);
                        if (roleChoice >= 1 && roleChoice <= roles.length) {
                            user.setRole(roles[roleChoice - 1]);
                        } else {
                            System.out.println("\nНеверный выбор. Попробуйте снова.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("\nПожалуйста, введите номер из списка.");
                    }
                }
            } else {
                System.out.println("\nВы не можете изменять свою собственную роль.");
            }

            // Обновление пользователя
            userService.update(user);
            System.out.println("\nИнформация о пользователе обновлена!");
            auditService.logAction(currentUser, "Пользователь изменен " + "\nID: " + id + "\nЛогин: " + username
                    + ",\nПароль: " + password + "\nРоль: " + user.getRole());

        } else {
            System.out.println("\nПользователь не найден.");
        }
    }

    //Интерфейс удаления пользователя
    private void deleteUser(User currentUser) {

        if (currentUser == null) {
            System.out.println("\nОшибка: Не удалось получить информацию о текущем пользователе.");
            return;
        }

        // Получаем список всех пользователей
        List<User> users = userService.getAllUsers();

        // Печатаем список пользователей с индексами
        System.out.println("Список пользователей:\n");
        for (int i = 0; i < users.size(); i++) {
            System.out.println((i + 1) + ". ID: " + users.get(i).getId() + " Логин: " + users.get(i).getUsername()
                    + " Роль: " + users.get(i).getRole());
        }

        System.out.print("\nВведите номер пользователя для удаления: ");
        int userIndex = Integer.parseInt(scanner.nextLine()) - 1; // Преобразуем номер в индекс

        if (userIndex < 0 || userIndex >= users.size()) {
            System.out.println("\nНеверный номер пользователя.");
            return;
        }

        // Получаем пользователя для удаления
        User userToDelete = users.get(userIndex);

        // Проверяем, пытается ли текущий пользователь удалить самого себя
        if (userToDelete.getId().equals(currentUser.getId())) {
            System.out.println("\nВы не можете удалить сами себя.");
            return;
        }

        // Удаляем пользователя из репозитория
        userService.delete(userToDelete.getId());

        // Перенумеруем ID оставшихся пользователей
        users.remove(userIndex); // Удаляем пользователя из списка

        // Обновляем ID оставшихся пользователей
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            String newId = String.valueOf(i + 1); // Новый ID для пользователя
            user.setId(newId);
            userService.update(user); // Обновляем пользователя в репозитории
        }

        // Логируем действие только после успешного завершения всех операций
        auditService.logAction(currentUser, "Удален пользователь: " + "\nID: " + userToDelete.getId()
                + "\nЛогин: " + userToDelete.getUsername()
                + ",\nПароль: " + userToDelete.getPassword() + "\nРоль: " + userToDelete.getRole());

        System.out.println("\nПользователь удален.");
    }
}
