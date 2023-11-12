package carsharing;

import java.sql.*;

public class Database {
    static final String JDBC_DRIVER = "org.h2.Driver";
    static String DB_URL = "jdbc:h2:./src/carsharing/db/carsharing";
    static Connection conn;
    static Statement stmt;



    public static void connectToDB() {
        conn = null;
        stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL);
            stmt = conn.createStatement();

        } catch (SQLException | ClassNotFoundException se) {
            se.printStackTrace();

        }

    }

    static void executeUpdate(String query) throws SQLException {
        stmt.executeUpdate(query);
        conn.setAutoCommit(true);
    }

    static ResultSet executeQuery(String query) throws SQLException {
        return stmt.executeQuery(query);
    }

    public static void closeConnection() {
        try {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public static void runInsert(String sql2) throws SQLException {
        connectToDB();
        executeUpdate(sql2);
        closeConnection();
    }

    public static ResultSet runSelect(String query) throws SQLException {
        connectToDB();
        return executeQuery(query);
    }

}