package app.view;

import app.controller.Utils;
import core.model.ProtocolType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ProtocolTypeWin {
    Stage dialog;
    ToolBar toolBar;
    public Button btnSave;
    public Button btnDel;
    public GridPane grid;
    public TextField tfId;
    public TextField tfName;
    public TableView<ProtocolType> tableViewProtocolType;
    public MenuButton menuBtnFilter;
    public TextField tfFilter;
    public HBox filter;
    private final HBox statusBar;

    public ProtocolTypeWin() {
        dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);

        setFilters();
        setTableViewPerson();
        setToolBar();
        setForm();

        TitledPane table = new TitledPane("Lista:", tableViewProtocolType);
        table.setCollapsible(false);
        VBox left = new VBox();
        left.setAlignment(Pos.TOP_LEFT);
        left.getChildren().add(filter);
        left.getChildren().add(table);
        statusBar = new HBox();
        statusBar.setPadding(new Insets(4.0,8.0,4.0,16.0));
        left.getChildren().add(statusBar);

        TitledPane detail = new TitledPane("Tipo:", grid);
        detail.setCollapsible(false);
        VBox right = new VBox();
        right.setAlignment(Pos.TOP_LEFT);
        right.getChildren().add(toolBar);
        right.getChildren().add(detail);

        SplitPane split = new SplitPane();
        split.getItems().addAll(left,right);

        List<String> statusMgs = new ArrayList<>();
        statusMgs.add("Ready");
        setStatusBar(statusMgs);

        Scene scene = new Scene(split, 600, 380);
        scene.getStylesheets().add(Utils.getCSS());
        dialog.setTitle("Tipo de Protocolo");
        dialog.setResizable(false);
        dialog.setScene(scene);
    }

    public void show() {
        dialog.show();
    }

    private void setTableViewPerson() {
        tableViewProtocolType = new TableView<>();

        TableColumn<ProtocolType, Integer> colId = new TableColumn<>("Id");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setVisible(false);

        TableColumn<ProtocolType, String> colDate = new TableColumn<>("Descrição");
        colDate.setCellValueFactory(new PropertyValueFactory<>("name"));

        tableViewProtocolType.getColumns().add(colId);
        tableViewProtocolType.getColumns().add(colDate);
        tableViewProtocolType.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

    private void setToolBar() {
        btnSave = new Button("Save");
        btnSave.setOnAction(e -> dialog.close());
        btnDel = new Button("Del");
        btnDel.setOnAction(e -> dialog.close());
        Button btnClear = new Button("Clear");
        btnClear.setOnAction(e -> clearForm());
        Button btnClose = new Button("Close");
        btnClose.setOnAction(e -> dialog.close());
        toolBar = new ToolBar();
        toolBar.getItems().add(btnSave);
        toolBar.getItems().add(btnDel);
        toolBar.getItems().add(btnClear);
        toolBar.getItems().add(btnClose);
    }

    private void setFilters() {
        MenuItem menuItemFilterName = new MenuItem("Descrição");
        menuBtnFilter = new MenuButton("Filtrar:", null, menuItemFilterName);
        menuBtnFilter.setId("menubutton");
        menuBtnFilter.setMaxWidth(92.0);
        menuItemFilterName.setOnAction(event -> menuBtnFilter.setText("Descrição"));
        tfFilter = new TextField();
        tfFilter.setPrefWidth(182.0);
        AnchorPane anchorFilter = new AnchorPane();
        Label lbX = new Label("x");
        lbX.setId("labelclear");
        lbX.setOnMouseClicked(event -> tfFilter.setText(""));
        anchorFilter.getChildren().addAll(tfFilter,lbX);
        AnchorPane.setLeftAnchor(tfFilter,4.0);
        AnchorPane.setTopAnchor(tfFilter,3.0);
        AnchorPane.setRightAnchor(lbX,9.0);
        AnchorPane.setTopAnchor(lbX,7.0);
        filter = new HBox(menuBtnFilter, anchorFilter);
        filter.setPadding(new Insets(6));
        filter.setAlignment(Pos.CENTER_LEFT);
    }

    private void setForm() {
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        tfId = new TextField();
        tfId.setPrefWidth(72.0);
        tfId.setMaxWidth(72.0);
        tfId.setEditable(false);
        VBox vBoxItem1 = new VBox(new Label("Id:"),tfId);
        grid.add(vBoxItem1,0,0);

        tfName = new TextField();
        tfName.setPrefWidth(254.0);
        VBox vBoxItem2 = new VBox(new Label("Descrição:"),tfName);
        grid.add(vBoxItem2,0,1);
    }

    public void setStatusBar(List<String> items) {
        statusBar.getChildren().clear();
        for (String item : items) {
            statusBar.getChildren().add(new Label(item));
        }
    }

    public void clearForm() {
        tfId.setText("");
        tfName.setText("");
    }
}

