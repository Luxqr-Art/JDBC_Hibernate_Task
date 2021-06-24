package jm.task.core.jdbc;


import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {

    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        // реализуйте алгоритм здесь
        userService.createUsersTable();

        userService.saveUser("Криштиану", "Роналду", (byte) 36);
        userService.saveUser("Лионель", "Месси", (byte) 33);
        userService.saveUser("Златан", "Ибрагимович", (byte) 39);

        userService.getAllUsers();
        System.out.println("<------------------------------------------------------------------->");
        userService.removeUserById(2);
//        userService.getAllUsers();

        userService.cleanUsersTable();
        userService.dropUsersTable();


    }
}
