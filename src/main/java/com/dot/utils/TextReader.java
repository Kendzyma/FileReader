package com.dot.utils;



import com.dot.model.UserAccessLog;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class TextReader {
    public static List<UserAccessLog> readFile(String filePath) {
        List<UserAccessLog> accessLogs = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
                while ((line = reader.readLine()) != null) {
                String[] logs = line.split("\\|");
               UserAccessLog userAccessLog =new  UserAccessLog();
               String[] date = logs[0].split(" ")[0].split("-");
                String[] time = logs[0].split(" ")[1].split(":");
            //Converting time stamp to local date time
                LocalDateTime dateTime=LocalDateTime.of(Integer.parseInt(date[0]),Integer.parseInt(date[1])
                        ,Integer.parseInt(date[2]),Integer.parseInt(time[0]),Integer.parseInt(time[1]),Integer.parseInt(time[2].replace(".","").substring(0,2)));

               userAccessLog.setDate(dateTime);
               userAccessLog.setIp(logs[1]);
               userAccessLog.setRequest(logs[2]);
               userAccessLog.setStatus(logs[3]);
               userAccessLog.setUserAgent(logs[4]);

                accessLogs.add(userAccessLog);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return accessLogs;
    }
}
