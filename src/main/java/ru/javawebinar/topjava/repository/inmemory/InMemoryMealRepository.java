package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.util.CollectionUtils;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.repository.inmemory.InMemoryUserRepository.USER_ID;

public class InMemoryMealRepository implements MealRepository {

    // Map <userId, Map <mealId, meal>>
    private final Map<Integer, Map<Integer, Meal>> repositoryMeal = new ConcurrentHashMap<>(); //multy map
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal ->save(meal,USER_ID));

    }

    @Override
    public Meal save(Meal meal, int userId) {
        //получаем map по user id, если у пользователя еще нет еды -> создаем новую map c его пустой едой для заполнения
        Map<Integer, Meal> userMealsMap = repositoryMeal.computeIfAbsent(userId, newMap -> new ConcurrentHashMap<Integer, Meal>(newMap));
        if (meal.isNew()) { //еда новая и у нее нет еще id
            meal.setId(counter.incrementAndGet());
            userMealsMap.put(meal.getId(), meal); //добавляем еду в хранилище еды пользователя
            return meal;
        }
        // handle case: update, but not present in storage
        return userMealsMap.computeIfPresent(meal.getId(), (id, oldMeal) -> meal); //если еда не новая, то она обновится новым значением вместо старого id не изменится
    }

    @Override
    public boolean delete(int id, int userId) {
        //получаем map по userId
        Map<Integer, Meal> userMealsMap = repositoryMeal.get(userId);
        return ((userMealsMap != null) && (userMealsMap.remove(id) != null)); //если и список еды пользователя не пустой и еда удалилась
    }

    @Override
    public Meal get(int id, int userId) {
        //получаем map по userId
        Map<Integer, Meal> userMealsMap = repositoryMeal.get(userId);
        return userMealsMap == null ? null : userMealsMap.get(id); //если список еды пустой возвращаем null, иначе значение еды
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        Map<Integer, Meal> userMealsMap = repositoryMeal.get(userId);

        return CollectionUtils.isEmpty(userMealsMap) ? Collections.emptyList() :
                userMealsMap.values().stream()
                        .sorted(Comparator.comparing((Meal meal) -> meal.getDateTime()).reversed()) //по дате в обратном порядке
                        .collect(Collectors.toList());
    }
}

