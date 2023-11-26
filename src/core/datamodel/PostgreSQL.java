package core.datamodel;

import app.controller.Utils;
import app.view.CustomAlert;

import java.sql.*;
import java.util.List;

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

    public static ResultSet SelectQuery(String query) {
        ResultSet rs;
        try (Connection conn = PostgreSQL.getConnection()) {
            try (Statement stmt = conn.createStatement()) {
                rs = stmt.executeQuery(query);
                return rs;
            }
        } catch (SQLException e) {
            CustomAlert.showError(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            PostgreSQL.closeDatabase(conn);
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