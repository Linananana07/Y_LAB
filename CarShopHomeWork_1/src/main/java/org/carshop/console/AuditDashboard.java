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

/**
 * Класс {@code AuditDashboard} представляет собой интерфейс управления аудитом.
 * <p>
 * Он предоставляет функциональность для просмотра, сортировки и экспорта записей аудита. Также включает в себя методы для навигации по меню и обработки пользовательского ввода.
 * </p>
 */
public class AuditDashboard {
    private UserService userService;
    private CarService carService;
    private OrderService orderService;
    private MainDashboard mainDashboard;
    private AuditService auditService;
    private Scanner scanner = new Scanner(System.in);

    /**
     * Конструктор для создания объекта {@code AuditDashboard}.
     *
     * @param userService сервис для управления пользователями
     * @param carService сервис для управления автомобилями
     * @param orderService сервис для управления заказами
     * @param auditService сервис для управления записями аудита
     * @param mainDashboard основной интерфейс управления
     */
    public AuditDashboard(UserService userService, CarService carService,
                          OrderService orderService, AuditService auditService,
                          MainDashboard mainDashboard) {
        this.userService = userService;
        this.carService = carService;
        this.orderService = orderService;
        this.auditService = auditService;
        this.mainDashboard = mainDashboard;
    }

    /**
     * Запускает интерфейс управления аудитом.
     * <p>
     * Позволяет пользователю просматривать записи аудита, сортировать их по пользователю или дате, экспортировать их в файл или вернуться в главное меню.
     * </p>
     *
     * @param currentUser текущий пользователь
     */
    public void manageAudit(User currentUser) {
        while (true) {
            displayAuditMenu();
            int choice = getValidatedChoice();

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
                    mainDashboard.navigateToDashboard(currentUser);
                    return;
                default:
                    System.out.println("\nНеверный выбор. Попробуйте снова.");
            }
        }
    }

    /**
     * Отображает главное меню управления аудитом.
     */
    private void displayAuditMenu() {
        System.out.println("\nУправление аудитом\n");
        System.out.println("1. Просмотреть все записи аудита");
        System.out.println("2. Сортировка по пользователям");
        System.out.println("3. Сортировка по дате");
        System.out.println("4. Экспортировать журнал действий");
        System.out.println("0. Назад");
        System.out.print("\nВыберите опцию: ");
    }

    /**
     * Просматривает все записи аудита.
     *
     * @param currentUser текущий пользователь
     */
    private void viewAllAuditLogs(User currentUser) {
        List<Audit> logs = auditService.getAuditLogs();
        if (logs.isEmpty()) {
            System.out.println("\nНет записей аудита.");
        } else {
            logs.forEach(this::displayAuditLog);
        }
        waitForUserInputAndReturnToMenu(currentUser);
    }

    /**
     * Сортирует записи аудита по пользователям.
     *
     * @param currentUser текущий пользователь
     */
    private void sortAuditLogsByUsername(User currentUser) {
        List<Audit> logs = auditService.getAuditLogs();
        if (logs.isEmpty()) {
            System.out.println("\nНет записей аудита.");
        } else {
            logs.sort(Comparator.comparing(audit -> audit.getUser().getUsername()));
            logs.forEach(this::displayAuditLog);
        }
        waitForUserInputAndReturnToMenu(currentUser);
    }

    /**
     * Сортирует записи аудита по дате.
     *
     * @param currentUser текущий пользователь
     */
    private void sortAuditLogsByDate(User currentUser) {
        List<Audit> logs = auditService.getAuditLogs();
        if (logs.isEmpty()) {
            System.out.println("\nНет записей аудита.");
        } else {
            logs.sort(Comparator.comparing(Audit::getDate));
            logs.forEach(this::displayAuditLog);
        }
        waitForUserInputAndReturnToMenu(currentUser);
    }

    /**
     * Экспортирует записи аудита в файл.
     * <p>
     * Запрашивает имя файла у пользователя и сохраняет все записи аудита в этот файл.
     * </p>
     */
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

    /**
     * Отображает запись аудита в консоль.
     *
     * @param audit запись аудита
     */
    private void displayAuditLog(Audit audit) {
        System.out.println(formatAuditLog(audit));
    }

    /**
     * Форматирует запись аудита для отображения.
     *
     * @param audit запись аудита
     * @return строка, представляющая запись аудита
     */
    private String formatAuditLog(Audit audit) {
        return "ID: " + audit.getId() + "\n" +
                "Пользователь: " + audit.getUser().getUsername() + "\n" +
                "Действие: " + audit.getAction() + "\n" +
                "Дата: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(audit.getDate()) + "\n";
    }

    /**
     * Ожидает пользовательского ввода и возвращает в меню.
     *
     * @param currentUser текущий пользователь
     */
    private void waitForUserInputAndReturnToMenu(User currentUser) {
        System.out.println("0. Назад");
        System.out.print("\nВыберите опцию: ");
        getValidatedChoice();
        manageAudit(currentUser);
    }

    /**
     * Валидирует выбор пользователя.
     *
     * @return выбор пользователя
     */
    private int getValidatedChoice() {
        int choice;
        while (true) {
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
                return choice;
            } else {
                System.out.println("\nВведите корректное число.");
                scanner.next();
            }
        }
    }
}
