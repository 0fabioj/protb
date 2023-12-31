package app.view;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class CustomAlert {
    /*
    NONE
    CONFIRMATION
    WARNING
    INFORMATION
    ERROR
     */
    public static void showInformation(String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Mensagem");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void showError(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static boolean showConfirmation(String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
        return alert.getResult() == ButtonType.OK;
    }
}