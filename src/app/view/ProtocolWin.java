package app.view;

import app.controller.Utils;
import core.model.Person;
import core.model.ProtocolType;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProtocolWin {
    Stage dialog;
    ToolBar toolBar;
    public Button btnSave;
    public Button btnDel;
    public Button btnClear;
    public GridPane grid;
    public TextField tfId;
    public DatePicker dpDate1;
    public ComboBox<Person> cbPerson;
    public ComboBox<ProtocolType> cbProtocolType;
    public TextArea taSummary;
    public DatePicker dpDate2;
    public ComboBox<String> cbStatus;
    public DatePicker dpDate3;
    public DatePicker dpDate4;
    public DatePicker dpDate5;
    private final HBox statusBar;

    public ProtocolWin() {
        dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);

        setToolBar();
        setForm();

        statusBar = new HBox();
        List<String> statusMgs = new ArrayList<>();
        statusMgs.add("Ready");
        setStatusBar(statusMgs);

        VBox root = new VBox();
        root.getChildren().addAll(toolBar,grid,statusBar);

        Scene scene = new Scene(root, 440.0, 440.0);
        scene.getStylesheets().add(Utils.getCSS());
        dialog.setTitle("Protocolo");
        dialog.setResizable(false);
        dialog.setScene(scene);
    }

    public void show() {
        dialog.showAndWait();
    }
    public void close() {
        dialog.close();
    }

    private void setToolBar() {
        btnSave = new Button("Save");
        btnSave.setOnAction(e -> dialog.close());
        btnDel = new Button("Del");
        btnDel.setOnAction(e -> dialog.close());
        btnClear = new Button("Clear");
        btnClear.setOnAction(e -> clearForm());
        Button btnCancel = new Button("Cancel");
        btnCancel.setOnAction(e -> dialog.close());
        toolBar = new ToolBar();
        toolBar.getItems().add(btnSave);
        toolBar.getItems().add(btnDel);
        toolBar.getItems().add(btnClear);
        toolBar.getItems().add(btnCancel);
    }

    private void setForm() {
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        tfId = new TextField();
        tfId.setPrefWidth(72.0);
        tfId.setMaxWidth(72.0);
        tfId.setEditable(false);
        VBox vBoxItem1 = new VBox(new Label("Número:"),tfId);
        grid.add(vBoxItem1,0,0);

        dpDate1 = new DatePicker();
        dpDate1.setPrefWidth(154.0);
        VBox vBoxItem2 = new VBox(new Label("Data do Protocolo:"),dpDate1);
        grid.add(vBoxItem2,1,0,4,1);

        cbPerson = new ComboBox<>();
        cbPerson.setPrefWidth(284.0);
        VBox vBoxItem3 = new VBox(new Label("Requerente:"),cbPerson);
        grid.add(vBoxItem3,0,1,6,1);

        cbProtocolType = new ComboBox<>();
        cbProtocolType.setPrefWidth(284.0);
        VBox vBoxItem4 = new VBox(new Label("Tipo:"),cbProtocolType);
        grid.add(vBoxItem4,0,2,6,1);

        taSummary = new TextArea();
        taSummary.setPrefWidth(392.0);
        VBox vBoxItem5 = new VBox(new Label("Resumo:"),taSummary);
        grid.add(vBoxItem5,0,3,6,1);

        dpDate2 = new DatePicker();
        dpDate2.setPrefWidth(154.0);
        VBox vBoxItem7 = new VBox(new Label("Data do Documento:"),dpDate2);
        grid.add(vBoxItem7,0,4,3,1);

        cbStatus = new ComboBox<>();
        cbStatus.setPrefWidth(124.0);
        VBox vBoxItem6 = new VBox(new Label("Situação:"),cbStatus);
        grid.add(vBoxItem6,0,5,3,1);

        dpDate3 = new DatePicker();
        dpDate3.setPrefWidth(154.0);
        VBox vBoxItem8 = new VBox(new Label("Recebimento:"),dpDate3);
        grid.add(vBoxItem8,0,6,2,1);

        dpDate4 = new DatePicker();
        dpDate4.setPrefWidth(154.0);
        VBox vBoxItem9 = new VBox(new Label("Remetimento:"),dpDate4);
        grid.add(vBoxItem9,2,6,2,1);

        dpDate5 = new DatePicker();
        dpDate5.setPrefWidth(154.0);
        VBox vBoxItem10 = new VBox(new Label("Deferimento:"),dpDate5);
        grid.add(vBoxItem10,4,6,2,1);
    }

    public void setStatusBar(List<String> items) {
        statusBar.getChildren().clear();
        for (String item : items) {
            statusBar.getChildren().add(new Label(item));
        }
    }

    public void clearForm() {
        dpDate1.setValue(LocalDate.now());
        cbPerson.getSelectionModel().select(-1);
        cbProtocolType.getSelectionModel().select(-1);
        taSummary.setText("");
        cbStatus.getSelectionModel().select(-1);
        dpDate2.setValue(LocalDate.now());
        dpDate3.setValue(LocalDate.now());
        dpDate3.setDisable(true);
        dpDate4.setValue(LocalDate.now());
        dpDate4.setDisable(true);
        dpDate5.setValue(LocalDate.now());
        dpDate5.setDisable(true);
    }
}
