package app.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
    @FXML
    Button okButton;
    @FXML
    TextField userIdField;
    @FXML
    PasswordField userPasswordField;
    @FXML
    Label errorLabel;

    @FXML
    protected void action_okButton(ActionEvent event) throws IOException {
        String userId = userIdField.getText();
        String userPassword = String.valueOf(userPasswordField.getText());
        HashMap<String,String> logininfo = new HashMap<String,String>();
        IPasswords iPasswords = new IPasswords();
        logininfo = iPasswords.getLoginInfo();

        if(logininfo.containsKey(userId)) {
            if(logininfo.get(userId).equals(userPassword)) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/view/MainWindow.fxml"));
                Parent root = loader.load();
                //root = FXMLLoader.load(getClass().getResource("/app/view/MainWindow.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
                stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);

                stage.focusedProperty().addListener(new ChangeListener<Boolean>()
                {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> ov, Boolean onHidden, Boolean onShown)
                    {
                        if(onShown) {
                            MainWindow mainWindow = loader.getController();
                            mainWindow.updateTableView();
                        }
                    }
                });
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
