package app.controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.Locale;

public class Protb extends Application {
    MainWinHandle mainWinHandle;
    @Override
    public void start(Stage primaryStage) {
        Locale.setDefault(new Locale("pt","BR"));

        BorderPane root = new BorderPane();
        mainWinHandle = new MainWinHandle();
        root.setTop(mainWinHandle.ui.getMenuBar());
        root.setCenter(mainWinHandle.ui.getCenterPane());
        root.setBottom(mainWinHandle.ui.getStatusBar());

        Scene scene = new Scene(root, 800, 480);
        scene.getStylesheets().add("/style.css");

        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
        primaryStage.setTitle("Livro de Protocolo");
        primaryStage.show();
    }

    public static void main(String[] args) { launch(args); }
}
