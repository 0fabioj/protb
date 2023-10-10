package core.controller;

import core.model.Person;
import database.PostgreSQLConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PersonController {
    static public boolean save(Person p) {
        Connection conn = PostgreSQLConnection.connect();
        String query;
        List params = new ArrayList<>();
        params.add(p.getName());
        try {
            if (check(p.getId()) > 0) {
                query = "UPDATE person SET name=? WHERE id=?;";
                params.add(p.getId());
            } else {
                query = "INSERT INTO person VALUES(" + PostgreSQLConnection.NextFreeId(conn, "person") + ",?);";
            }
            return PostgreSQLConnection.ExecQuery1(conn, query, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static public boolean delete(int id)
    {
        Connection conn = PostgreSQLConnection.connect();
        String query = "DELETE FROM person WHERE id = " + id;
        try {
            return PostgreSQLConnection.ExecQuery1(conn, query, null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static private int check(int id)
    {
        Connection conn = PostgreSQLConnection.connect();
        String query = "SELECT count(*) FROM person WHERE id = " + id;
        try {
            return PostgreSQLConnection.SelectCount(conn, query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static public ObservableList<Person> getList()
    {
        ObservableList<Person> list1 = FXCollections.observableArrayList();
        Connection conn = PostgreSQLConnection.connect();
        String query = "SELECT id, name FROM person ORDER BY name ASC";
        Statement st;
        ResultSet rs;
        try{
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while(rs.next()){
                list1.add(new Person(rs.getInt("id"), rs.getString("name")));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        System.out.println("Person getList()");
        for (Person person : list1) {
            System.out.println("id:" + person.getId() + " nome:" + person.getName());
        }

        return list1;
    }
}
