package org.carshop.console;

import org.carshop.model.Audit;
import org.carshop.model.User;
import org.carshop.services.AuditService;
import org.carshop.services.CarService;
import org.carshop.services.OrderService;
import org.carshop.services.UserService;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class AuditDashboard {
    private UserService userService;
    private CarService carService;
    private OrderService orderService;
    private MainDashboard mainDashboard;
    private AuditService auditService;
    private Scanner scanner = new Scanner(System.in);

    public AuditDashboard(UserService userService, CarService carService,
                        OrderService orderService, AuditService auditService,
                        MainDashboard mainDashboard) {
        this.userService = userService;
        this.carService = carService;
        this.orderService = orderService;
        this.auditService = auditService;
        this.mainDashboard = mainDashboard;
    }


    // Интерфейс управления аудитом
    public void manageAudit(User currentUser) {
        int choice;
        while (true) {
            System.out.println("\nУправление аудитом\n");
            System.out.println("1. Просмотреть все записи аудита");
            System.out.println("2. Сортировка по пользователям");
            System.out.println("3. Сортировка по дате");
            System.out.println("4. Экспортировать журнал действий");
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
                    viewAllAuditLogs(currentUser);
                    break;
                case 2:
                    sortAuditLogsByUsername(currentUser);
                    break;
                case 3:
                    sortAuditLogsByDate(currentUser);
                    break;
                case 4:
                    exportAuditLogs();
                    break;
                case 0:
                    mainDashboard.goToMainDashboard(currentUser);
                    return;
                default:
                    System.out.println("\nНеверный выбор. Попробуйте снова.");
            }
        }
    }

    // Просмотр всех записей аудита
    private void viewAllAuditLogs(User currentUser) {

        int choice;

        List<Audit> logs = auditService.getAuditLogs();
        if (logs.isEmpty()) {
            System.out.println("\nНет записей аудита.");
        } else {
            logs.forEach(this::displayAuditLog);
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
                    manageAudit(currentUser);
                default:
                    System.out.println("\nНеверный выбор. Попробуйте снова.");
            }
        }
    }

    // Фильтрация по пользователю
    private void sortAuditLogsByUsername(User currentUser) {

        int choice;
        List<Audit> logs = auditService.getAuditLogs();
        if (logs.isEmpty()) {
            System.out.println("\nНет записей аудита.");
        } else {
            logs.sort(Comparator.comparing(audit -> audit.getUser().getUsername()));
            logs.forEach(this::displayAuditLog);
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
                    manageAudit(currentUser);
                default:
                    System.out.println("\nНеверный выбор. Попробуйте снова.");
            }
        }
    }

    // Фильтрация по дате
    private void sortAuditLogsByDate(User currentUser) {
        int choice;
        List<Audit> logs = auditService.getAuditLogs();
        if (logs.isEmpty()) {
            System.out.println("\nНет записей аудита.");
        } else {
            logs.sort(Comparator.comparing(Audit::getDate));
            logs.forEach(this::displayAuditLog);
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
                    manageAudit(currentUser);
                default:
                    System.out.println("\nНеверный выбор. Попробуйте снова.");
            }
        }
    }

    // Экспорт журнала действий
    private void exportAuditLogs() {
        System.out.print("\nВведите имя файла для экспорта: ");
        String fileName = scanner.nextLine();
        try (FileWriter writer = new FileWriter(fileName)) {
            List<Audit> logs = auditService.getAuditLogs();
            for (Audit audit : logs) {
                writer.write(formatAuditLog(audit) + "\n");
            }
            System.out.println("\nЖурнал действий экспортирован в файл: " + fileName);
        } catch (IOException e) {
            System.out.println("\nОшибка при экспорте журнала действий.");
        }
    }

    // Отображение записи аудита
    private void displayAuditLog(Audit audit) {
        System.out.println(formatAuditLog(audit));
    }

    // Форматирование записи аудита для отображения
    private String formatAuditLog(Audit audit) {
        return "ID: " + audit.getId() + "\n" +
                "Пользователь: " + audit.getUser().getUsername() + "\n" +
                "Действие: " + audit.getAction() + "\n" +
                "Дата: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(audit.getDate()) + "\n";
    }
}
