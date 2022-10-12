package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.createTo;

public class MealServlet extends HttpServlet {
    public static List<MealTo> listMeals = Arrays.asList(
            new MealTo(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500, false),
            new MealTo(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000, false),
            new MealTo(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500, false),
            new MealTo(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100, true),
            new MealTo(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000, true),
            new MealTo(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500, true),
            new MealTo(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410, true)
    );

    public static List<MealTo> addMeal(List<MealTo> inputList, Meal meal) {
        //конвертируем все MealTo to Meal чтобы добавить новый meal-> List
        List<Meal> converingMealToToMealList = new ArrayList<>();
        for (MealTo mealTo : inputList) {
            converingMealToToMealList.add(new Meal(mealTo.getDateTime(), mealTo.getDescription(), mealTo.getCalories()));
        }
        converingMealToToMealList.add(meal);
        //теперь нужно сформировать обратно List<MealTo> из List<Meal>
        Map<LocalDate, Integer> caloriesSumByDate = converingMealToToMealList.stream() //map по дням калории
                .collect(Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories)));

        listMeals = converingMealToToMealList.stream()
                .map(m -> createTo(m, caloriesSumByDate.get(m.getDate()) > MealsUtil.caloriesPerDay))
                .collect(Collectors.toList());
        return listMeals;
    }

    public static List<MealTo> refreshData(List<MealTo> inputList) {
        Map<LocalDate, Integer> caloriesSumByDate = inputList.stream()//map по дням калории
                .collect(Collectors.groupingBy(m -> m.getDateTime().toLocalDate(), Collectors.summingInt(m -> m.getCalories())));
        listMeals = inputList.stream()
                .map(m -> new MealTo(m.getDateTime(), m.getDescription(), m.getCalories(), caloriesSumByDate.get(m.getDateTime().toLocalDate()) > MealsUtil.caloriesPerDay))
                .collect(Collectors.toList());
        listMeals.sort(Comparator.comparing(MealTo::getDateTime));
        return listMeals;
    }

    public static List<MealTo> deleteMeal(List<MealTo> inputList, LocalDateTime timeStampForDeleting) {
        List<MealTo> updatedListMeal = new ArrayList<>();
        for (MealTo meal : inputList) {
            if (!(meal.getDateTime().equals(timeStampForDeleting))) {
                updatedListMeal.add(meal);
            }
        }
        listMeals = updatedListMeal;
        return listMeals;
    }

    public static MealTo getMeal(List<MealTo> inputList, LocalDateTime timeStampForDeleting) {
        MealTo result = null;
        for (MealTo meal : inputList) {
            if ((meal.getDateTime().equals(timeStampForDeleting))) {
                result = meal;
            }
        }
        return result;
    }


    private static final Logger log = getLogger(UserServlet.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("forward to post");

        String forward = "";
        String dateTime = request.getParameter("mealDatetime");
        String description = request.getParameter("mealDescription");
        String calories = request.getParameter("mealCalories");
        Meal addedMeal = new Meal(LocalDateTime.parse(dateTime), description, Integer.parseInt(calories));
        //далее в зависимости от того добавляли или редактировали питание
        if ((request.getParameter("mealtoDateTimeMarker") != null)) { //редактировали питание
            //starting process of editing meal -> create new one and after delete edited meal!
            listMeals = deleteMeal(listMeals, LocalDateTime.parse(request.getParameter("mealtoDateTimeMarker")));
            addMeal(listMeals, addedMeal); //Добавляем питание в любом случае
            forward = "meals.jsp";
            refreshData(listMeals); //обновление отображения списка питания
            request.setAttribute("listMeals", listMeals);
        } else { //добавляли питание новое
            addMeal(listMeals, addedMeal); //Добавляем питание в любом случае
            forward = "add-meal.jsp";
            request.setAttribute("processingStatus", "Successfully added meal");
        }
        refreshData(listMeals); //обновление отображения списка питания
        request.getRequestDispatcher(forward).forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("forward to meals");

        String forward = "";
        String action = request.getParameter("action");

        if (!(action == null)) {

            if (action.equalsIgnoreCase("insert")) { //перекидываем на страничку ввода нового питания
                forward = "add-meal.jsp";
                request.setAttribute("processingStatus", "");
                request.getRequestDispatcher(forward).forward(request, response); //при redirect теряются вводные!

            } else if (action.equalsIgnoreCase("listMeals")) {
                forward = "meals.jsp";
                request.setAttribute("listMeals", listMeals);
                request.getRequestDispatcher(forward).forward(request, response);

            } else if (action.equalsIgnoreCase("delete")) {
                listMeals = deleteMeal(listMeals, LocalDateTime.parse(request.getParameter("mealTimeStamp"))); //переопределяем список еды через удаление полученного параметра питания
                refreshData(listMeals); //обновление отображения списка питания
                request.setAttribute("listMeals", listMeals);
                request.getRequestDispatcher("meals.jsp").forward(request, response);

            } else if (action.equalsIgnoreCase("edit")) {
                request.setAttribute("mealForEditing", getMeal(listMeals, LocalDateTime.parse(request.getParameter("mealTimeStamp")))); //параметр питания для редактирования -> LocalDateTime -> по нему получаем строку питания т отдаем ее в форму
                forward = "edit-meal.jsp";
                request.getRequestDispatcher(forward).forward(request, response); //при redirect теряются вводные!
            }
        } else {
            //в любом случае выполниться при первом нажатии в Meals
            request.setAttribute("listMeals", listMeals);
            request.getRequestDispatcher("meals.jsp").forward(request, response); //при redirect теряются вводные!
        }
    }
}
