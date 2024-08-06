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

public class CarDashboard {

    private UserService userService;
    private CarService carService;
    private OrderService orderService;
    private AuditService auditService;
    private MainDashboard mainDashboard;
    private Scanner scanner = new Scanner(System.in);

    public CarDashboard(UserService userService, CarService carService,
                        OrderService orderService, AuditService auditService,
                        MainDashboard mainDashboard) {
        this.userService = userService;
        this.carService = carService;
        this.orderService = orderService;
        this.auditService = auditService;
        this.mainDashboard = mainDashboard;
    }

    //Интерфейс управления автомобилями
    public void manageCars(User currentUser) {

        int choice = -1;
        while (true) {
            System.out.println("\nУправление автомобилями\n");
            System.out.println("1. Добавить новый автомобиль");
            System.out.println("2. Обновить информацию об автомобиле");
            System.out.println("3. Удалить автомобиль");
            System.out.println("4. Просмотреть все автомобили");
            System.out.println("0. Назад");
            System.out.print("\nВыберите опцию: ");

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("\nВведите корректное число.");
            }

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
                    mainDashboard.goToMainDashboard(currentUser);
                default:
                    System.out.println("\nНеверный выбор. Попробуйте снова.");
            }
        }
    }

    //Интерфейс добавления автомобиля
    private void addNewCar(User currentUser) {
        String make;
        String model;
        int year;
        double price;
        int conditionChoice;

        do {
            System.out.print("\nВведите марку автомобиля: ");
            make = scanner.nextLine();
            if (make.isEmpty() || make.chars().allMatch(Character::isDigit)) {
                System.out.println("\nМарка автомобиля не должна состоять только из цифр. Попробуйте снова.");
            }
        } while (make.isEmpty() || make.chars().allMatch(Character::isDigit));

        do {
            System.out.print("Введите модель автомобиля: ");
            model = scanner.nextLine();
            if (model.isEmpty() || model.chars().allMatch(Character::isDigit)) {
                System.out.println("\nМодель автомобиля не должна состоять только из цифр. Попробуйте снова.");
            }
        } while (model.isEmpty() || model.chars().allMatch(Character::isDigit));

        while (true) {
            try {
                System.out.print("Введите год выпуска автомобиля: ");
                year = Integer.parseInt(scanner.nextLine());
                if (year <= 0) {
                    System.out.println("\nГод выпуска должен быть положительным числом. Попробуйте снова.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("\nГод выпуска должен состоять только из цифр. Попробуйте снова.");
            }
        }

        while (true) {
            try {
                System.out.print("Введите цену автомобиля: ");
                price = Double.parseDouble(scanner.nextLine());
                if (price < 0) {
                    System.out.println("\nЦена автомобиля не может быть отрицательной. Попробуйте снова.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("\nЦена автомобиля должна быть числовым значением. Попробуйте снова.");
            }
        }

        System.out.println("Выберите состояние автомобиля:");
        ConditionCar[] conditions = ConditionCar.values();
        for (int i = 0; i < conditions.length; i++) {
            System.out.println(i + 1 + ". " + ConditionCar.getDisplayName(conditions[i]));
        }

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

        ConditionCar condition = conditions[conditionChoice - 1];

        carService.addCar(make, model, year, price, condition.name());
        auditService.logAction(currentUser, "Добавлен автомобиль " + make + " " + model
                + " " + year + " " + price + " " + condition.name());
        System.out.println("\nАвтомобиль добавлен!");
    }

    //Интерфейс изменения автомобиля
    private void updateCar(User currentUser) {
        System.out.print("\nВведите ID автомобиля для обновления: ");
        String id = scanner.nextLine();
        Car car = carService.getCarById(id);
        if (car != null) {
            System.out.print("Введите новую марку автомобиля (оставьте пустым для пропуска): ");
            String make = scanner.nextLine();
            if (!make.isEmpty()) {
                if (!make.chars().allMatch(Character::isDigit)) {
                    car.setMake(make);
                } else {
                    System.out.println("\nМарка автомобиля не должна состоять только из цифр.");
                }
            }

            System.out.print("Введите новую модель автомобиля (оставьте пустым для пропуска): ");
            String model = scanner.nextLine();
            if (!model.isEmpty()) {
                if (!model.chars().allMatch(Character::isDigit)) {
                    car.setModel(model);
                } else {
                    System.out.println("\nМодель автомобиля не должна состоять только из цифр.");
                }
            }

            System.out.print("Введите новый год выпуска автомобиля (оставьте пустым для пропуска): ");
            String yearStr = scanner.nextLine();
            if (!yearStr.isEmpty()) {
                try {
                    int year = Integer.parseInt(yearStr);
                    car.setYear(year);
                } catch (NumberFormatException e) {
                    System.out.println("\nГод выпуска должен состоять только из цифр.");
                }
            }

            System.out.print("Введите новую цену автомобиля (оставьте пустым для пропуска): ");
            String priceStr = scanner.nextLine();
            if (!priceStr.isEmpty()) {
                try {
                    double price = Double.parseDouble(priceStr);
                    car.setPrice(price);
                } catch (NumberFormatException e) {
                    System.out.println("\nЦена автомобиля должна быть числовым значением.");
                }
            }

            System.out.println("Выберите новое состояние автомобиля (оставьте пустым для пропуска):");
            ConditionCar[] conditions = ConditionCar.values();
            for (int i = 0; i < conditions.length; i++) {
                System.out.println((i + 1) + ". " + ConditionCar.getDisplayName(conditions[i]));
            }

            String conditionChoiceStr = scanner.nextLine();
            if (!conditionChoiceStr.isEmpty()) {
                try {
                    int conditionChoice = Integer.parseInt(conditionChoiceStr);
                    if (conditionChoice >= 1 && conditionChoice <= conditions.length) {
                        car.setCondition(conditions[conditionChoice - 1].name());
                    } else {
                        System.out.println("\nНеверный выбор. Попробуйте снова.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("\nПожалуйста, введите номер из списка.");
                }
            }

            carService.updateCar(car);
            System.out.println("\nИнформация об автомобиле обновлена!");
            auditService.logAction(currentUser, "Обновлен автомобиль под ID " + id + "\nМарка " + car.getMake()
                    + "\nМодель " + car.getModel() + "\nГод выпуска " + car.getYear() + "\nЦена " + car.getPrice() +
                    "\nСостояние " + car.getCondition());
        } else {
            System.out.println("\nАвтомобиль не найден.");
        }
    }

    //Интерфейс удаления автомобиля
    private void deleteCar(User currentUser) {
        System.out.print("\nВведите ID автомобиля для удаления: ");
        String id = scanner.nextLine();

        Car car = carService.getCarById(id);
        if (car != null) {
            carService.deleteCar(id);
            System.out.println("\nАвтомобиль удален.");

            auditService.logAction(currentUser, "Удален автомобиль под ID " + id + "\nМарка " + car.getMake()
            + "\nМодель " + car.getMake() + "\nГод выпуска " + car.getYear() + "\nЦена " + car.getPrice() +
                    "\nСостояние " + car.getCondition());

            // Пересчитать ID остальных автомобилей
            List<Car> allCars = carService.getAllCars();
            for (Car c : allCars) {
                if (Integer.parseInt(c.getId()) > Integer.parseInt(id)) {
                    int newId = Integer.parseInt(c.getId()) - 1;
                    c.setId(String.valueOf(newId));
                    carService.updateCar(c);
                }
            }
        } else {
            System.out.println("\nАвтомобиль с ID " + id + " не найден.");
        }
    }



    //Интерфейс просмотра всех автомобилей
    public void viewAllCars(User currentUser) {

        int choice = -1;
        List<Car> cars = carService.getAllCars();
        if (cars.isEmpty()) {
            System.out.println("\nНет доступных автомобилей.");
        } else {
            for (Car car : cars) {
                System.out.println("ID: " + car.getId());
                System.out.println("Марка: " + car.getMake());
                System.out.println("Модель: " + car.getModel());
                System.out.println("Год выпуска: " + car.getYear());
                System.out.println("Цена: " + car.getPrice());
                System.out.println("Состояние: " + car.getCondition());
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
                    filterAndSortCars(currentUser);
                case 0:
                    if (currentUser.getRole() == Role.CLIENT) {
                        mainDashboard.clientDashboard(currentUser);
                    } else {
                        manageCars(currentUser);
                    }
                default:
                    System.out.println("\nНеверный выбор. Попробуйте снова.");
            }
        }
    }

    // Фильтрация автомобилей по критериям
    private void filterAndSortCars(User currentUser) {

        int choice = -1;

        while (true) {
            System.out.println("\nФильтрация и сортировка автомобилей\n");
            System.out.println("1. Фильтрация по марке");
            System.out.println("2. Фильтрация по модели");
            System.out.println("3. Фильтрация по году выпуска");
            System.out.println("4. Сортировка по цене");
            System.out.println("0. Назад");
            System.out.print("\nВыберите опцию: ");

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("\nВведите корректное число.");
            }

            switch (choice) {
                case 1:
                    filterCarsByMake(currentUser);
                    break;
                case 2:
                    filterCarsByModel(currentUser);
                    break;
                case 3:
                    filterCarsByYear(currentUser);
                    break;
                case 4:
                    sortCarsByPrice(currentUser);
                    break;
                case 0:
                    if (currentUser.getRole() == Role.CLIENT) {
                        mainDashboard.clientDashboard(currentUser);
                    } else {
                        manageCars(currentUser);
                    }
                    return;
                default:
                    System.out.println("\nНеверный выбор. Попробуйте снова.");
            }
        }
    }

    // Фильтрация автомобилей по марке
    private void filterCarsByMake(User currentUser) {
        System.out.print("\nВведите марку автомобиля для фильтрации: ");
        String make = scanner.nextLine();
        List<Car> filteredCars = carService.getCarsByMake(make);
        displayCars(currentUser, filteredCars);
    }

    // Фильтрация автомобилей по модели
    private void filterCarsByModel(User currentUser) {
        System.out.print("\nВведите модель автомобиля для фильтрации: ");
        String model = scanner.nextLine();
        List<Car> filteredCars = carService.getCarsByModel(model);
        displayCars(currentUser, filteredCars);
    }

    // Фильтрация автомобилей по году выпуска
    private void filterCarsByYear(User currentUser) {
        System.out.print("\nВведите год выпуска автомобиля для фильтрации: ");
        int year = Integer.parseInt(scanner.nextLine());
        List<Car> filteredCars = carService.getCarsByYear(year);
        displayCars(currentUser, filteredCars);
    }

    // Сортировка автомобилей по цене
    private void sortCarsByPrice(User currentUser) {
        System.out.println("\nСортировка автомобилей по цене\n");
        System.out.println("1. По возрастанию");
        System.out.println("2. По убыванию");
        System.out.print("\nВыберите опцию: ");

        int choice = Integer.parseInt(scanner.nextLine());

        List<Car> sortedCars = carService.sortCarsByPrice(choice == 1);
        displayCars(currentUser, sortedCars);
    }

    // Отображение списка автомобилей
    private void displayCars(User currentUser, List<Car> cars) {
        int choice = -1;
        if (cars.isEmpty()) {
            System.out.println("\nНет доступных автомобилей.");
        } else {
            for (Car car : cars) {
                System.out.println("ID: " + car.getId());
                System.out.println("Марка: " + car.getMake());
                System.out.println("Модель: " + car.getModel());
                System.out.println("Год выпуска: " + car.getYear());
                System.out.println("Цена: " + car.getPrice());
                System.out.println();
            }
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
                    filterAndSortCars(currentUser);
                default:
                    System.out.println("\nНеверный выбор. Попробуйте снова.");
            }
        }
    }
}
