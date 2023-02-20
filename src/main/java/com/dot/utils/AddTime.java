package com.dot.utils;

import java.time.LocalDateTime;


public class AddTime {
private  static final int ONEHOUR = 1;
private static final int TWENTYFOURHOURS = 24;

    public static String addToTime (String dateTime,String endTime){
        String[] startDate = dateTime.split("\\.")[0].split("-");
        String[] startTime = dateTime.split("\\.")[1].split(":");
        LocalDateTime startDateTime=LocalDateTime.of(Integer.parseInt(startDate[0]),Integer.parseInt(startDate[1])
                ,Integer.parseInt(startDate[2]),Integer.parseInt(startTime[0]),Integer.parseInt(startTime[1]),Integer.parseInt(startTime[2]));

        LocalDateTime endDateTime= endTime.equals("hourly") ? startDateTime.plusHours(ONEHOUR) : endTime.equals("daily") ? startDateTime.plusHours(TWENTYFOURHOURS) : startDateTime;

        return endDateTime.toString().replace("T"," ");
    }
}
