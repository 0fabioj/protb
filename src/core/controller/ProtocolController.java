package core.controller;

import core.model.Person;
import core.model.Protocol;
import core.model.ProtocolType;
import database.PostgreSQLConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProtocolController {
    static public boolean save(Protocol p) {
        Connection conn = PostgreSQLConnection.connect();
        String query;
        List<Object> params = new ArrayList<>();
        params.add(p.getPerson().getId());
        params.add(p.getProtocolType().getId());
        params.add(p.getSummary());
        params.add(p.getPath());
        params.add(p.getStatus());
        params.add(p.getRecorded());
        params.add(p.getRequested());
        params.add(p.getReceipted());
        params.add(p.getForwarded());
        params.add(p.getChecked());
        try {
            if (check(p.getId()) > 0) {
                query = "UPDATE protocol SET person_id=?, type_id=?, summary=?, path=?, status=?, "+
                        "recorded=?, requested=?, receipted=?, forwarded=?, checked=?, altered=now() "+
                        "WHERE id=?;";
                params.add(p.getId());
            } else {
                query = "INSERT INTO protocol VALUES(" + PostgreSQLConnection.NextFreeId(conn, "protocol") +
                        ",?,?,?,?,?,?,?,?,?,?,now(),null);";
            }
            return PostgreSQLConnection.ExecQuery1(conn, query, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static public boolean delete(int id)
    {
        Connection conn = PostgreSQLConnection.connect();
        String query = "DELETE FROM protocol WHERE id = " + id;
        try {
            return PostgreSQLConnection.ExecQuery1(conn, query, null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static private int check(int id)
    {
        Connection conn = PostgreSQLConnection.connect();
        String query = "SELECT count(*) FROM protocol WHERE id = " + id;
        try {
            return PostgreSQLConnection.SelectCount(conn, query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static public ObservableList<Protocol> getList()
    {
        ObservableList<Protocol> protocolList = FXCollections.observableArrayList();
        Connection conn = PostgreSQLConnection.connect();
        String query = "SELECT p.id, pe.name, pt.description, p.recorded, p.status FROM protocol p "
                +"INNER JOIN person pe ON (pe.id=p.person_id) "
                +"INNER JOIN protocol_type pt ON (pt.id=p.type_id)";
        Statement st;
        ResultSet rs;
        try{
            st = conn.createStatement();
            rs = st.executeQuery(query);
            Protocol protocol;

            while(rs.next()) {
                protocol = new Protocol();
                protocol.setId(rs.getInt(1));
                protocol.setPerson(new Person(0,rs.getString(2)));
                protocol.setProtocolType(new ProtocolType(0,rs.getString(3)));
                protocol.setRecorded(rs.getDate(4).toLocalDate());
                protocol.setStatus(rs.getInt(5));
                protocolList.add(protocol);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return protocolList;
    }
}
