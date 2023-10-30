package database;

import app.controller.CustomAlert;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.List;

public class PostgreSQL {
    public static Connection conn;
    final private static String url = "jdbc:postgresql://192.168.1.156:5432/protb";
    final private static String user = "pgsql";
    final private static String password = "1234";

    public static Connection connect() {
        conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            //System.out.println("Opened database successfully");
        } catch (SQLException e) {
            CustomAlert.showError(e.getMessage());
        }
        return conn;
    }

    public static Connection getConnection() throws SQLException {
        if(conn !=null && !conn.isClosed())
            return conn;
        connect();
        return conn;
    }

    public static void closeDatabase(Connection conn) {
        try { conn.close(); } catch (Exception e) { /* Ignored */ }
        //System.out.println("Closed database");
    }
    public static void closeDatabase(PreparedStatement ps) {
        try { ps.close(); } catch (Exception e) { /* Ignored */ }
    }
    public static void closeDatabase(ResultSet rs) {
        try { rs.close(); } catch (Exception e) { /* Ignored */ }
    }

    public static boolean ExecQuery1(Connection conn, String query, List params) throws SQLException {
        //System.out.println("ExecQuery1");
        //System.out.println(query);
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            int result = -1;
            int index = 1;
            if (null != params && !params.isEmpty()) {
                //for (int i = 0; i < params.size(); i ++) {
                for (Object param : params) {
                    pstmt.setObject(index++, param);
                }
            }
            result = pstmt.executeUpdate();
            return result > 0 ? true : false;
        } catch (SQLException e) {
            CustomAlert.showError(e.getMessage());
            return false;
        }
    }

    public static int SelectCount(Connection conn, String query) throws SQLException {
        int size = 0;
        try (Statement stmt = conn.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    size = rs.getInt(1);
                }
            }
        }
        return size;
    }

    public static int NextFreeId(String table) throws SQLException {
        var query = "SELECT MAX(id)+1 FROM " + table + ";";
        int nextId = 0;
        try (Connection conn = PostgreSQL.connect()) {
            try (Statement stmt = conn.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(query)) {
                    while (rs.next()) {
                        if (rs.getObject(1) != null) {
                            nextId = rs.getInt(1);
                        } else {
                            nextId = 1;
                        }
                    }
                }
            }
        }
        return nextId;
    }

    public static int ExecQuery2(String query, List params) throws SQLException {
        try (Connection conn = PostgreSQL.getConnection()) {
            //try (Connection conn = PostgreSQL.connect()) {
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                int index = 1;
                if (null != params && !params.isEmpty()) {
                    for (Object param : params) {
                        pstmt.setObject(index++, param);
                    }
                }
                return pstmt.executeUpdate();
            } catch (SQLException e) {
                CustomAlert.showError(e.getMessage());
                return -1;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            PostgreSQL.closeDatabase(conn);
        }
    }

    /*public static boolean IsReady() {
        boolean res = false;
        try (Connection conn = PostgreSQL.getConnection()) {
            if (conn != null) {
                System.out.println("Connected to the database!");
                res = true;
            } else {
                System.out.println("Failed to make connection!");
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
        return res;
    }*/
}
