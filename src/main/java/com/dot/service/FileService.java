package com.dot.service;

import com.dot.repository.FileReaderRepository;
import com.dot.repository.FileReaderRepostoryImpl;
import com.dot.model.BlockedIp;
import com.dot.model.UserAccessLog;
import com.dot.utils.TextReader;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileService {
   private final FileReaderRepository fileReaderRepository;
   private final int INCREMENTATION = 1;
    private final Logger logger = Logger.getLogger(FileReaderRepostoryImpl.class.getName());


    public FileService() throws SQLException, ClassNotFoundException {
         this.fileReaderRepository = new FileReaderRepostoryImpl();
    }

    public boolean createTable() throws SQLException, ClassNotFoundException {
       return fileReaderRepository.createTable();
    }

    public List<UserAccessLog> readFile(String filePath) {
        return TextReader.readFile(filePath);
    }

    public void save(List<UserAccessLog> logs) throws SQLException {
         fileReaderRepository.saveAll(logs);
    }

    public List<UserAccessLog> getUserAccessLog(String startDate, String endDate) throws SQLException {
        return fileReaderRepository.findByDateBetween(startDate,endDate);
    }


    public void addToBlockedIp(List<UserAccessLog> userAccessLogs, String limit) throws SQLException {

        HashMap<String,Integer> hashMap = new HashMap<>();
        userAccessLogs.forEach(log->{
            if(hashMap.containsKey(log.getIp())){
                int count = hashMap.get(log.getIp());
                hashMap.put(log.getIp(),count+INCREMENTATION);
            }
              else {
                hashMap.put(log.getIp(),INCREMENTATION);
            }
        });

        for(Map.Entry<String,Integer> map: hashMap.entrySet()) {
            if(map.getValue() > Integer.parseInt(limit)){
                BlockedIp blockedIp = new BlockedIp();
                blockedIp.setIp(map.getKey());
                blockedIp.setRequestNumber(map.getValue());
                blockedIp.setComment("Made more than "+limit+" request");
                fileReaderRepository.save(blockedIp);
            }

        }
        logger.log(Level.INFO,"blocked ip added to table");
    }
    public void closeConnection() throws SQLException {
        fileReaderRepository.closeConnection();
    }
    }

