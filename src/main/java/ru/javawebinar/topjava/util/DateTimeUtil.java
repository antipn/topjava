package ru.javawebinar.topjava.util;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    //DB не поддерживают значения по умолчанию, лучше преопределить мин и макс дату
    //если даты не введены в фильтре то будем использовать эти по умолчанию
    public static final LocalDateTime MIN_DATE = LocalDateTime.of(1, 1, 1, 0, 0);
    public static final LocalDateTime MAX_DATE = LocalDateTime.of(3000, 1, 1, 0, 0);

    public static boolean isBetweenHalfOpen(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) < 0;
    }

    //Если Null -> время будет минимальное значение времени для текущего дня
    public static LocalDateTime getStartInclusive(LocalDate localDate) {
        return localDate != null ? localDate.atStartOfDay() : MIN_DATE;
    }

    //Если Null -> время будет минимальное значение времени для следующего дня
    public static LocalDateTime getEndExclusive(LocalDate localDate) {
        return localDate != null ? localDate.plus(1, ChronoUnit.DAYS).atStartOfDay() : MAX_DATE;
    }

    public static @Nullable
    LocalDate parseLocalDate(@Nullable String str) {
        return StringUtils.isEmpty(str) ? null : LocalDate.parse(str);

    }

    public static @Nullable
    LocalTime parseLocalTime(@Nullable String str) {
        return StringUtils.isEmpty(str) ? null : LocalTime.parse(str);

    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

}

