package app.controller;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Locale;

public class Protb extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Locale.setDefault(new Locale("pt","BR"));
        stage.getIcons().add(new Image("/book.png"));

        Parent loader = FXMLLoader.load(getClass().getResource("/app/view/Login.fxml"));

        stage.setScene(new Scene(loader));
        stage.setTitle("[protB] Livro de Protocolo");
        stage.show();
    }

    public static void main(String[] args) { launch(args); }

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
}
