package core.datamodel;

import core.model.Person;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PersonDataModel {
    private ObservableList<Person> personFXBeans;

    public PersonDataModel() {
        init();
    }

    private void init() {
        personFXBeans = getList();
        personFXBeans.addListener(createListener());
    }

    public ObservableList<Person> getPersonFXBeans() {
        return personFXBeans;
    }

    private ObservableList<Person> getList() {
        return FXCollections.observableArrayList(loadAll());
    }

    private ListChangeListener<Person> createListener() {
        return (ListChangeListener.Change<? extends Person> c) -> {
            while (c.next()) {
                if (c.wasRemoved() && c.wasAdded()) {
                    System.out.println("ListenerExtractor" + " gambi:"+c.getAddedSubList());
                    for (Person p : c.getAddedSubList()) {
                        update(p);
                    }
                } else {
                    if (c.wasRemoved()) {
                        System.out.println("ListenerExtractor" + " removed: "+c.getRemoved());
                        for (Person p : c.getRemoved()) {
                            del(p);
                        }
                    }
                    if (c.wasAdded()) {
                        System.out.println("ListenerExtractor" + " added: "+c.getAddedSubList());
                        for (Person p : c.getAddedSubList()) {
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

    private int insert(Person p) {
        String query = "INSERT INTO person(id, name) VALUES(?,?);";
        List<Object> params = new ArrayList<>();
        params.add(p.getId());
        params.add(p.getName());
        return PostgreSQL.ExecQuery(query, params);
    }

    private int update(Person p) {
        String query = "UPDATE person SET name=? WHERE id=?;";
        List<Object> params = new ArrayList<>();
        params.add(p.getName());
        params.add(p.getId());
        return PostgreSQL.ExecQuery(query, params);
    }

    private int del(Person p) {
        String query = "DELETE FROM person WHERE id="+p.getId();
        return PostgreSQL.ExecQuery(query,null);
    }

    private List<Person> loadAll() {
        System.out.println("Load All:");
        List<Person> list = new ArrayList<>();
        String query = "SELECT id, name "+
                "FROM person ORDER BY name ASC";
        List<Map<Integer, Object>> rs = PostgreSQL.SelectQuery(query);
        for (Map<Integer, Object> row : rs) {
            list.add(new Person((Integer) row.get(1),(String) row.get(2)));
        }
        return list;
    }

    public int generateID() {
        return PostgreSQL.NextFreeId("person");
    }
}
