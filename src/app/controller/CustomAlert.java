package app.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class CustomAlert {
    public static void showAlert(Alert.AlertType type, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle("[protB] Mensagem");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    /*
    NONE
    CONFIRMATION
    WARNING
    INFORMATION
    ERROR
     */
    public static void showInformation(String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("[protB] Mensagem");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void showError(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("[protB] Erro");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static boolean showConfirmation(String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("[protB] Confirmação");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
        return alert.getResult() == ButtonType.OK;
    }
}
