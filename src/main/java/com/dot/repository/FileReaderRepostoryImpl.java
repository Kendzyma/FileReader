package com.dot.repository;

import com.dot.connector.JdbcConnectable;
import com.dot.connector.PostgresConnector;
import com.dot.model.BlockedIp;
import com.dot.model.UserAccessLog;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileReaderRepostoryImpl implements FileReaderRepository{
    private final Logger logger = Logger.getLogger(FileReaderRepostoryImpl.class.getName());
    private final Connection connection;
    private Statement stmt = null;
    PreparedStatement preparedStatement = null;
    public FileReaderRepostoryImpl() throws SQLException, ClassNotFoundException {
        JdbcConnectable jdbcConnectable = new PostgresConnector();
        connection = jdbcConnectable.postgresConnector();
    }

    @Override
    public boolean createTable() throws SQLException {
        stmt = connection.createStatement();
        boolean isExist = tableExists("user_access_log");
        if (!isExist) {
            String userAccessTableQuery = "create table user_access_log(\n" +
                    "id serial primary key,\n" +
                    "date timestamp(6),\n" +
                    "ip varchar(255),\n" +
                    "request varchar(255),\n" +
                    "status varchar(255),\n" +
                    "user_agent varchar(255)\n" +
                    ")";
            stmt.executeUpdate(userAccessTableQuery);
            logger.log(Level.INFO,"USER_ACCESS_LOG table created");
        }
        if (!isExist) {

            String blockedIPTableQuery = "create table blocked_ip_table(\n" +
                    "id serial primary key,\n" +
                    "ip varchar(255),\n" +
                    "request_number int,\n" +
                    "comment varchar(255)\n" +
                    ")";

            stmt.executeUpdate(blockedIPTableQuery);
            logger.log(Level.INFO,"BLOCKED_IP_TABLE created");
        }
        logger.log(Level.INFO, "All tables created");
        return isExist;
    }

    @Override
    public void saveAll(List<UserAccessLog> userAccessLogs) {

        userAccessLogs.forEach(log-> {
            try {
                String create = "INSERT INTO user_access_log (date,ip,request,status,user_agent) VALUES (?, ?, ?, ?, ?)";

                preparedStatement = connection.prepareStatement(create);
                preparedStatement.setTimestamp(1,Timestamp.valueOf(log.getDate()));
                preparedStatement.setString(2, log.getIp());
                preparedStatement.setString(3, log.getRequest());
                preparedStatement.setString(4, log.getStatus());
                preparedStatement.setString(5, log.getUserAgent());

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        logger.log(Level.INFO,"all user access logs saved");
        }

    @Override
    public List<UserAccessLog> findByDateBetween(String startDate, String endDate) throws SQLException {
        startDate = startDate.replace(".", " ");
        String getBlockedIpQuery = "select date,ip,request,status,user_agent from user_access_log where date between '"+startDate+"' and '"+endDate+"'";

        preparedStatement = connection.prepareStatement(getBlockedIpQuery);
        ResultSet resultSet = preparedStatement.executeQuery();

        List<UserAccessLog> userAccessLogs = new ArrayList<>();
        while (resultSet.next()){
            LocalDateTime dateTime = resultSet.getTimestamp("date").toLocalDateTime();
            String ip = resultSet.getString("ip");
            String request = resultSet.getString("request");
            String status = resultSet.getString("status");
            String userAgent = resultSet.getString("user_agent");

            UserAccessLog userAccessLog = new UserAccessLog(dateTime,ip,request,status,userAgent);
            userAccessLogs.add(userAccessLog);
        }

        return userAccessLogs;
    }

    @Override
    public void save(BlockedIp blockedIp) throws SQLException {
        boolean isExist = false;
        // check if ip is already blocked
        String checkQuery = "select ip,request_number,comment from blocked_ip_table where ip = ?";
        preparedStatement = connection.prepareStatement(checkQuery);
        preparedStatement.setString(1, blockedIp.getIp());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            isExist = true;
        }

        if(!isExist) {
            String saveQuery = "INSERT INTO blocked_ip_table (ip,request_number,comment) VALUES (?, ?, ?)";
            try {
                preparedStatement = connection.prepareStatement(saveQuery);
                preparedStatement.setString(1, blockedIp.getIp());
                preparedStatement.setInt(2, blockedIp.getRequestNumber());
                preparedStatement.setString(3, blockedIp.getComment());
                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean tableExists(String tableName) throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();
        ResultSet resultSet = meta.getTables(null, null, tableName, new String[] {"TABLE"});

        return resultSet.next();
    }

    @Override
    public void closeConnection() throws SQLException {
        connection.close();
    }
}
