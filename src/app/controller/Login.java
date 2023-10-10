package app.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class Login {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    Button cancelButton;
    @FXML
    Button okButton;
    @FXML
    TextField userIdField;
    @FXML
    PasswordField userPasswordField;
    @FXML
    Label errorLabel;

    @FXML
    protected void action_cancelButton(ActionEvent event) throws IOException {
        Platform.exit();
    }

    @FXML
    protected void action_okButton(ActionEvent event) throws IOException {
        String userId = userIdField.getText();
        String userPassword = String.valueOf(userPasswordField.getText());
        HashMap<String,String> logininfo = new HashMap<String,String>();
        IPasswords iPasswords = new IPasswords();
        logininfo = iPasswords.getLoginInfo();

        if(logininfo.containsKey(userId)) {
            if(logininfo.get(userId).equals(userPassword)) {
                root = FXMLLoader.load(getClass().getResource("/app/view/MainWindow.fxml"));
                stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
                stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
            }
            else {
                errorLabel.setText("errou senha!");
            }
        }
        else {
            errorLabel.setText("errou usuario!");
        }
    }
}
