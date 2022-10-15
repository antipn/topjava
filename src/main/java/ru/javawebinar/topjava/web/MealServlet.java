package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.getWithExceeded;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("forward to meals");

        List<MealTo> listMeals = getWithExceeded(MealsUtil.MEAL_LIST,MealsUtil.DEFAULT_CALORIES);

        request.setAttribute("listMeals", listMeals); //здесь мы должны получать список еды с маркировкой о превышении калорий в день

        request.getRequestDispatcher("meals.jsp").forward(request, response); //при redirect теряются вводные!
    }


}
