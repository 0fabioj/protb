package core.datamodel;

import app.controller.Utils;
import core.model.Person;
import core.model.Protocol;
import core.model.ProtocolType;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        List<Map<Integer, Object>> rs = PostgreSQL.SelectQuery(query);
        Protocol result = null;
        for (Map<Integer, Object> row : rs) {
            result = new Protocol(
                    (Integer) row.get(1),
                    Utils.sqlTimeStampToLocalDate(row.get(2)),
                    new Person((Integer) row.get(3),(String) row.get(4)),
                    new ProtocolType((Integer) row.get(5),(String) row.get(6)),
                    (String) row.get(7),
                    (String) row.get(8),
                    Utils.sqlTimeStampToLocalDate(row.get(9)),
                    (Integer) row.get(10),
                    row.get(11) != null ? Utils.sqlTimeStampToLocalDate(row.get(11)) : LocalDate.now(),
                    row.get(12) != null ? Utils.sqlTimeStampToLocalDate(row.get(12)) : LocalDate.now(),
                    row.get(13) != null ? Utils.sqlTimeStampToLocalDate(row.get(13)) : LocalDate.now()
            );
        }
        return result;
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
        List<Map<Integer, Object>> rs = PostgreSQL.SelectQuery(query);
        for (Map<Integer, Object> row : rs) {
            list.add(new Protocol(
                    (Integer) row.get(1),
                    Utils.sqlTimeStampToLocalDate(row.get(2)),
                    new Person((Integer) row.get(3),(String) row.get(4)),
                    new ProtocolType((Integer) row.get(5),(String) row.get(6)),
                    (String) row.get(7),
                    (String) row.get(8),
                    Utils.sqlTimeStampToLocalDate(row.get(9)),
                    (Integer) row.get(10),
                    row.get(11) != null ? Utils.sqlTimeStampToLocalDate(row.get(11)) : LocalDate.now(),
                    row.get(12) != null ? Utils.sqlTimeStampToLocalDate(row.get(12)) : LocalDate.now(),
                    row.get(13) != null ? Utils.sqlTimeStampToLocalDate(row.get(13)) : LocalDate.now()
            ));
        }
        return list;
    }
}
