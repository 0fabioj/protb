package app.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoginWin {
    Stage dialog;
    public TextField tfUser;
    public PasswordField pfPassWord;
    public Button btnCancel;
    public Button btnOK;
    public LoginWin() {
        dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);

        GridPane layout = new GridPane();
        layout.setAlignment(Pos.CENTER);

        layout.add(new ImageView(new Image("/book.png")),0,0, 1, 3);
        Label lbTitle = new Label("Livro de Protocolo");
        lbTitle.setId("title");
        layout.add(lbTitle,1,0,2,1);

        layout.add(new Label("Usuário"),1,1);
        tfUser = new TextField();
        tfUser.setPrefWidth(144.0);
        layout.add(tfUser,2,1);

        layout.add(new Label("Senha"),1,2);
        pfPassWord = new PasswordField();
        pfPassWord.setPrefWidth(144.0);
        layout.add(pfPassWord,2,2);

        btnOK = new Button("Login");
        layout.add(btnOK,2,3);
        btnCancel = new Button("Cancelar");
        layout.add(btnCancel,1,3);

        Scene scene = new Scene(layout, 360, 180);
        scene.getStylesheets().add("/style.css");
        dialog.setTitle("Login");
        dialog.setScene(scene);
    }

    public void show() {
        dialog.showAndWait();
    }
    public void close() {
        dialog.close();
    }
}
