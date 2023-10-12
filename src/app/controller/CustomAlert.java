package app.controller;

import javafx.scene.control.Alert;

public class CustomAlert {
    public static void showAlert(Alert.AlertType type, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle("[protB] Mensagem");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
