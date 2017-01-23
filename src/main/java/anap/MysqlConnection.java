package anap;

import java.sql.*;

public class MysqlConnection {

    Connection conn = null;

    public static Connection dbConnector() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/anap", "John", "123");
            return conn;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static Connection dbConnector(String fn, String ln) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/anap", "John", "123");
            return conn;
        } catch (Exception e) {
            System.out.println("e");
            return null;
        }
    }
}
