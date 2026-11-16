package cinema.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "3306";
    private static final String DB_NAME = "cinema_db";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    private static final String DB_URL = String.format(
            "jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Ho_Chi_Minh",
            DB_HOST, DB_PORT, DB_NAME
    );

    private static Connection connection = null;
    private static Boolean isConnectedToDB = null;

    private DBConnection() {
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {

                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                } catch (ClassNotFoundException ignored) {
                    try {
                        Class.forName("com.mysql.jdbc.Driver");
                    } catch (ClassNotFoundException ignored2) {
                    }
                }
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                isConnectedToDB = true;
            }
        } catch (SQLException e) {
            isConnectedToDB = false;

            return null;
        }
        return connection;
    }

    public static boolean isConnected() {
        if (isConnectedToDB != null) {
            return isConnectedToDB;
        }
        Connection conn = getConnection();
        return conn != null;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Lỗi đóng kết nối CSDL: " + e.getMessage());
            } finally {
                connection = null;
                isConnectedToDB = null;
            }
        }
    }
}

