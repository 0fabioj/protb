package core.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Person {
    private final IntegerProperty id;
    private final StringProperty name;

    public Person(int id, String name) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
    }

    public int getId() {
        return idProperty().get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        idProperty().set(id);
    }

    public String getName() {
        return nameProperty().get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        nameProperty().set(name);
    }

    @Override
    public String toString() {
        return getName();
    }
}
