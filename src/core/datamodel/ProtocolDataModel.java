package core.datamodel;

import core.model.Person;
import core.model.Protocol;
import core.model.ProtocolType;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProtocolDataModel {
    private ObservableList<Protocol> protocolFXBeans;

    public ProtocolDataModel() {
        init();
    }

    private void init() {
        protocolFXBeans = getList();
        protocolFXBeans.addListener(createListener());
    }

    public ObservableList<Protocol> getProtocolFXBeans() {
        return protocolFXBeans;
    }

    private ObservableList<Protocol> getList()
    {
        //return FXCollections.observableArrayList(Protocol.extractor());
        return FXCollections.observableArrayList(loadAll());
    }

    private ListChangeListener<Protocol> createListener() {
        return (ListChangeListener.Change<? extends Protocol> c) -> {
            while (c.next()) {
                if (c.wasRemoved() && c.wasAdded()) {
                    System.out.println("ListenerExtractor" + " gambi:"+c.getAddedSubList());
                    for (Protocol p : c.getAddedSubList()) {
                        update(p);
                    }
                } else {
                    if (c.wasRemoved()) {
                        System.out.println("ListenerExtractor" + " removed: "+c.getRemoved());
                        for (Protocol p : c.getRemoved()) {
                            del(p);
                        }
                    }
                    if (c.wasAdded()) {
                        System.out.println("ListenerExtractor" + " added: "+c.getAddedSubList());
                        for (Protocol p : c.getAddedSubList()) {
                            insert(p);
                        }
                    }
                }

                if (c.wasUpdated()) {
                    System.out.println("ListenerExtractor" + " updated");
                }
            }
        };
    }

    private int insert(Protocol p) {
        String query = "INSERT INTO protocol VALUES(?,?,?,?,?,?,?,?,?,?,?,now(),null);";
        List<Object> params = new ArrayList<>();
        params.add(p.getId());
        params.add(p.person().getId());
        params.add(p.protocolType().getId());
        params.add(p.getSummary());
        params.add(p.getPath());
        params.add(p.getStatusInt());
        params.add(p.getRecorded());
        params.add(p.getRequested());
        params.add(p.getReceipted());
        params.add(p.getForwarded());
        params.add(p.getChecked());
        return PostgreSQL.ExecQuery(query, params);
    }

    private int update(Protocol p) {
        String query = "UPDATE protocol SET person_id=?, type_id=?, summary=?, path=?, status=?, "+
                "recorded=?, requested=?, receipted=?, forwarded=?, checked=?, altered=now() "+
                "WHERE id=?;";
        List<Object> params = new ArrayList<>();
        params.add(p.person().getId());
        params.add(p.protocolType().getId());
        params.add(p.getSummary());
        params.add(p.getPath());
        params.add(p.getStatusInt());
        params.add(p.getRecorded());
        params.add(p.getRequested());
        params.add(p.getReceipted());
        params.add(p.getForwarded());
        params.add(p.getChecked());
        params.add(p.getId());
        return PostgreSQL.ExecQuery(query, params);
    }

    private int del(Protocol p) {
        String query = "DELETE FROM protocol WHERE id="+p.getId();
        return PostgreSQL.ExecQuery(query,null);
    }

    public static Protocol load(int id) {
        System.out.println("Load:"+id);
        String query = "SELECT p.id, p.recorded, pe.id, pe.name, pt.id, pt.description, "
                +"p.summary, p.path, p.requested, p.status, "
                +"p.receipted, p.forwarded, p.checked, p.created, p.altered "
                +"FROM protocol p "
                +"INNER JOIN person pe ON (pe.id=p.person_id) "
                +"INNER JOIN protocol_type pt ON (pt.id=p.type_id) "
                +"WHERE p.id="+id+";";
        ResultSet rs = PostgreSQL.SelectQuery(query);
        while(true){
            try {
                if (!rs.next()) break;
                return new Protocol(
                        rs.getInt(1),
                        rs.getDate(2).toLocalDate(),
                        new Person(rs.getInt(3),rs.getString(4)),
                        new ProtocolType(rs.getInt(5),rs.getString(6)),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getDate(9).toLocalDate(),
                        rs.getInt(10),
                        rs.getObject(11) != null ? rs.getDate(11).toLocalDate() : LocalDate.now(),
                        rs.getObject(12) != null ? rs.getDate(12).toLocalDate() : LocalDate.now(),
                        rs.getObject(13) != null ? rs.getDate(13).toLocalDate() : LocalDate.now()
                );
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    private List<Protocol> loadAll() {
        System.out.println("Load All Protocols:");
        List<Protocol> list = new ArrayList<>();
        String query = "SELECT p.id, p.recorded, pe.id, pe.name, pt.id, pt.description, "
                +"p.summary, p.path, p.requested, p.status, "
                +"p.receipted, p.forwarded, p.checked, p.created, p.altered "
                +"FROM protocol p "
                +"INNER JOIN person pe ON (pe.id=p.person_id) "
                +"INNER JOIN protocol_type pt ON (pt.id=p.type_id) "
                +"ORDER BY p.id DESC;";
        ResultSet rs = PostgreSQL.SelectQuery(query);
        while(true){
            try {
                if (!rs.next()) break;
                list.add(new Protocol(
                        rs.getInt(1),
                        rs.getDate(2).toLocalDate(),
                        new Person(rs.getInt(3),rs.getString(4)),
                        new ProtocolType(rs.getInt(5),rs.getString(6)),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getDate(9).toLocalDate(),
                        rs.getInt(10),
                        rs.getObject(11) != null ? rs.getDate(11).toLocalDate() : LocalDate.now(),
                        rs.getObject(12) != null ? rs.getDate(12).toLocalDate() : LocalDate.now(),
                        rs.getObject(13) != null ? rs.getDate(13).toLocalDate() : LocalDate.now()
                ));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return list;
    }
}
