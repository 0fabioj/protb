package core.controller;

import app.controller.CustomAlert;
import core.model.Person;
import database.IDatabase;
import database.PostgreSQL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PersonController implements IDatabase {
    public static boolean save(Person p) {
        Connection conn = PostgreSQL.connect();
        String query;
        List<Object> params = new ArrayList<>();
        params.add(p.getName());
        try {
            if (check(p.getId()) > 0) {
                query = "UPDATE person SET name=? WHERE id=?;";
                params.add(p.getId());
            } else {
                query = "INSERT INTO person(id, name) VALUES(" + PostgreSQL.NextFreeId(conn, "person") + ",?);";
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
        String query = "DELETE FROM person WHERE id=" + id;
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
        String query = "SELECT count(*) FROM person WHERE id=" + id;
        try {
            return PostgreSQL.SelectCount(conn, query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            PostgreSQL.closeDatabase(conn);
        }
    }

    public static ObservableList<Person> getList()
    {
        ObservableList<Person> list1 = FXCollections.observableArrayList();
        Connection conn = PostgreSQL.connect();
        String query = "SELECT id, name, hiring, number_id, address, address_number, district, "+
                        "city, zip_code, state, country, phone1, phone2, email "+
                        "FROM person ORDER BY name ASC";
        Statement st;
        ResultSet rs;
        try{
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while(rs.next()){
                Person p = new Person();
                p.setId(rs.getInt(1));
                p.setName(rs.getString(2));
                if (rs.getObject(3) != null) { p.setHiring(rs.getDate(3).toLocalDate()); }
                p.setNumberId(rs.getString(4));
                p.setAddress(rs.getString(5));
                p.setAddressNumber(rs.getString(6));
                p.setDistrict(rs.getString(7));
                p.setCity(rs.getString(8));
                p.setZipCode(rs.getString(9));
                p.setState(rs.getInt(10));
                p.setCountry(rs.getInt(11));
                p.setPhone1(rs.getString(12));
                p.setPhone2(rs.getString(13));
                p.setEmail(rs.getString(14));
                list1.add(p);
            }
        } catch(SQLException e){
            CustomAlert.showError(e.getMessage());
        } finally {
            PostgreSQL.closeDatabase(conn);
        }
        /*System.out.println("Person getList()");
        for (Person person : list1) {
            System.out.println("id:" + person.getId() + " nome:" + person.getName());
        }*/

        return list1;
    }
}
