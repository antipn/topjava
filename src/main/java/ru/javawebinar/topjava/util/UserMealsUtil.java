package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 7, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);


        //!
        filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        //!
//        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with excess. Implement by cycles
        List<UserMealWithExcess> result = new ArrayList<>();
        HashMap<LocalDate, Integer> mapCaloriesPerDay = new HashMap<>();

        for (UserMeal meal : meals) { //init map with 0 calories per day
            mapCaloriesPerDay.put(meal.getDateTime().toLocalDate(), 0);
        }

        for (UserMeal meal : meals) { //filling calories in map per day
            if (mapCaloriesPerDay.containsKey(meal.getDateTime().toLocalDate())) {
                mapCaloriesPerDay.put(meal.getDateTime().toLocalDate(), mapCaloriesPerDay.get(meal.getDateTime().toLocalDate()) + meal.getCalories());
            }
        }

        for (UserMeal meal : meals) { //fillint result -> filtered date + excess calories
            if ((meal.getDateTime().toLocalTime().isAfter(startTime) || meal.getDateTime().toLocalTime().equals(startTime))
                    && meal.getDateTime().toLocalTime().isBefore(endTime)) {
                result.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), caloriesPerDay, mapCaloriesPerDay.get(meal.getDateTime().toLocalDate()) > caloriesPerDay)); //добавили еду как отсортированную но пока не считали
            }
        }
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO Implement by streams
        List<UserMealWithExcess> result = new ArrayList<>();

        // filtered list by time in stream
        List<UserMeal> filterdMeal = meals.stream()
                .filter(m -> m.getDateTime().toLocalTime().isAfter(startTime) || m.getDateTime().toLocalTime().equals(startTime))
                .filter(m -> m.getDateTime().toLocalTime().isBefore(endTime))
                .collect(Collectors.toList());
        //.forEach(m -> System.out.println(m.getDescription() + " " + m.getDateTime().toLocalTime()));

//        meals.stream()
//                .collect(Collectors.toMap((m) -> m.getDateTime().toLocalDate(), (m) -> m.getCalories()));

        return null;
    }
}
