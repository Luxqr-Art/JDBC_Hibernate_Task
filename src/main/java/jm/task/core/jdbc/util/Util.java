package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Util {
    // реализуйте настройку соеденения с БД
    private static final String USER_NAME = "Логин";
    private static final String URL = "jdbc:mysql://localhost:3306/Имя базы";
    private static final String PASSWORD = "Пароль";
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    public  Connection getConnection() {
        Connection connect = null;

        try {
            Class.forName(DB_DRIVER);
            connect = DriverManager.getConnection(URL, USER_NAME, PASSWORD);


        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connect;
    }
}

