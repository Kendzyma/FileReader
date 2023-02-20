package com.dot.connector;

import java.sql.Connection;
import java.sql.SQLException;

public interface JdbcConnectable {
Connection postgresConnector() throws ClassNotFoundException, SQLException;
}
