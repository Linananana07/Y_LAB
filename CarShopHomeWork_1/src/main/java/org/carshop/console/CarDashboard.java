package org.carshop.console;

import org.carshop.model.Car;
import org.carshop.model.ConditionCar;
import org.carshop.model.User;
import org.carshop.services.AuditService;
import org.carshop.services.CarService;
import org.carshop.services.OrderService;
import org.carshop.services.UserService;
import org.carshop.model.Role;

import java.util.List;
import java.util.Scanner;


/**
 * Класс {@code CarDashboard} представляет интерфейс управления автомобилями в системе.
 * Он предоставляет методы для добавления, обновления, удаления и просмотра автомобилей,
 * а также для сортировки и фильтрации.
 * Класс взаимодействует с {@code UserService}, {@code CarService}, {@code OrderService},
 * {@code AuditService} и {@code MainDashboard}.
 */
public class CarDashboard {

    private UserService userService;
    private CarService carService;
    private OrderService orderService;
    private AuditService auditService;
    private MainDashboard mainDashboard;
    private Scanner scanner = new Scanner(System.in);


    /**
     * Конструктор класса {@code CarDashboard}.
     *
     * @param userService сервис для управления пользователями
     * @param carService сервис для управления автомобилями
     * @param orderService сервис для управления заказами
     * @param auditService сервис для управления аудитом
     * @param mainDashboard основной интерфейс панели управления
     */
    public CarDashboard(UserService userService, CarService carService,
                        OrderService orderService, AuditService auditService,
                        MainDashboard mainDashboard) {
        this.userService = userService;
        this.carService = carService;
        this.orderService = orderService;
        this.auditService = auditService;
        this.mainDashboard = mainDashboard;
    }

    /**
     * Запускает интерфейс управления автомобилями.
     *
     * @param currentUser текущий пользователь
     */
    public void manageCars(User currentUser) {
        int choice;
        while (true) {
            displayCarManagementMenu();
            choice = mainDashboard.getUserChoice();
            handleCarManagementChoice(choice, currentUser);
        }
    }


    /**
     * Отображает меню управления автомобилями.
     */
    private void displayCarManagementMenu() {
        System.out.println("\nУправление автомобилями\n");
        System.out.println("1. Добавить новый автомобиль");
        System.out.println("2. Обновить информацию об автомобиле");
        System.out.println("3. Удалить автомобиль");
        System.out.println("4. Просмотреть все автомобили");
        System.out.println("0. Назад");
        System.out.print("\nВыберите опцию: ");
    }

    /**
     * Обрабатывает выбор пользователя из меню управления автомобилями.
     *
     * @param choice выбор пользователя
     * @param currentUser текущий пользователь
     */
    private void handleCarManagementChoice(int choice, User currentUser) {
        switch (choice) {
            case 1:
                addNewCar(currentUser);
                break;
            case 2:
                updateCar(currentUser);
                break;
            case 3:
                deleteCar(currentUser);
                break;
            case 4:
                viewAllCars(currentUser);
                break;
            case 0:
                mainDashboard.navigateToDashboard(currentUser);
                break;
            default:
                System.out.println("\nНеверный выбор. Попробуйте снова.");
        }
    }

    /**
     * Добавляет новый автомобиль в систему.
     *
     * @param currentUser текущий пользователь
     */
    private void addNewCar(User currentUser) {
        String make = getValidMarkAndModelInput("Введите марку автомобиля: ", "Марка автомобиля не должна состоять только из цифр.");
        String model = getValidMarkAndModelInput("Введите модель автомобиля: ", "Модель автомобиля не должна состоять только из цифр.");
        int year = getYearInput("Введите год выпуска автомобиля: ", "Год выпуска должен быть положительным числом.");
        double price = getPriceInput("Введите цену автомобиля: ", "Цена автомобиля не может быть отрицательной.");
        ConditionCar condition = getCarCondition();

        carService.addCar(make, model, year, price, condition.name());
        auditService.logAction(currentUser, "Добавлен автомобиль " + make + " " + model
                + " " + year + " " + price + " " + condition.name());
        System.out.println("\nАвтомобиль добавлен!");
    }

    /**
     * Получает и проверяет входное значение от пользователя.
     *
     * @param prompt сообщение для запроса ввода
     * @param errorMessage сообщение об ошибке, если ввод недействителен
     * @return валидное значение ввода
     */
    private String getValidMarkAndModelInput(String prompt, String errorMessage) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine();
            if (input.isEmpty() || input.chars().allMatch(Character::isDigit)) {
                System.out.println("\n" + errorMessage);
            }
        } while (input.isEmpty() || input.chars().allMatch(Character::isDigit));
        return input;
    }

    /**
     * Получает положительное целое значение от пользователя.
     *
     * @param prompt сообщение для запроса ввода
     * @param errorMessage сообщение об ошибке, если ввод недействителен
     * @return валидное положительное целое значение
     */
    private int getYearInput(String prompt, String errorMessage) {
        int value;
        while (true) {
            try {
                System.out.print(prompt);
                value = Integer.parseInt(scanner.nextLine());
                if (value <= 0) {
                    System.out.println("\n" + errorMessage);
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("\n" + errorMessage);
            }
        }
        return value;
    }

    /**
     * Получает положительное значение с плавающей точкой от пользователя.
     *
     * @param prompt сообщение для запроса ввода
     * @param errorMessage сообщение об ошибке, если ввод недействителен
     * @return валидное положительное значение с плавающей точкой
     */
    private double getPriceInput(String prompt, String errorMessage) {
        double value;
        while (true) {
            try {
                System.out.print(prompt);
                value = Double.parseDouble(scanner.nextLine());
                if (value < 0) {
                    System.out.println("\n" + errorMessage);
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("\n" + errorMessage);
            }
        }
        return value;
    }

    /**
     * Получает состояние автомобиля от пользователя.
     *
     * @return выбранное состояние автомобиля
     */
    private ConditionCar getCarCondition() {
        System.out.println("Выберите состояние автомобиля:");
        ConditionCar[] conditions = ConditionCar.values();
        for (int i = 0; i < conditions.length; i++) {
            System.out.println(i + 1 + ". " + ConditionCar.getDisplayName(conditions[i]));
        }

        int conditionChoice;
        while (true) {
            try {
                System.out.print("Введите номер состояния: ");
                conditionChoice = Integer.parseInt(scanner.nextLine());
                if (conditionChoice < 1 || conditionChoice > conditions.length) {
                    System.out.println("\nНеверный выбор. Попробуйте снова.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("\nПожалуйста, введите номер из списка.");
            }
        }
        return conditions[conditionChoice - 1];
    }

    /**
     * Обновляет информацию об автомобиле.
     *
     * @param currentUser текущий пользователь
     */
    private void updateCar(User currentUser) {
        System.out.print("\nВведите ID автомобиля для обновления: ");
        String id = scanner.nextLine();
        Car car = carService.getCarById(id);
        if (car != null) {
            updateCarDetails(car);
            carService.updateCar(car);
            auditService.logAction(currentUser, "Обновлен автомобиль под ID " + id + "\nМарка " + car.getMake()
                    + "\nМодель " + car.getModel() + "\nГод выпуска " + car.getYear() + "\nЦена " + car.getPrice() +
                    "\nСостояние " + car.getCondition());
        } else {
            System.out.println("\nАвтомобиль не найден.");
        }
    }

    /**
     * Обновляет детали автомобиля.
     *
     * @param car автомобиль для обновления
     */
    private void updateCarDetails(Car car) {
        updateCarMake(car);
        updateCarModel(car);
        updateCarYear(car);
        updateCarPrice(car);
        updateCarCondition(car);
    }

    /**
     * Обновляет марку автомобиля.
     *
     * @param car автомобиль для обновления
     */
    private void updateCarMake(Car car) {
        System.out.print("Введите новую марку автомобиля (оставьте пустым для пропуска): ");
        String make = scanner.nextLine();
        if (!make.isEmpty() && !make.chars().allMatch(Character::isDigit)) {
            car.setMake(make);
        } else if (!make.isEmpty()) {
            System.out.println("\nМарка автомобиля не должна состоять только из цифр.");
        }
    }

    /**
     * Обновляет модель автомобиля.
     *
     * @param car автомобиль для обновления
     */
    private void updateCarModel(Car car) {
        System.out.print("Введите новую модель автомобиля (оставьте пустым для пропуска): ");
        String model = scanner.nextLine();
        if (!model.isEmpty() && !model.chars().allMatch(Character::isDigit)) {
            car.setModel(model);
        } else if (!model.isEmpty()) {
            System.out.println("\nМодель автомобиля не должна состоять только из цифр.");
        }
    }

    /**
     * Обновляет год выпуска автомобиля.
     *
     * @param car автомобиль для обновления
     */
    private void updateCarYear(Car car) {
        while (true) {
            System.out.print("Введите новый год выпуска автомобиля (оставьте пустым для пропуска): ");
            String yearInput = scanner.nextLine();
            if (yearInput.isEmpty()) {
                break;
            }
            try {
                int year = Integer.parseInt(yearInput);
                if (year > 0) {
                    car.setYear(year);
                    break;
                } else {
                    System.out.println("\nГод выпуска должен быть положительным числом.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\nГод выпуска должен быть числом.");
            }
        }
    }

    /**
     * Обновляет цену автомобиля.
     *
     * @param car автомобиль для обновления
     */
    private void updateCarPrice(Car car) {
        while (true) {
            System.out.print("Введите новую цену автомобиля (оставьте пустым для пропуска): ");
            String priceInput = scanner.nextLine();
            if (priceInput.isEmpty()) {
                break;
            }
            try {
                double price = Double.parseDouble(priceInput);
                if (price >= 0) {
                    car.setPrice(price);
                    break;
                } else {
                    System.out.println("\nЦена автомобиля не может быть отрицательной.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\nЦена автомобиля должна быть числом.");
            }
        }
    }

    /**
     * Обновляет состояние автомобиля.
     *
     * @param car автомобиль для обновления
     */
    private void updateCarCondition(Car car) {
        System.out.println("Выберите новое состояние автомобиля (оставьте пустым для пропуска):");
        ConditionCar[] conditions = ConditionCar.values();
        for (int i = 0; i < conditions.length; i++) {
            System.out.println(i + 1 + ". " + ConditionCar.getDisplayName(conditions[i]));
        }

        while (true) {
            System.out.print("Введите номер состояния (оставьте пустым для пропуска): ");
            String conditionInput = scanner.nextLine();
            if (conditionInput.isEmpty()) {
                break;
            }
            try {
                int conditionChoice = Integer.parseInt(conditionInput);
                if (conditionChoice >= 1 && conditionChoice <= conditions.length) {
                    car.setCondition(conditions[conditionChoice - 1].name());
                    break;
                } else {
                    System.out.println("\nНеверный выбор. Попробуйте снова.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\nПожалуйста, введите номер из списка.");
            }
        }
    }

    /**
     * Удаляет автомобиль из системы.
     *
     * @param currentUser текущий пользователь
     */
    private void deleteCar(User currentUser) {
        System.out.print("\nВведите ID автомобиля для удаления: ");
        String id = scanner.nextLine();
        Car car = carService.getCarById(id);
        if (car != null) {
            carService.deleteCar(id);
            auditService.logAction(currentUser, "Удален автомобиль " + car.getMake()
                    + " " + car.getModel() + " " + car.getYear() + " " + car.getPrice()
                    + " " + car.getCondition());
            System.out.println("\nАвтомобиль удален.");
            carService.updateCarIndices();
        } else {
            System.out.println("\nАвтомобиль не найден.");
        }
    }

    /**
     * Просматривает все автомобили в системе.
     *
     * @param currentUser текущий пользователь
     */
    public void viewAllCars(User currentUser) {
        List<Car> cars = carService.getAllCars();
        if (cars.isEmpty()) {
            System.out.println("\nНет доступных автомобилей.");
        } else {
            displayCars(cars);
            showSortOrBackMenu(currentUser);
        }
    }

    /**
     * Отображает список всех автомобилей.
     *
     * @param cars список автомобилей для отображения.
     */
    private void displayCars(List<Car> cars) {
        System.out.println("\nСписок всех автомобилей:");
        for (Car car : cars) {
            System.out.println("ID: " + car.getId());
            System.out.println("Марка: " + car.getMake());
            System.out.println("Модель: " + car.getModel());
            System.out.println("Год выпуска: " + car.getYear());
            System.out.println("Цена: " + car.getPrice());
            System.out.println("Состояние: " + ConditionCar.getDisplayName(ConditionCar.valueOf(car.getCondition())));
            System.out.println();
        }
    }

    /**
     * Показывает меню сортировки или возврата.
     * Меню отображается до тех пор, пока пользователь не выберет корректный вариант.
     *
     * @param currentUser текущий пользователь.
     */
    private void showSortOrBackMenu(User currentUser) {
        while (true) {
            System.out.println("\n1. Сортировать список");
            System.out.println("0. Назад");
            System.out.print("\nВыберите опцию: ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("\nВведите корректное число.");
                continue;
            }

            switch (choice) {
                case 1:
                    handleSortAndFilterOptions(currentUser);
                    return;
                case 0:
                    if (!currentUser.getRole().equals(Role.CLIENT)) {
                        manageCars(currentUser);
                        break;
                    }
                    mainDashboard.navigateToDashboard(currentUser);
                    return;
                default:
                    System.out.println("\nНеверный выбор. Попробуйте снова.");
            }
        }
    }

    /**
     * Обрабатывает опции сортировки и фильтрации.
     * Пользователь может выбрать один из вариантов фильтрации или сортировки.
     *
     * @param currentUser текущий пользователь.
     */
    private void handleSortAndFilterOptions(User currentUser) {
        while (true) {
            displaySortAndFilterMenu();
            int choice = mainDashboard.getUserChoice();
            switch (choice) {
                case 1:
                    filterCarsByMake();
                    break;
                case 2:
                    filterCarsByModel();
                    break;
                case 3:
                    filterCarsByYear();
                    break;
                case 4:
                    sortCarsByPrice();
                    break;
                case 0:
                    if (currentUser.getRole().equals(Role.CLIENT)) {
                        mainDashboard.clientDashboard(currentUser);
                        break;
                    }
                    manageCars(currentUser);
                    return;
                default:
                    System.out.println("\nНеверный выбор. Попробуйте снова.");
            }
        }
    }

    /**
     * Отображает меню сортировки и фильтрации автомобилей.
     */
    private void displaySortAndFilterMenu() {
        System.out.println("\nОпции сортировки и фильтрации\n");
        System.out.println("1. Фильтровать по марке");
        System.out.println("2. Фильтровать по модели");
        System.out.println("3. Фильтровать по году выпуска");
        System.out.println("4. Сортировать по цене");
        System.out.println("0. Назад");
        System.out.print("\nВыберите опцию: ");
    }

    /**
     * Фильтрует автомобили по марке и отображает результат.
     */
    private void filterCarsByMake() {
        System.out.print("Введите марку автомобиля для фильтрации: ");
        String make = scanner.nextLine();
        List<Car> filteredCars = carService.getCarsByMake(make);
        if (filteredCars.isEmpty()) {
            System.out.println("\nНет автомобилей с маркой " + make + ".");
        } else {
            displayCars(filteredCars);
        }
    }

    /**
     * Фильтрует автомобили по модели и отображает результат.
     */
    private void filterCarsByModel() {
        System.out.print("Введите модель автомобиля для фильтрации: ");
        String model = scanner.nextLine();
        List<Car> filteredCars = carService.getCarsByModel(model);
        if (filteredCars.isEmpty()) {
            System.out.println("\nНет автомобилей с моделью " + model + ".");
        } else {
            displayCars(filteredCars);
        }
    }

    /**
     * Фильтрует автомобили по году выпуска и отображает результат.
     */
    private void filterCarsByYear() {
        int year = getYearInput("Введите год выпуска для фильтрации: ", "Год выпуска должен быть положительным числом.");
        List<Car> filteredCars = carService.getCarsByYear(year);
        if (filteredCars.isEmpty()) {
            System.out.println("\nНет автомобилей с годом выпуска " + year + ".");
        } else {
            displayCars(filteredCars);
        }
    }

    /**
     * Сортирует автомобили по цене и отображает результат.
     * Пользователь может выбрать сортировку по возрастанию или убыванию.
     */
    private void sortCarsByPrice() {
        System.out.println("\nСортировка автомобилей по цене\n");
        System.out.println("1. По возрастанию");
        System.out.println("2. По убыванию");
        System.out.print("\nВыберите опцию: ");
        int choice = mainDashboard.getUserChoice();
        List<Car> sortedCars;
        switch (choice) {
            case 1:
                sortedCars = carService.sortCarsByPrice(true);
                displayCars(sortedCars);
                break;
            case 2:
                sortedCars = carService.sortCarsByPrice(false);
                displayCars(sortedCars);
                break;
            default:
                System.out.println("\nНеверный выбор. Попробуйте снова.");
        }
    }
}
