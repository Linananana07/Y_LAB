package org.carshop.repositories;

import org.carshop.model.Car;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * Репозиторий для хранения автомобилей в памяти.
 */
public class InMemoryCarRepository implements CarRepository {

    private List<Car> cars = new ArrayList<>();

    /**
     * Сохраняет автомобиль в репозитории.
     *
     * @param car автомобиль для сохранения
     */
    @Override
    public void save(Car car) {
        cars.add(car);
    }

    /**
     * Находит автомобиль по его идентификатору.
     *
     * @param id идентификатор автомобиля
     * @return автомобиль с указанным идентификатором, или null, если автомобиль не найден
     */
    @Override
    public Car findById(String id) {
        return cars.stream().filter(car -> car.getId().equals(id)).findFirst().orElse(null);
    }

    /**
     * Возвращает список всех автомобилей в репозитории.
     *
     * @return список всех автомобилей
     */
    @Override
    public List<Car> findAll() {
        return new ArrayList<>(cars);
    }

    /**
     * Обновляет информацию об автомобиле в репозитории.
     *
     * @param car автомобиль с обновленными данными
     */
    @Override
    public void update(Car car) {
        Optional<Car> existingCar = cars.stream().filter(c -> c.getId().equals(car.getId())).findFirst();
        existingCar.ifPresent(value -> {
            value.setMake(car.getMake());
            value.setModel(car.getModel());
            value.setYear(car.getYear());
            value.setPrice(car.getPrice());
            value.setCondition(car.getCondition());
        });
    }

    /**
     * Удаляет автомобиль из репозитория по его идентификатору.
     *
     * @param id идентификатор автомобиля для удаления
     */
    @Override
    public void delete(String id) {
        cars.removeIf(car -> car.getId().equals(id));
    }

    /**
     * Возвращает количество автомобилей в репозитории.
     *
     * @return количество автомобилей
     */
    @Override
    public long count() {
        return cars.size();
    }
}
