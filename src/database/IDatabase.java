package database;

import javafx.collections.ObservableList;

public interface IDatabase {
    static boolean save(Object obj) {
        return false;
    }

    static boolean delete(int id) {
        return false;
    }

    static int check(int id) {
        return 0;
    }

    static ObservableList<Object> getList() {
        return null;
    }
}
