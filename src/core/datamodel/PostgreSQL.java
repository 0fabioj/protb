package core.datamodel;

import app.controller.Utils;
import app.view.CustomAlert;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostgreSQL {
    public static Connection conn;
    final private static String url = "jdbc:postgresql://" + Utils.getServerConfig() + "/protb";
    final private static String user = "pgsql";
    final private static String password = "1234";

    public static Connection connect() {
        conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
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
        try { conn.close(); } catch (Exception ignored) { }
    }
    public static void closeDatabase(PreparedStatement ps) {
        try { ps.close(); } catch (Exception ignored) { }
    }
    public static void closeDatabase(ResultSet rs) {
        try { rs.close(); } catch (Exception ignored) { }
    }

    public static int ExecQuery(String query, List<Object> params) {
        try (Connection conn = PostgreSQL.getConnection()) {
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

    public static List<Map<Integer,Object>> SelectQuery(String query) {

        List<Map<Integer,Object>> resultList = new ArrayList<>();
        Map<Integer,Object> row = null;

        try (
                Connection conn = PostgreSQL.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
        ) {
            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();
            while (rs.next()) {
                row = new HashMap<Integer,Object>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(i,rs.getObject(i));
                }
                resultList.add(row);
            }
            return resultList;
        } catch (SQLException ex) {
            CustomAlert.showError(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    public static int NextFreeId(String table) {
        var query = "SELECT MAX(id)+1 FROM " + table + ";";
        int nextId = 0;
        try (Connection conn = PostgreSQL.getConnection()) {
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return nextId;
    }
}