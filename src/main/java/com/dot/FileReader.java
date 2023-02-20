package com.dot;

import com.dot.model.UserAccessLog;
import com.dot.service.FileService;
import com.dot.utils.AddTime;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileReader {
   private static Logger logger = Logger.getLogger(FileReader.class.getName());
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        //Command line arguments
        String pathToFIle = args[0].split("=")[1];
        String startDate = args[1].split("=")[1];
        String endTime = args[2].split("=")[1].split(" ")[0];
        String limit = args[3].split("=")[1];

        //Read File
        FileService fileService = new FileService();
        List<UserAccessLog> userAccessLogs = fileService.readFile(pathToFIle);
        logger.log(Level.INFO,"File reading complete. size is "+userAccessLogs.size());

        //create table
        logger.log(Level.INFO,"Creating table.......");
        boolean isExist = fileService.createTable();

        //Save data to db
        if(!isExist) {
            logger.log(Level.INFO,"saving to db.....");
            fileService.save(userAccessLogs);
        }
        //Increment date time by hour or 24 hours
        String date = AddTime.addToTime(startDate, endTime);

        // get all logs by date
        logger.log(Level.INFO,"Getting all user access logs from db....");
        List<UserAccessLog> userAccessLogs1 = fileService.getUserAccessLog(startDate, date);

        //add to blocked ip table
        logger.log(Level.INFO,"Adding to blocked ip table.........");
        fileService.addToBlockedIp(userAccessLogs1, limit);

        //close connection

        fileService.closeConnection();
        logger.log(Level.INFO,"Connection closed");
    }
}