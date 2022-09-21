package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    private static String dbUrl = "jdbc:mysql://localhost:3306/servlet_db"; //Database address
    private static String dbUser = "root"; //Database user
    private static String dbPwd = ""; //Database password
    private static String dbDriver = "com.mysql.cj.jdbc.Driver"; //Database driven

    private static Connection conn = null;

    //Get connection
    public static Connection Conn() {
        if (conn == null) {
            try {
                Class.forName(dbDriver);
                conn = DriverManager.getConnection(dbUrl,dbUser,dbPwd);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        return conn;
    }
}
