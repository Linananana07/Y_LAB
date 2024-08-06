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

public class OrderDashboard {

    private UserService userService;
    private CarService carService;
    private OrderService orderService;
    private AuditService auditService;
    private MainDashboard mainDashboard;
    private Scanner scanner = new Scanner(System.in);

    public OrderDashboard(UserService userService, CarService carService,
                        OrderService orderService, AuditService auditService,
                        MainDashboard mainDashboard) {
        this.userService = userService;
        this.carService = carService;
        this.orderService = orderService;
        this.auditService = auditService;
        this.mainDashboard = mainDashboard;
    }

    //Интерфейс управления заказами
    protected void manageOrders(User currentUser) {
        int choice = -1;
        while (true) {
            System.out.println("\nУправление заказами\n");
            System.out.println("1. Создать новый заказ");
            System.out.println("2. Обновить статус заказа");
            System.out.println("3. Удалить заказ");
            System.out.println("4. Просмотреть все заказы");
            System.out.println("0. Назад");
            System.out.print("\nВыберите опцию: ");

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("\nВведите корректное число.");
            }

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
                    mainDashboard.goToMainDashboard(currentUser);
                    return;
                default:
                    System.out.println("\nНеверный выбор. Попробуйте снова.");
            }
        }
    }

    //Интерфейс создания заказов
    public void createNewOrder(User currentUser) {

        String login;

        if (currentUser.getRole() == Role.CLIENT) {
            login = currentUser.getUsername();
        } else {
            System.out.print("\nВведите логин клиента: ");
            login = scanner.nextLine();
        }
        User client = userService.findByUsername(login);
        if (client == null) {
            System.out.println("\nКлиент не найден.");
            return;
        }

        System.out.print("Введите ID автомобиля: ");
        String carId = scanner.nextLine();
        Car car = carService.getCarById(carId);
        if (car == null) {
            System.out.println("\nАвтомобиль не найден.");
            return;
        }

        // Проверка на существование активных заказов с статусом PENDING или COMPLETED
        boolean carIsBooked = orderService.getAllOrders().stream()
                .anyMatch(order -> order.getCar().getId().equals(carId)
                        && (order.getStatus() == OrderStatus.PENDING
                        || order.getStatus() == OrderStatus.COMPLETED));

        if (carIsBooked) {
            System.out.println("\nЭтот автомобиль уже забронирован или продан.");
            return;
        }

        orderService.createOrder(client, car);
        System.out.println("\nЗаказ создан.");

        auditService.logAction(currentUser, "Добавлен заказ под " + login + " на автомобиль под ID: " + carId);
        System.out.println("\nАвтомобиль добавлен!");
        if (currentUser.getRole() == Role.CLIENT) {
            mainDashboard.clientDashboard(currentUser);
        } else {
            manageOrders(currentUser);
        }
    }

    private void updateOrderStatus(User currentUser) {
        System.out.print("\nВведите ID заказа для обновления: ");
        String id = scanner.nextLine();
        Order order = findOrderById(id);

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

    private Order findOrderById(String id) {
        return orderService.getOrderById(id);
    }

    private void displayCurrentOrderStatus(Order order) {
        System.out.println("\nТекущий статус: " + OrderStatus.getDisplayName(order.getStatus()));
    }

    private boolean canChangeStatus(Order order) {
        if (order.getStatus() == OrderStatus.COMPLETED) {
            System.out.println("\nЗаказ уже завершен. Статус не может быть изменен.");
            return false;
        }
        return true;
    }

    private OrderStatus getNewOrderStatus() {
        System.out.println("Выберите новый статус:");
        int index = 1;
        for (OrderStatus status : OrderStatus.values()) {
            System.out.println(index + ". " + OrderStatus.getDisplayName(status));
            index++;
        }

        int choice = -1;
        while (choice < 1 || choice > OrderStatus.values().length) {
            System.out.print("\nВведите номер статуса: ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice < 1 || choice > OrderStatus.values().length) {
                    System.out.println("\nНеверный выбор. Попробуйте снова.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\nВведите корректный номер.");
            }
        }

        return OrderStatus.values()[choice - 1];
    }

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

    private void updateClientPurchaseCount(User client) {
        if (client != null) {
            client.setPurchaseCount(client.getPurchaseCount() + 1);
            userService.update(client);
        }
    }

    //Интерфейс удаления заказов
    private void deleteOrder(User currentUser) {
        System.out.print("\nВведите ID заказа для удаления: ");
        String id = scanner.nextLine();
        Order orderToDelete = orderService.getOrderById(id);

        if (orderToDelete != null) {
            // Удаление заказа
            auditService.logAction(currentUser, "Удален заказ под индексом "
                    + id + "\nКлиент " + orderToDelete.getClient() + "\nАвтомобиль " + orderToDelete.getCar()
                    + "\nДата " + orderToDelete.getDate() + "\nСтатус " + orderToDelete.getStatus());
            orderService.deleteOrder(id);
            System.out.println("\nЗаказ удален.");

            // Переиндексация оставшихся заказов
            List<Order> allOrders = orderService.getAllOrders();
            List<Order> updatedOrders = new ArrayList<>();

            // Перенумерация заказов
            for (int i = 0; i < allOrders.size(); i++) {
                Order order = allOrders.get(i);
                // Устанавливаем новый ID для заказа (например, на основе его текущего индекса)
                order.setId(String.valueOf(i + 1));
                updatedOrders.add(order);
            }

            // Сохранение обновленных заказов
            for (Order updatedOrder : updatedOrders) {
                orderService.updateOrder(updatedOrder);
            }
        } else {
            System.out.println("\nЗаказ не найден.");
        }
    }

    //Интерфейс всех заказов
    private void viewAllOrders(User currentUser) {
        int choice = -1;
        List<Order> orders = orderService.getAllOrders();
        if (orders.isEmpty()) {
            System.out.println("\nНет доступных заказов.");
        } else {
            for (Order order : orders) {
                System.out.println("ID: " + order.getId());
                System.out.println("Клиент: " + order.getClient().getUsername());
                System.out.println("Автомобиль: " + order.getCar().getMake() + " " + order.getCar().getModel());
                System.out.println("Дата: " + order.getDate());
                System.out.println("Статус: " + order.getStatus());
                System.out.println();
            }
        }
        while (true) {
            System.out.println("\n1. Отсортировать");
            System.out.println("0. Назад");
            System.out.print("\nВыберите опцию: ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("\nВведите корректное число.");
            }
            switch (choice) {
                case 1:
                    sortOrders(currentUser);
                case 0:
                    manageOrders(currentUser);
                default:
                    System.out.println("\nНеверный выбор. Попробуйте снова.");
            }
        }
    }
    // Сортировка заказов
    private void sortOrders(User currentUser) {
        System.out.println("\nФильтрация и сортировка заказов\n");
        System.out.println("1. Сортировка по дате");
        System.out.println("2. Сортировка по клиенту");
        System.out.println("3. Сортировка по статусу");
        System.out.println("3. Сортировка по марке машины");
        System.out.print("\nВыберите опцию: ");

        int choice = -1;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("\nВведите корректное число.");
        }

        List<Order> sortedOrders = new ArrayList<>(orderService.getAllOrders());
        switch (choice) {
            case 1:
                sortedOrders.sort(Comparator.comparing(Order::getDate));
                break;
            case 2:
                sortedOrders.sort(Comparator.comparing(o -> o.getClient().getUsername()));
                break;
            case 3:
                sortedOrders.sort(Comparator.comparing(Order::getStatus));
                break;
            case 4:
                sortedOrders.sort(Comparator.comparing(o -> o.getCar().getMake()));
                break;
            default:
                System.out.println("\nНеверный выбор. Попробуйте снова.");
                return;
        }

        displayOrders(currentUser, sortedOrders);
    }

    // Отображение списка заказов
    public void displayOrders(User currentUser, List<Order> orders) {
        int choice = -1;
        for (Order order : orders) {
            System.out.println("ID: " + order.getId());
            System.out.println("Клиент: " + order.getClient().getUsername());
            System.out.println("Автомобиль: " + order.getCar().getMake() + " " + order.getCar().getModel());
            System.out.println("Дата: " + order.getDate());
            System.out.println("Статус: " + OrderStatus.getDisplayName(order.getStatus()));
            System.out.println();
        }
        while (true) {
            System.out.println("0. Назад");
            System.out.print("\nВыберите опцию: ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("\nВведите корректное число.");
            }
            switch (choice) {
                case 0:
                    if (currentUser.getRole() == Role.CLIENT) {
                        mainDashboard.clientDashboard(currentUser);
                    } else {
                        manageOrders(currentUser);
                    }
                default:
                    System.out.println("\nНеверный выбор. Попробуйте снова.");
            }
        }
    }
}
