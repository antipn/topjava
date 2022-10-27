package ru.javawebinar.topjava.service;

import org.assertj.core.matcher.AssertionMatcher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static java.time.LocalDateTime.of;
import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})

@RunWith(SpringRunner.class)
//перед каждым тестом будет восстанавливаться база указанным скриптом
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    MealService service;

    @Autowired
    MealRepository repository;

    @Test
    public void delete() {
        service.delete(MEAL1_ID, USER_ID);
        Assert.assertNull(repository.get(MEAL1_ID, USER_ID));
    }

    @Test
    public void deleteNotFound() {
        Assert.assertThrows(NotFoundException.class, () -> service.delete(1, USER_ID));
    }

    @Test
    public void deleteNotOwnMeal() {
        Assert.assertThrows(NotFoundException.class, () -> service.delete(MEAL1_ID, ADMIN_ID));
    }

    @Test
    public void getBetweenInclusiveWithNullDates() {
        assertThat(MEALS).isEqualTo(service.getBetweenInclusive(null, null, USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> actual = service.getBetweenInclusive(LocalDate.of(2020, Month.JANUARY, 30), LocalDate.of(2020, Month.JANUARY, 30), USER_ID);
        List<Meal> expected = Arrays.asList(USER_MEAL_3, USER_MEAL_2, USER_MEAL_1);
        //assertThat(expected).isEqualTo(actual);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void getAll() {
        List<Meal> actual = service.getBetweenInclusive(LocalDate.of(2020, Month.JANUARY, 30), LocalDate.of(2020, Month.JANUARY, 31), USER_ID);
        List<Meal> expected = MEALS;
        //assertThat(expected).isEqualTo(actual);
        assertThat(expected).usingRecursiveComparison().isEqualTo(actual);
    }

    @Test
    public void update() {
        Meal updatedMeal = service.get(MEAL1_ID, USER_ID);
        updatedMeal.setDescription("Обновленная еда");
        service.update(updatedMeal, USER_ID);
        //assertThat(updatedMeal).isEqualTo(service.get(MEAL1_ID, USER_ID));
        assertThat(updatedMeal).usingRecursiveComparison().isEqualTo(service.get(MEAL1_ID, USER_ID));

    }

    @Test
    public void create() {
        Meal newMeal = new Meal(null, of(2020, Month.FEBRUARY, 1, 10, 30), "Завтрак", 240);
        Meal createdMeal = service.create(newMeal, USER_ID);
        Integer mealId = createdMeal.getId();
        newMeal.setId(mealId);
        assertThat(newMeal).usingRecursiveComparison().isEqualTo(createdMeal); //проверяем что две еды идентичны

    }
}