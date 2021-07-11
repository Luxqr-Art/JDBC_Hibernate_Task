package jm.task.core.jdbc.dao;
import jm.task.core.jdbc.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static jm.task.core.jdbc.util.Util.util;


public class UserDaoJDBCImpl implements UserDao {


    Connection connect = util.getConnection();

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
        try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
            connect.setAutoCommit(false);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            connect.commit();
        } catch (SQLException e) {
            rollback();
            e.printStackTrace();
        } finally {
            autoCommitOn();
        }
    }

    public void removeUserById(long id) {
        try (PreparedStatement preparedStatement = connect.prepareStatement("DELETE FROM users WHERE ID = ?")) {
            connect.setAutoCommit(false);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            connect.commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();
        } finally {
            autoCommitOn();
        }

    }

    public List<User> getAllUsers() {
        List<User> listUser = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (ResultSet resultSet = connect.createStatement().executeQuery(sql)) {
            while (resultSet.next()) {
                User users = new User();
                connect.setAutoCommit(false);
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

    void rollback() {
        try {
            connect.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void autoCommitOn() {
        try {
            connect.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
