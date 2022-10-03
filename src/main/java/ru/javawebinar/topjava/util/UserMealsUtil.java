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
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);
//        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with excess. Implement by cycles
        Map<LocalDate, Integer> mapCaloriesPerDay = new HashMap<>(); //map1
        Map<LocalDate, List<UserMeal>> mapMealsPerDay = new HashMap<>(); //map2
        List<UserMeal> filteredMealsList = new ArrayList<>(); //filtered list by start and end time for converting to result list using map1 and map2
        List<UserMealWithExcess> result = new ArrayList<>();

        for (UserMeal meal : meals) {
            if (meal.getCalories() <= 0) {
                continue;
            }
            LocalDate localDate = meal.getDateTime().toLocalDate();
            LocalTime localTime = meal.getDateTime().toLocalTime();
            //заполнить map1 <LocalDate,Ineger>
            if (!mapCaloriesPerDay.containsKey(localDate)) {//если записи еще не было кладем первую с калориями за прием пищи

                mapCaloriesPerDay.put(localDate, meal.getCalories());
            } else {
                mapCaloriesPerDay.put(localDate, mapCaloriesPerDay.get(localDate) + meal.getCalories());
            }
            //условие по времени для заполнения map2 <LocalDate, List<UserMeal>>
            // ? как мы можем тут фильтровать по времени ?
            // если например первая строка нам точно по условиям е подходит, мы же не можем ее игнорировать после того как поймем что она нужна
            List<UserMeal> mealsPerDayList = new ArrayList<>();
            if (!mapMealsPerDay.containsKey(localDate)) {//если такого дня еще нет то добавляем его в map2
                mapMealsPerDay.put(localDate, new ArrayList<>());
                mealsPerDayList.add(meal);
                mapMealsPerDay.put(localDate, mealsPerDayList);
            } else {
                mealsPerDayList = mapMealsPerDay.get(localDate);
                mealsPerDayList.add(meal);
                mapMealsPerDay.put(localDate, mealsPerDayList);
            }
            if ((localTime.isAfter(startTime) || localTime.equals(startTime)) && localTime.isBefore(endTime)) {
//                System.out.println("Этот прием пищи " + meal.getDescription() + " " + localDate + " в " + localTime + "\t\t\t нам подходит");
                filteredMealsList.add(meal);
            } else {
//               System.out.println("Этот прием пищи " + meal.getDescription() + " " + localDate + " в " + localTime + "\t нам не подходит");
            }
        }

        for (UserMeal meal : filteredMealsList) {
            result.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), mapCaloriesPerDay.get(meal.getDateTime().toLocalDate()) > caloriesPerDay));
        }
//        System.out.println("Map1");
//        for (Map.Entry<LocalDate, Integer> entry : mapCaloriesPerDay.entrySet()) {
//            System.out.println(entry.getKey() + " " + entry.getValue());
//        }
//
//        System.out.println("Map2");
//        for (Map.Entry<LocalDate, List<UserMeal>> entry : mapMealsPerDay.entrySet()) {
//            System.out.println(entry.getKey() + " ");
//            List<UserMeal> mealsList = entry.getValue();
//            for (int i = 0; i < entry.getValue().size(); i++) {
//                System.out.println(mealsList.get(i).getDateTime().toLocalTime() + " " + mealsList.get(i).getDescription() + " " + mealsList.get(i).getCalories());
//            }
//        }
//        System.out.println("filteredMealsList");
//        for (UserMeal meal : filteredMealsList) {
//            System.out.println(meal.getDateTime() + " " + meal.getDescription() + " " + meal.getCalories());
//        }
//
//        System.out.println("Result");
//        for (UserMealWithExcess meal : result) {
//            System.out.println(meal.toString());
//        }


//        for (UserMeal meal : meals) { //filling result -> filtered date + excess calories
//            //это условие можно использовать выше в map
//            if ((meal.getDateTime().toLocalTime().isAfter(startTime) || meal.getDateTime().toLocalTime().equals(startTime))
//                    && meal.getDateTime().toLocalTime().isBefore(endTime)) {
//                result.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), caloriesPerDay, mapCaloriesPerDay.get(meal.getDateTime().toLocalDate()) > caloriesPerDay)); //добавили еду как отсортированную но пока не считали
//            }
//        }

        //формируем конечного листа с данными взять map2 и данные из map1
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO Implement by streams
        List<UserMealWithExcess> result = new ArrayList<>();

        // filtered list by time in stream ->
        List<UserMeal> filterdMeal = meals.stream().filter(m -> m.getDateTime().toLocalTime().isAfter(startTime) || m.getDateTime().toLocalTime().equals(startTime)).filter(m -> m.getDateTime().toLocalTime().isBefore(endTime)).collect(Collectors.toList());

        // created map <UserMealWithExcess, Integer> with sum by day
        meals.stream().collect(Collectors.toMap(m -> m.getDateTime(), Function.identity()));

        //.mapToInt(UserMeal::getCalories)
        //.forEach(System.out::println);
        return null;
    }
}
