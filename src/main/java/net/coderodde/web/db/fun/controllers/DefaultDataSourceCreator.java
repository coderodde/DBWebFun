package net.coderodde.web.db.fun.controllers;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class DefaultDataSourceCreator {

    public static MysqlDataSource create() {
        MysqlDataSource mysql =
                DBUtils.getMysqlDataSource(
                        "root",
                        "your_password",
                        "jdbc:mysql://localhost:3306/funny_db", 
                        -1, 
                        null);
            
        return mysql;
    }
}
