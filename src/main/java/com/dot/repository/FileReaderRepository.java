package com.dot.repository;

import com.dot.model.BlockedIp;
import com.dot.model.UserAccessLog;

import java.sql.SQLException;
import java.util.List;

public interface FileReaderRepository {
     boolean createTable() throws SQLException, ClassNotFoundException;
     void saveAll(List<UserAccessLog> userAccessLogs) throws SQLException;
     List<UserAccessLog> findByDateBetween(String startDate, String endDate) throws SQLException;
     void save(BlockedIp blockedIp) throws SQLException;
     void closeConnection() throws SQLException;
}
