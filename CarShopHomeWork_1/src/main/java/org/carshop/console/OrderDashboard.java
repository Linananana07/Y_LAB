package org.carshop.console;

import org.carshop.model.*;
import org.carshop.services.AuditService;
import org.carshop.services.CarService;
import org.carshop.services.OrderService;
import org.carshop.services.UserService;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Класс для управления заказами, включая создание, обновление, удаление и просмотр всех заказов.
 */
public class OrderDashboard {

    private UserService userService;
    private CarService carService;
    private OrderService orderService;
    private AuditService auditService;
    private MainDashboard mainDashboard;
    private Scanner scanner = new Scanner(System.in);

    /**
     * Конструктор класса OrderDashboard.
     *
     * @param userService сервис для работы с пользователями
     * @param carService сервис для работы с автомобилями
     * @param orderService сервис для работы с заказами
     * @param auditService сервис для работы с аудитом
     * @param mainDashboard основной интерфейс для управления панелями
     */
    public OrderDashboard(UserService userService, CarService carService,
                          OrderService orderService, AuditService auditService,
                          MainDashboard mainDashboard) {
        this.userService = userService;
        this.carService = carService;
        this.orderService = orderService;
        this.auditService = auditService;
        this.mainDashboard = mainDashboard;
    }

    /**
     * Запускает интерфейс управления заказами для текущего пользователя.
     *
     * @param currentUser текущий пользователь
     */
    protected void manageOrders(User currentUser) {
        int choice;
        do {
            displayOrderManagementMenu();
            choice = getValidatedChoice();
            handleOrderManagementChoice(choice, currentUser);
        } while (choice != 0);
    }

    /**
     * Отображает меню управления заказами.
     */
    private void displayOrderManagementMenu() {
        System.out.println("\nУправление заказами\n");
        System.out.println("1. Создать новый заказ");
        System.out.println("2. Обновить статус заказа");
        System.out.println("3. Удалить заказ");
        System.out.println("4. Просмотреть все заказы");
        System.out.println("0. Назад");
        System.out.print("\nВыберите опцию: ");
    }

    /**
     * Считывает и валидирует выбор пользователя.
     *
     * @return выбранная опция
     */
    private int getValidatedChoice() {
        int choice = -1;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("\nВведите корректное число.");
        }
        return choice;
    }

    /**
     * Обрабатывает выбор пользователя в меню управления заказами.
     *
     * @param choice выбранная опция
     * @param currentUser текущий пользователь
     */
    private void handleOrderManagementChoice(int choice, User currentUser) {
        switch (choice) {
            case 1:
                createNewOrder(currentUser);
                break;
            case 2:
                updateOrderStatus(currentUser);
                break;
            case 3:
                deleteOrder(currentUser);
                break;
            case 4:
                viewAllOrders(currentUser);
                break;
            case 0:
                mainDashboard.navigateToDashboard(currentUser);
                break;
            default:
                System.out.println("\nНеверный выбор. Попробуйте снова.");
        }
    }

    /**
     * Создает новый заказ для текущего пользователя.
     *
     * @param currentUser текущий пользователь
     */
    public void createNewOrder(User currentUser) {
        String login = getClientLogin(currentUser);
        User client = userService.findByUsername(login);
        if (client == null) {
            System.out.println("\nКлиент не найден.");
            return;
        }

        String carId = getCarIdFromInput();
        Car car = carService.getCarById(carId);
        if (car == null) {
            System.out.println("\nАвтомобиль не найден.");
            return;
        }

        if (isCarBooked(carId)) {
            System.out.println("\nЭтот автомобиль уже забронирован или продан.");
            return;
        }

        orderService.createOrder(client, car);
        System.out.println("\nЗаказ создан.");
        auditService.logAction(currentUser, "Добавлен заказ под " + login + " на автомобиль под ID: " + carId);
        navigateAfterOrderCreation(currentUser);
    }

    /**
     * Получает логин клиента в зависимости от роли текущего пользователя.
     *
     * @param currentUser текущий пользователь
     * @return логин клиента
     */
    private String getClientLogin(User currentUser) {
        if (currentUser.getRole() == Role.CLIENT) {
            return currentUser.getUsername();
        } else {
            System.out.print("\nВведите логин клиента: ");
            return scanner.nextLine();
        }
    }

    /**
     * Получает ID автомобиля от ввода пользователя.
     *
     * @return ID автомобиля
     */
    private String getCarIdFromInput() {
        System.out.print("Введите ID автомобиля: ");
        return scanner.nextLine();
    }

    /**
     * Проверяет, забронирован ли автомобиль.
     *
     * @param carId ID автомобиля
     * @return true, если автомобиль забронирован, иначе false
     */
    private boolean isCarBooked(String carId) {
        return orderService.getAllOrders().stream()
                .anyMatch(order -> order.getCar().getId().equals(carId)
                        && (order.getStatus() == OrderStatus.PENDING
                        || order.getStatus() == OrderStatus.COMPLETED));
    }

    /**
     * Переходит к соответствующей панели после создания заказа.
     *
     * @param currentUser текущий пользователь
     */
    private void navigateAfterOrderCreation(User currentUser) {
        if (currentUser.getRole() == Role.CLIENT) {
            mainDashboard.clientDashboard(currentUser);
        } else {
            manageOrders(currentUser);
        }
    }

    /**
     * Обновляет статус заказа.
     *
     * @param currentUser текущий пользователь
     */
    private void updateOrderStatus(User currentUser) {
        String orderId = getOrderIdFromInput();
        Order order = findOrderById(orderId);

        if (order != null) {
            displayCurrentOrderStatus(order);
            if (canChangeStatus(order)) {
                OrderStatus newStatus = getNewOrderStatus();
                if (newStatus != null) {
                    updateOrder(order, newStatus, currentUser);
                }
            }
        } else {
            System.out.println("\nЗаказ не найден.");
        }
    }

    /**
     * Получает ID заказа от ввода пользователя.
     *
     * @return ID заказа
     */
    private String getOrderIdFromInput() {
        System.out.print("\nВведите ID заказа для обновления: ");
        return scanner.nextLine();
    }

    /**
     * Находит заказ по его ID.
     *
     * @param id ID заказа
     * @return найденный заказ или null, если заказ не найден
     */
    private Order findOrderById(String id) {
        return orderService.getOrderById(id);
    }

    /**
     * Отображает текущий статус заказа.
     *
     * @param order заказ
     */
    private void displayCurrentOrderStatus(Order order) {
        System.out.println("\nТекущий статус: " + OrderStatus.getDisplayName(order.getStatus()));
    }

    /**
     * Проверяет, можно ли изменить статус заказа.
     *
     * @param order заказ
     * @return true, если статус можно изменить, иначе false
     */
    private boolean canChangeStatus(Order order) {
        if (order.getStatus() == OrderStatus.COMPLETED) {
            System.out.println("\nЗаказ уже завершен. Статус не может быть изменен.");
            return false;
        }
        return true;
    }

    /**
     * Получает новый статус заказа от ввода пользователя.
     *
     * @return новый статус заказа
     */
    private OrderStatus getNewOrderStatus() {
        System.out.println("Выберите новый статус:");
        for (int i = 0; i < OrderStatus.values().length; i++) {
            System.out.println((i + 1) + ". " + OrderStatus.getDisplayName(OrderStatus.values()[i]));
        }

        int choice = -1;
        while (choice < 1 || choice > OrderStatus.values().length) {
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice < 1 || choice > OrderStatus.values().length) {
                    System.out.println("\nНеверный выбор. Попробуйте снова.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\nВведите корректное число.");
            }
        }

        return OrderStatus.values()[choice - 1];
    }

    /**
     * Обновляет статус заказа и сохраняет изменения.
     *
     * @param order заказ
     * @param newStatus новый статус заказа
     * @param currentUser текущий пользователь
     */
    private void updateOrder(Order order, OrderStatus newStatus, User currentUser) {
        if (newStatus == order.getStatus()) {
            System.out.println("\nСтатус заказа уже установлен на: " + OrderStatus.getDisplayName(newStatus));
        } else {
            order.setStatus(newStatus);
            orderService.updateOrder(order);
            if (newStatus == OrderStatus.COMPLETED) {
                updateClientPurchaseCount(order.getClient());
            }
            System.out.println("\nСтатус заказа обновлен на: " + OrderStatus.getDisplayName(newStatus));
            auditService.logAction(currentUser, "Обновлен статус заказа под индексом: "
                    + order.getId() + " на " + newStatus);
        }
    }

    /**
     * Обновляет количество покупок клиента после завершения заказа.
     *
     * @param client клиент
     */
    private void updateClientPurchaseCount(User client) {
        if (client != null) {
            client.setPurchaseCount(client.getPurchaseCount() + 1);
            userService.update(client);
        }
    }

    /**
     * Удаляет заказ.
     *
     * @param currentUser текущий пользователь
     */
    private void deleteOrder(User currentUser) {
        String orderId = getOrderIdFromInput();
        Order orderToDelete = findOrderById(orderId);

        if (orderToDelete != null) {
            auditService.logAction(currentUser, "Удален заказ под индексом "
                    + orderId + "\nКлиент " + orderToDelete.getClient().getUsername()
                    + "\nАвтомобиль под ID " + orderToDelete.getCar().getId()
                    + "\nДата " + orderToDelete.getDate()
                    + "\nСтатус " + orderToDelete.getStatus());
            orderService.deleteOrder(orderId);
            System.out.println("\nЗаказ удален.");
            reindexOrders();
        } else {
            System.out.println("\nЗаказ не найден.");
        }
    }

    /**
     * Изменяем индекс последующих заказов после удаленного.
     *
     */
    private void reindexOrders() {
        List<Order> allOrders = orderService.getAllOrders();
        for (int i = 0; i < allOrders.size(); i++) {
            Order order = allOrders.get(i);
            // Устанавливаем новый индекс в зависимости от текущего порядка
            order.setId(String.valueOf(i + 1));
            orderService.updateOrder(order);
        }
    }

    /**
     * Просматривает все заказы и предоставляет возможность сортировки и фильтрации.
     *
     * @param currentUser текущий пользователь
     */
    private void viewAllOrders(User currentUser) {
        List<Order> orders = orderService.getAllOrders();
        if (orders.isEmpty()) {
            System.out.println("\nНет доступных заказов.");
        } else {
            displayOrders(orders);
            showSortOrBackMenu(currentUser);
        }
    }

    /**
     * Отображает список заказов.
     *
     * @param orders Список заказов для отображения
     */
    private void displayOrders(List<Order> orders) {
        for (Order order : orders) {
            System.out.println("ID: " + order.getId());
            System.out.println("Клиент: " + order.getClient().getUsername());
            System.out.println("Автомобиль: " + order.getCar().getMake() + " " + order.getCar().getModel());
            System.out.println("Дата: " + order.getDate());
            System.out.println("Статус: " + OrderStatus.getDisplayName(order.getStatus()));
            System.out.println();
        }
    }

    /**
     * Отображает меню сортировки заказов или возврата в предыдущее меню.
     *
     * @param currentUser Текущий пользователь
     */
    private void showSortOrBackMenu(User currentUser) {
        int choice;
        do {
            System.out.println("\n1. Отсортировать заказы");
            System.out.println("0. Назад");
            System.out.print("\nВыберите опцию: ");
            choice = getValidatedChoice();
            if (choice == 1) {
                promptSortOrders(currentUser);
            } else if (choice != 0) {
                System.out.println("\nНеверный выбор. Попробуйте снова.");
            }
        } while (choice != 0);
        manageOrders(currentUser);
    }

    /**
     * Запрашивает у пользователя выбор параметров сортировки заказов.
     *
     * @param currentUser Текущий пользователь
     */
    private void promptSortOrders(User currentUser) {
        int choice;
        do {
            System.out.println("\nФильтрация и сортировка заказов\n");
            System.out.println("1. Сортировка по дате");
            System.out.println("2. Сортировка по клиенту");
            System.out.println("3. Сортировка по статусу");
            System.out.println("0. Назад");
            System.out.print("\nВыберите опцию: ");
            choice = getValidatedChoice();
            handleSortChoice(choice, currentUser);
        } while (choice != 0);
    }

    /**
     * Обрабатывает выбор пользователя для сортировки заказов.
     *
     * @param choice      Выбранная пользователем опция
     * @param currentUser Текущий пользователь
     */
    private void handleSortChoice(int choice, User currentUser) {
        switch (choice) {
            case 1:
                sortByDate(currentUser);
                break;
            case 2:
                sortByClient(currentUser);
                break;
            case 3:
                sortByStatus(currentUser);
                break;
            case 0:
                viewAllOrders(currentUser);
                break;
            default:
                System.out.println("\nНеверный выбор. Попробуйте снова.");
        }
    }

    /**
     * Сортирует заказы по дате.
     *
     * @param currentUser Текущий пользователь
     */
    private void sortByDate(User currentUser) {
        List<Order> sortedOrders = orderService.getAllOrders().stream()
                .sorted(Comparator.comparing(Order::getDate))
                .collect(Collectors.toList());
        displayOrders(sortedOrders);
        showSortOrBackMenu(currentUser);
    }

    /**
     * Сортирует заказы по клиенту.
     *
     * @param currentUser Текущий пользователь
     */
    public  void sortByClient(User currentUser) {
        List<Order> sortedOrders = orderService.getAllOrders().stream()
                .sorted(Comparator.comparing(order -> order.getClient().getUsername()))
                .collect(Collectors.toList());
        displayOrders(sortedOrders);
        showSortOrBackMenu(currentUser);
    }

    /**
     * Сортирует заказы по ID клиента.
     *
     * @param currentUser Текущий пользователь
     */
    public void filterByClientId(User currentUser) {
        List<Order> filterOrder = orderService.getAllOrders().stream()
                .filter(order -> order.getClient().getId().equals(currentUser.getId()))
                .collect(Collectors.toList());

        if (filterOrder.isEmpty()) {
            System.out.println("Заказов не найдено");
        } else {
            displayOrders(filterOrder);
        }
    }

    /**
     * Сортирует заказы по статусу.
     *
     * @param currentUser Текущий пользователь
     */
    private void sortByStatus(User currentUser) {
        List<Order> sortedOrders = orderService.getAllOrders().stream()
                .sorted(Comparator.comparing(Order::getStatus))
                .collect(Collectors.toList());
        displayOrders(sortedOrders);
        showSortOrBackMenu(currentUser);
    }
}
