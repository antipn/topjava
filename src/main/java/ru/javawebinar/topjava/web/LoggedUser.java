package ru.javawebinar.topjava.web;

public class LoggedUser {
    public static void setId(int id){
        SecurityUtil.setUserId(id); //устанавливаем поле userId
    }
}
