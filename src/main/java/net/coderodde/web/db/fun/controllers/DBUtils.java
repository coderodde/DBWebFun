package net.coderodde.web.db.fun.controllers;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class contains some common utilities for dealing with databases.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Aug 8, 2017)
 */
public final class DBUtils {

    /**
     * Creates and sets a MySQL data source.
     * 
     * @param user       the user name.
     * @param password   the password.
     * @param url        the database URL.
     * @param port       the database port.
     * @param serverName the name of the server.
     * 
     * @return the MySQL data source.
     */
    public static  MysqlDataSource getMysqlDataSource(String user,
                                                      String password,
                                                      String url,
                                                      int port,
                                                      String serverName) {
        MysqlDataSource dataSource = new MysqlDataSource();
        
        if (user != null) {
            dataSource.setUser(user);
        }
        
        if (password != null) {
            dataSource.setPassword(password);
        }
        
        if (url != null) {
            dataSource.setURL(url);
        }
        
        if (port >= 0) {
            dataSource.setPort(port);
        }
        
        if (serverName != null) {
            dataSource.setServerName(serverName);
        }
        
        return dataSource;
    }
    
    /**
     * Closes a {@code ResultSet}.
     * 
     * @param resultSet the result set to close.
     */
    public static void close(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Could not close a ResultSet.", ex);
        }
    }
    
    /**
     * Closes a {@code PreparedStatement}.
     * 
     * @param preparedStatement the prepared statement to close.
     */
    public static void close(PreparedStatement preparedStatement) {
        try {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(
                    "Could not close a PreparedStatement.",
                    ex);
        }        
    }
    
    /**
     * Closes a {@code Connection}.
     * 
     * @param connection the connection to close.
     */
    public static void close(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(
                    "Could not close a Connection.",
                    ex);
        }        
    }
    
    /**
     * Closes the data base related resources.
     * 
     * @param resultSet         the result set to close.
     * @param preparedStatement the prepared statement to close.
     * @param connection        the connection to close.
     */
    public static void close(ResultSet resultSet,
                             PreparedStatement preparedStatement, 
                             Connection connection) {
        close(resultSet);
        close(preparedStatement);
        close(connection);
    }
}
