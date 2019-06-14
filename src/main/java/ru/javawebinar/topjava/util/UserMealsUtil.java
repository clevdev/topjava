package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;


public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        List<UserMealWithExceed> userMealWithExceedList = getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);

        for (UserMealWithExceed meal : userMealWithExceedList) {
            System.out.println(meal.toString());
        }
        userMealWithExceedList = getFilteredWithExceededV2(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        for (UserMealWithExceed meal : userMealWithExceedList) {
            System.out.println(meal.toString());
        }
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with correctly exceeded field

        Map<LocalDate, Integer> coloriesPerDayList = new HashMap<>();
        List<UserMealWithExceed> mealWithExceedList = new ArrayList<>();

        for (UserMeal meal : mealList) {
            coloriesPerDayList.put(meal.getDateTime().toLocalDate(),
                    coloriesPerDayList.getOrDefault(meal.getDateTime().toLocalDate(), 0) + meal.getCalories());
        }

        for (UserMeal usermeal : mealList) {
            if (TimeUtil.isBetween(usermeal.getDateTime().toLocalTime(), startTime, endTime)) {
                mealWithExceedList.add(new UserMealWithExceed(usermeal.getDateTime(),
                        usermeal.getDescription(),
                        usermeal.getCalories(),
                        (coloriesPerDayList.getOrDefault(usermeal.getDateTime().toLocalDate(), 0) > caloriesPerDay)));
            }
        }

        return mealWithExceedList;
    }
    
    public static List<UserMealWithExceed> getFilteredWithExceededV2(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> coloriesPerDayList = mealList.stream().collect(Collectors.groupingBy(m -> m.getDateTime().toLocalDate(), Collectors.summingInt(UserMeal::getCalories)));
        List<UserMealWithExceed> mealWithExceedList = mealList.stream().filter(m -> TimeUtil.isBetween(m.getDateTime().toLocalTime(), startTime, endTime))
                    .map(m -> new UserMealWithExceed(m.getDateTime(),
                        m.getDescription(),
                        m.getCalories(),
                        (coloriesPerDayList.getOrDefault(m.getDateTime().toLocalDate(), 0) > caloriesPerDay))).collect(Collectors.toList());

        return mealWithExceedList;

    }

}
