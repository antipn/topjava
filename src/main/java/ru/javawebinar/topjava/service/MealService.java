package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {
    private final MealRepository repository;

    @Autowired
    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal,int userId){
        return repository.save(meal, userId);
    }

    public void delete (int id, int userId){
        checkNotFoundWithId(repository.delete(id, userId),id);
    }

    public Meal get(int mealId, int userId){
        return checkNotFoundWithId(repository.get(mealId,userId),mealId);
    }

    public List<Meal> getAll(int userId){
        return repository.getAll(userId);
    }

    public void update (Meal meal, int userId){
        checkNotFoundWithId(repository.save(meal,userId), meal.getId());
    }

//potential method get meals by meal and user id + start and end time

}