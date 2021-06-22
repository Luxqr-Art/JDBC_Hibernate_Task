package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class UserDaoJDBCImpl implements UserDao {

    Util utils = new Util();

    Connection connect = utils.getConnection();


    public UserDaoJDBCImpl() {


    }

    public void createUsersTable() {

        try (Statement statement = connect.createStatement()) {
            String sqlTable = "CREATE TABLE IF NOT EXISTS users(ID BIGINT PRIMARY KEY AUTO_INCREMENT, NAME VARCHAR(40), SURNAME VARCHAR(40), AGE INT)";
            statement.executeUpdate(sqlTable);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {

        try (Statement statement = connect.createStatement()) {

            statement.executeUpdate("DROP TABLE IF EXISTS users");

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
//        try (PreparedStatement preparedStatement = connect.prepareStatement(sql)){
//            preparedStatement.setString(1, name);
//            preparedStatement.setString(2, lastName);
//            preparedStatement.setByte(3, age);
//            preparedStatement.executeUpdate();
//
//        }
//        catch (SQLException e) {
//            e.printStackTrace();
//        }
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    public void removeUserById(long id) {

        try (PreparedStatement preparedStatement = connect.prepareStatement("DELETE FROM users WHERE ID = ?")) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public List<User> getAllUsers() {
        List<User> listUser = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (ResultSet resultSet = connect.createStatement().executeQuery(sql)) {

            while (resultSet.next()) {
                User users = new User();
                users.setId(resultSet.getLong("ID"));
                users.setName(resultSet.getString("NAME"));
                users.setLastName(resultSet.getString("SURNAME"));
                users.setAge(resultSet.getByte("AGE"));
                listUser.add(users);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listUser;
    }

    public void cleanUsersTable() {
        try (Statement statement = connect.createStatement()) {
            statement.executeUpdate("TRUNCATE TABLE users");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}