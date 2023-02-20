package com.dot.connector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PostgresConnector implements JdbcConnectable{
    private final Logger logger = Logger.getLogger(PostgresConnector.class.getName());
    @Override
    public Connection postgresConnector() throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        try {
            logger.log(Level.INFO,"connecting to postgres");
            return DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/req_limit",
                            "postgres","root");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            logger.log(Level.INFO,"connection established");
        }
    }
}
