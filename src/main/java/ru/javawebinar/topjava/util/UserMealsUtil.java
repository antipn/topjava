package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 29, 13, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 29, 16, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 29, 19, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 7, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 29, 20, 0), "Ужин2", 610)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        List<UserMealWithExcess> mealsTo2 = filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        //System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
        mealsTo2.forEach(System.out::println);
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with excess. Implement by cycles
        Map<LocalDate, Integer> mapCaloriesPerDay = new HashMap<>(); //map with days and calories per day
        List<UserMeal> filteredMealsList = new ArrayList<>(); //filtered list by start and end time for converting to result list using map1 and map2
        List<UserMealWithExcess> result = new ArrayList<>();

        for (UserMeal meal : meals) {
            if (meal.getCalories() <= 0) {
                continue;
            }
            LocalDate localDate = meal.getDateTime().toLocalDate();
            LocalTime localTime = meal.getDateTime().toLocalTime();

            if (!mapCaloriesPerDay.containsKey(localDate)) {//counting calories for all day
                mapCaloriesPerDay.put(localDate, meal.getCalories());
            } else {
                mapCaloriesPerDay.put(localDate, mapCaloriesPerDay.get(localDate) + meal.getCalories());
            }

            if ((localTime.isAfter(startTime) || localTime.equals(startTime)) && localTime.isBefore(endTime)) {//filtered meal
                filteredMealsList.add(meal);
            }
        }

        for (UserMeal meal : filteredMealsList) {
            result.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), mapCaloriesPerDay.get(meal.getDateTime().toLocalDate()) > caloriesPerDay));
        }
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO Implement by streams
        List<UserMealWithExcess> result = new ArrayList<>();
        // filtered list by time in stream
        List<UserMeal> filterdMeal = meals.stream().filter(m -> m.getDateTime().toLocalTime().isAfter(startTime) || m.getDateTime().toLocalTime().equals(startTime)).filter(m -> m.getDateTime().toLocalTime().isBefore(endTime)).collect(Collectors.toList());
        // created map <UserMealWithExcess, Integer> with sum by day
        Map<LocalDate, Integer> mapCaloriesPerDay = meals.stream()
                .collect(Collectors.groupingBy(m -> m.getDateTime().toLocalDate(), Collectors.summingInt(UserMeal::getCalories)));
        //created result list
        result = filterdMeal.stream().map(m -> new UserMealWithExcess(m.getDateTime(), m.getDescription(), m.getCalories(), mapCaloriesPerDay.get(m.getDateTime().toLocalDate()) > caloriesPerDay)).collect(Collectors.toList());
        return result;
    }
}
