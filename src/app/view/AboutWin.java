package app.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AboutWin {
    Stage dialog;
    public AboutWin() {
        dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);

        GridPane layout = new GridPane();
        layout.setAlignment(Pos.CENTER);

        layout.add(new ImageView(new Image("/book.png")),0,0, 1, 3);
        layout.add(new Label("Livro de Protocolo"),1,0);
        layout.add(new Label("JavaFX"),1,1);
        layout.add(new Label("0fabioj"),1,2);

        Button btnClose = new Button("Fechar");
        btnClose.setOnAction(e -> dialog.close());
        layout.add(btnClose,1,3);

        Scene scene = new Scene(layout, 300, 150);
        scene.getStylesheets().add("/style.css");
        dialog.setTitle("Sobre");
        dialog.setScene(scene);
    }

    public void show() {
        dialog.show();
    }
}
