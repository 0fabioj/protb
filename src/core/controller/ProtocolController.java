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
        params.add(p.getStatusInt());
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
        String query = "SELECT p.id, pe.id, pe.name, pt.id, pt.description, "
                +"p.summary, p.recorded, p.status, p.path, "
                +"p.recorded, p.requested, p.receipted, p.forwarded, p.checked, p.created, p.altered "
                +"FROM protocol p "
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
                protocol.setPerson(new Person(rs.getInt(2),rs.getString(3)));
                protocol.setProtocolType(new ProtocolType(rs.getInt(4),rs.getString(5)));
                protocol.setSummary(rs.getString(6));
                protocol.setRecorded(rs.getDate(7).toLocalDate());
                protocol.setStatus(rs.getInt(8));
                protocol.setPath(rs.getString(9));
                //protocol.setRecorded(rs.getDate(10).toLocalDate()); }
                //protocol.setRequested(rs.getDate(11).toLocalDate());
                //protocol.setReceipted(rs.getDate(12).toLocalDate());
                if(rs.getObject(13) != null) { protocol.setForwarded(rs.getDate(13).toLocalDate()); }
                if(rs.getObject(14) != null) { protocol.setChecked(rs.getDate(14).toLocalDate()); }
                protocol.setCreated(rs.getTimestamp(15).toLocalDateTime());
                if(rs.getObject(16) != null) { protocol.setAltered(rs.getTimestamp(16).toLocalDateTime()); }
                protocolList.add(protocol);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return protocolList;
    }
}
