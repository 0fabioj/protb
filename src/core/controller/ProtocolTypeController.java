package core.controller;

import core.model.Person;
import core.model.ProtocolType;
import database.PostgreSQLConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProtocolTypeController {
    static public boolean save(ProtocolType pt) {
        Connection conn = PostgreSQLConnection.connect();
        String query;
        List params = new ArrayList<>();
        params.add(pt.getDescription());
        try {
            if (check(pt.getId()) > 0) {
                query = "UPDATE protocol_type SET description=? WHERE id=?;";
                params.add(pt.getId());
            } else {
                query = "INSERT INTO protocol_type VALUES(" + PostgreSQLConnection.NextFreeId(conn, "protocol_type") + ",?);";
            }
            return PostgreSQLConnection.ExecQuery1(conn, query, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static public boolean delete(int id)
    {
        Connection conn = PostgreSQLConnection.connect();
        String query = "DELETE FROM protocol_type WHERE id = " + id;
        try {
            return PostgreSQLConnection.ExecQuery1(conn, query, null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static private int check(int id)
    {
        Connection conn = PostgreSQLConnection.connect();
        String query = "SELECT count(*) FROM protocol_type WHERE id = " + id;
        try {
            return PostgreSQLConnection.SelectCount(conn, query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static public ObservableList<ProtocolType> getList()
    {
        ObservableList<ProtocolType> list1 = FXCollections.observableArrayList();
        Connection conn = PostgreSQLConnection.connect();
        String query = "SELECT id, description FROM protocol_type ORDER BY description ASC";
        Statement st;
        ResultSet rs;
        try{
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while(rs.next()){
                list1.add(new ProtocolType(rs.getInt("id"), rs.getString("description")));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        System.out.println("ProtocolType getList()");
        for (ProtocolType protocolType : list1) {
            System.out.println("id:" + protocolType.getId() + " nome:" + protocolType.getDescription());
        }

        return list1;
    }

}
