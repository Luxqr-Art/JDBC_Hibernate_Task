package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class UserDaoJDBCImpl implements UserDao {

    Util util = new Util();

    public UserDaoJDBCImpl() {


    }

    public void createUsersTable() {

        try (Connection connect = util.getConnection(); Statement statement = connect.createStatement()) {
            connect.setAutoCommit(false);
            String sqlTable = "CREATE TABLE IF NOT EXISTS users(ID BIGINT PRIMARY KEY AUTO_INCREMENT, NAME VARCHAR(40), SURNAME VARCHAR(40), AGE INT)";
            statement.executeUpdate(sqlTable);
            connect.commit();

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public void dropUsersTable() {

        try (Connection connect = util.getConnection(); Statement statement = connect.createStatement()) {
            connect.setAutoCommit(false);
            statement.executeUpdate("DROP TABLE IF EXISTS users");
            connect.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void saveUser(String name, String lastName, byte age) {
        String sql = "INSERT INTO users(NAME, SURNAME, AGE) VALUES(? ,?, ?)";
            /*
          try resource использовать  удобнее,  в комметариях писали  что,   не которые менторы
          просят сделать  через классический try.
             */
//        try (Connection connect = util.getConnection();
//             PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
//            connect.setAutoCommit(false);
//            preparedStatement.setString(1, name);
//            preparedStatement.setString(2, lastName);
//            preparedStatement.setByte(3, age);
//            preparedStatement.executeUpdate();
//            connect.commit();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        PreparedStatement preparedStatement = null;
        Connection connect = null;
        try {
            connect = util.getConnection();
            connect.setAutoCommit(false);
            preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            connect.commit();

        } catch (SQLException e) {
            try {
                connect.rollback();
            } catch (SQLException t) {
                t.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connect != null) {
                try {
                    connect.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void removeUserById(long id) {

        try (Connection connect = util.getConnection(); PreparedStatement preparedStatement = connect.prepareStatement("DELETE FROM users WHERE ID = ?")) {
            connect.setAutoCommit(false);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            connect.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public List<User> getAllUsers() {
        List<User> listUser = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection connect = util.getConnection(); ResultSet resultSet = connect.createStatement().executeQuery(sql)) {

            while (resultSet.next()) {
                User users = new User();
                connect.setAutoCommit(false);
                users.setId(resultSet.getLong("ID"));
                users.setName(resultSet.getString("NAME"));
                users.setLastName(resultSet.getString("SURNAME"));
                users.setAge(resultSet.getByte("AGE"));
                listUser.add(users);
                connect.commit();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listUser;
    }

    public void cleanUsersTable() {

        try (Connection connect = util.getConnection(); Statement statement = connect.createStatement()) {
            connect.setAutoCommit(false);
            statement.executeUpdate("TRUNCATE TABLE users");
            connect.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
