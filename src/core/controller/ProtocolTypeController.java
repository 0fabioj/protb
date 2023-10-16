package core.controller;

import app.controller.CustomAlert;
import core.model.ProtocolType;
import database.IDatabase;
import database.PostgreSQL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProtocolTypeController implements IDatabase {
    public static boolean save(ProtocolType pt) {
        Connection conn = PostgreSQL.connect();
        String query;
        List<Object> params = new ArrayList<>();
        params.add(pt.getDescription());
        try {
            if (check(pt.getId()) > 0) {
                query = "UPDATE protocol_type SET description=? WHERE id=?;";
                params.add(pt.getId());
            } else {
                query = "INSERT INTO protocol_type VALUES(" + PostgreSQL.NextFreeId(conn, "protocol_type") + ",?);";
            }
            return PostgreSQL.ExecQuery1(conn, query, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            PostgreSQL.closeDatabase(conn);
        }
    }

    public static boolean delete(int id)
    {
        Connection conn = PostgreSQL.connect();
        String query = "DELETE FROM protocol_type WHERE id=" + id;
        try {
            return PostgreSQL.ExecQuery1(conn, query, null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            PostgreSQL.closeDatabase(conn);
        }
    }

    public static int check(int id)
    {
        Connection conn = PostgreSQL.connect();
        String query = "SELECT count(*) FROM protocol_type WHERE id=" + id;
        try {
            return PostgreSQL.SelectCount(conn, query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            PostgreSQL.closeDatabase(conn);
        }
    }

    public static ObservableList<ProtocolType> getList()
    {
        ObservableList<ProtocolType> list1 = FXCollections.observableArrayList();
        Connection conn = PostgreSQL.connect();
        String query = "SELECT id, description FROM protocol_type ORDER BY description ASC;";
        Statement st;
        ResultSet rs;
        try{
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while(rs.next()){
                list1.add(new ProtocolType(rs.getInt(1), rs.getString(2)));
            }
        } catch(SQLException e){
            CustomAlert.showError(e.getMessage());
        } finally {
            PostgreSQL.closeDatabase(conn);
        }

        /*System.out.println("ProtocolType getList()");
        for (ProtocolType protocolType : list1) {
            System.out.println("id:" + protocolType.getId() + " nome:" + protocolType.getDescription());
        }*/

        return list1;
    }

}
