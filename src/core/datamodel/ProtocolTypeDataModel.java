package core.datamodel;

import core.model.Person;
import core.model.ProtocolType;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProtocolTypeDataModel {
    private ObservableList<ProtocolType> protocolTypeFXBeans;

    public ProtocolTypeDataModel() {
        init();
    }

    private void init() {
        protocolTypeFXBeans = getList();
        protocolTypeFXBeans.addListener(createListener());
    }

    public ObservableList<ProtocolType> getProtocolTypeFXBeans() {
        return protocolTypeFXBeans;
    }

    private ObservableList<ProtocolType> getList() {
        return FXCollections.observableArrayList(loadAll());
    }

    private ListChangeListener<ProtocolType> createListener() {
        return (ListChangeListener.Change<? extends ProtocolType> c) -> {
            while (c.next()) {
                if (c.wasRemoved() && c.wasAdded()) {
                    System.out.println("ListenerExtractor" + " gambi:"+c.getAddedSubList());
                    for (ProtocolType pt : c.getAddedSubList()) {
                        update(pt);
                    }
                } else {
                    if (c.wasRemoved()) {
                        System.out.println("ListenerExtractor" + " removed: "+c.getRemoved());
                        for (ProtocolType pt : c.getRemoved()) {
                            del(pt);
                        }
                    }
                    if (c.wasAdded()) {
                        System.out.println("ListenerExtractor" + " added: "+c.getAddedSubList());
                        for (ProtocolType pt : c.getAddedSubList()) {
                            insert(pt);
                        }
                    }
                }

                if (c.wasUpdated()) {
                    System.out.println("ListenerExtractor" + " updated");
                }
            }
        };
    }

    private int insert(ProtocolType p) {
        String query = "INSERT INTO protocol_type(id, description) VALUES(?,?);";
        List<Object> params = new ArrayList<>();
        params.add(p.getId());
        params.add(p.getName());
        return PostgreSQL.ExecQuery(query, params);
    }

    private int update(ProtocolType p) {
        String query = "UPDATE protocol_type SET description=? WHERE id=?;";
        List<Object> params = new ArrayList<>();
        params.add(p.getName());
        params.add(p.getId());
        return PostgreSQL.ExecQuery(query, params);
    }

    private int del(ProtocolType p) {
        String query = "DELETE FROM protocol_type WHERE id="+p.getId();
        return PostgreSQL.ExecQuery(query,null);
    }

    private List<ProtocolType> loadAll() {
        List<ProtocolType> list = new ArrayList<>();
        String query = "SELECT id, description "+
                "FROM protocol_type ORDER BY description ASC";
        List<Map<Integer, Object>> rs = PostgreSQL.SelectQuery(query);
        for (Map<Integer, Object> row : rs) {
            list.add(new ProtocolType((Integer) row.get(1),(String) row.get(2)));
        }
        return list;
    }

    public int generateID() {
        return PostgreSQL.NextFreeId("protocol_type");
    }
}