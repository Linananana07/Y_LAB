package org.carshop.services;

import org.carshop.model.Car;
import org.carshop.repositories.CarRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для управления автомобилями.
 * <p>Этот сервис предоставляет методы для добавления, получения, обновления и удаления автомобилей.
 * Также включает функции для фильтрации и сортировки автомобилей.</p>
 */
public class CarService {

    private CarRepository carRepository;

    /**
     * Конструктор класса {@code CarService}.
     *
     * @param carRepository репозиторий для управления автомобилями
     */
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    /**
     * Добавляет новый автомобиль.
     *
     * @param make марка автомобиля
     * @param model модель автомобиля
     * @param year год выпуска
     * @param price цена автомобиля
     * @param condition состояние автомобиля
     */
    public void addCar(String make, String model, int year, double price, String condition) {
        Car car = new Car(generateId(), make, model, year, price, condition);
        carRepository.save(car);
    }

    /**
     * Возвращает список всех автомобилей.
     *
     * @return список всех автомобилей
     */
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    /**
     * Возвращает автомобиль по его идентификатору.
     *
     * @param id идентификатор автомобиля
     * @return автомобиль с указанным идентификатором, или {@code null}, если автомобиль не найден
     */
    public Car getCarById(String id) {
        return carRepository.findById(id);
    }

    /**
     * Обновляет информацию об автомобиле.
     *
     * @param car автомобиль с обновленной информацией
     */
    public void updateCar(Car car) {
        carRepository.update(car);
    }

    /**
     * Удаляет автомобиль по его идентификатору.
     *
     * @param id идентификатор автомобиля
     */
    public void deleteCar(String id) {
        carRepository.delete(id);
    }

    /**
     * Генерирует уникальный идентификатор для нового автомобиля.
     *
     * @return уникальный идентификатор
     */
    private String generateId() {
        return String.valueOf(carRepository.count() + 1);
    }

    /**
     * Возвращает список автомобилей, соответствующих указанной марке.
     *
     * @param make марка автомобиля
     * @return список автомобилей с указанной маркой
     */
    public List<Car> getCarsByMake(String make) {
        return carRepository.findAll().stream()
                .filter(car -> car.getMake().equalsIgnoreCase(make))
                .collect(Collectors.toList());
    }

    /**
     * Возвращает список автомобилей, соответствующих указанной модели.
     *
     * @param model модель автомобиля
     * @return список автомобилей с указанной моделью
     */
    public List<Car> getCarsByModel(String model) {
        return carRepository.findAll().stream()
                .filter(car -> car.getModel().equalsIgnoreCase(model))
                .collect(Collectors.toList());
    }

    /**
     * Возвращает список автомобилей, соответствующих указанному году выпуска.
     *
     * @param year год выпуска
     * @return список автомобилей с указанным годом выпуска
     */
    public List<Car> getCarsByYear(int year) {
        return carRepository.findAll().stream()
                .filter(car -> car.getYear() == year)
                .collect(Collectors.toList());
    }

    /**
     * Сортирует автомобили по цене.
     *
     * @param ascending {@code true}, если сортировка по возрастанию, {@code false}, если по убыванию
     * @return отсортированный список автомобилей
     */
    public List<Car> sortCarsByPrice(boolean ascending) {
        return carRepository.findAll().stream()
                .sorted(ascending
                        ? Comparator.comparingDouble(Car::getPrice)
                        : Comparator.comparingDouble(Car::getPrice).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Обновляет индексы автомобилей, чтобы они начинались с 1 и увеличивались на 1 для каждого следующего автомобиля.
     */
    public void updateCarIndices() {
        List<Car> cars = carRepository.findAll();
        for (int i = 0; i < cars.size(); i++) {
            Car car = cars.get(i);
            car.setId(String.valueOf(i + 1));
            carRepository.update(car);
        }
    }
}
