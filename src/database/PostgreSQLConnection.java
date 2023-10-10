package database;

import javafx.util.Pair;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgreSQLConnection {
    public static Connection conn;
    final private static String url = "jdbc:postgresql://192.168.1.154:5432/protb";
    final private static String user = "pgsql";
    final private static String password = "1234";

    public static Connection connect() {
        conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            //System.out.println("Opened database successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static Connection getConnection() throws SQLException {
        if(conn !=null && !conn.isClosed())
            return conn;
        connect();
        return conn;
    }

    public static boolean ExecQuery1(Connection conn, String query, List params) throws SQLException {
        System.out.println("ExecQuery1");
        System.out.println(query);
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            int result = -1;
            int index = 1;
            if (null != params && !params.isEmpty()) {
                for (int i = 0; i < params.size(); i ++) {
                    pstmt.setObject(index ++, params.get(i));
                }
            }
            result = pstmt.executeUpdate();
            return result > 0 ? true : false;
        } catch (SQLException s) {
            System.out.println(s.getMessage());
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static int SelectCount(Connection conn, String query) throws  SQLException {
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

    public static int NextFreeId(Connection conn, String table) throws SQLException {
        var query = "SELECT MAX(id)+1 FROM " + table;
        int nextId = 0;
        try (Statement stmt = conn.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    nextId = rs.getInt(1);
                }
            }
        }
        return nextId;
    }


}
