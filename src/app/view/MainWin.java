package app.view;

import app.controller.Utils;
import core.model.Protocol;
import core.model.ProtocolType;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.List;

public class MainWin {
    private MenuBar menuBar;
    public MenuItem menuItemLogoff;
    public MenuItem menuItemProtocol;
    public MenuItem menuItemPerson;
    public MenuItem menuItemProtocolType;
    public MenuItem menuItemAbout;
    public GridPane gridFilter;
    public TextField tfFilterId;
    public TextField tfFilterPerson;
    public ComboBox<ProtocolType> cbFilterType;
    public ComboBox<String> cbFilterPeriod;
    public DatePicker dpFilterDate1;
    public DatePicker dpFilterDate2;
    private VBox centerPane;
    public TableView<Protocol> tableViewProtocol;
    private HBox statusBar;

    public MainWin() {
        setMenuBar();
        setFilters();
        setTableViewProtocol();
        setCenterPane();
        setStatusBar();
    }

    private void setMenuBar() {
        Menu menuFile = new Menu("_Arquivo");
        menuItemLogoff = new MenuItem("_Bloquear");
        MenuItem menuItemExit = new MenuItem("_Sair");
        menuItemExit.setOnAction(event -> {
            Platform.exit();
            System.exit(0);
        });
        menuFile.getItems().add(menuItemLogoff);
        menuFile.getItems().add(menuItemExit);

        Menu menuEdit = new Menu("_Editar");
        menuItemProtocol = new MenuItem("_Protocolo");
        menuItemPerson = new MenuItem("_Requerente");
        menuItemProtocolType = new MenuItem("_Tipo de Protocolo");
        menuEdit.getItems().add(menuItemProtocol);
        menuEdit.getItems().add(menuItemPerson);
        menuEdit.getItems().add(menuItemProtocolType);

        Menu menuHelp = new Menu("A_juda");
        menuItemAbout = new MenuItem("So_bre");
        menuHelp.getItems().add(menuItemAbout);

        menuBar = new MenuBar();
        menuBar.getMenus().add(menuFile);
        menuBar.getMenus().add(menuEdit);
        menuBar.getMenus().add(menuHelp);
    }

    public MenuBar getMenuBar() {
        return menuBar;
    }

    private void setCenterPane() {
        centerPane = new VBox();
        centerPane.setSpacing(8);
        TitledPane titledPaneFilter = new TitledPane("Pesquisa:", gridFilter);
        titledPaneFilter.setCollapsible(false);
        centerPane.getChildren().add(titledPaneFilter);

        TitledPane titledPaneProtocol = new TitledPane("Requerimentos:", tableViewProtocol);
        titledPaneProtocol.setCollapsible(false);
        centerPane.getChildren().add(titledPaneProtocol);
    }

    public VBox getCenterPane() {
        return centerPane;
    }

    private void setTableViewProtocol() {
        tableViewProtocol = new TableView<>();

        TableColumn<Protocol, Integer> colId = new TableColumn<>("Id");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Protocol, LocalDate> colDate = new TableColumn<>("Data");
        colDate.setCellValueFactory(new PropertyValueFactory<>("recorded"));

        TableColumn<Protocol, String> colPerson = new TableColumn<>("Requerente");
        colPerson.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().person().getName()));

        TableColumn<Protocol, String> colType = new TableColumn<>("Tipo");
        colType.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().protocolType().getName()));

        TableColumn<Protocol, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getStatus()));

        tableViewProtocol.getColumns().add(colId);
        tableViewProtocol.getColumns().add(colDate);
        tableViewProtocol.getColumns().add(colPerson);
        tableViewProtocol.getColumns().add(colType);
        tableViewProtocol.getColumns().add(colStatus);
    }

    private void setStatusBar() {
        statusBar = new HBox();
        statusBar.setPadding(new Insets(4.0,8.0,4.0,16.0));
        statusBar.getChildren().add(new Label("rá"));
    }

    public HBox getStatusBar() {
        return statusBar;
    }

    public void updateStatusBar(List<String> items) {
        statusBar.getChildren().clear();
        for (String item : items) {
            statusBar.getChildren().add(new Label(item));
        }
    }

    private void setFilters() {
        gridFilter = new GridPane();
        gridFilter.setAlignment(Pos.CENTER_LEFT);

        tfFilterId = new TextField();
        tfFilterId.setPrefWidth(92.0);
        tfFilterId.setMaxWidth(92.0);
        Utils.addTextLimiter(tfFilterId,5);
        AnchorPane anchorFilterId = new AnchorPane();
        Label lbXId = new Label("x");
        lbXId.setId("labelclear");
        lbXId.setOnMouseClicked(event -> tfFilterId.setText(""));
        anchorFilterId.getChildren().addAll(tfFilterId,lbXId);
        AnchorPane.setTopAnchor(tfFilterId,0.0);
        AnchorPane.setRightAnchor(lbXId,9.0);
        AnchorPane.setTopAnchor(lbXId,4.0);
        VBox vBoxItem1 = new VBox(new Label("Número:"),anchorFilterId);
        gridFilter.add(vBoxItem1,0,0);

        tfFilterPerson = new TextField();
        tfFilterPerson.setPrefWidth(284.0);
        Utils.addTextLimiter(tfFilterPerson,50);
        AnchorPane anchorFilterPerson = new AnchorPane();
        Label lbXPerson = new Label("x");
        lbXPerson.setId("labelclear");
        lbXPerson.setOnMouseClicked(event -> tfFilterPerson.setText(""));
        anchorFilterPerson.getChildren().addAll(tfFilterPerson,lbXPerson);
        AnchorPane.setTopAnchor(tfFilterPerson,0.0);
        AnchorPane.setRightAnchor(lbXPerson,9.0);
        AnchorPane.setTopAnchor(lbXPerson,4.0);
        VBox vBoxItem2 = new VBox(new Label("Requerente:"),anchorFilterPerson);
        gridFilter.add(vBoxItem2,0,1,2,1);

        cbFilterType = new ComboBox<>();
        cbFilterType.setPrefWidth(284.0);
        VBox vBoxItem3 = new VBox(new Label("Tipo:"),cbFilterType);
        gridFilter.add(vBoxItem3,0,2,2,1);

        cbFilterPeriod = new ComboBox<>();
        cbFilterPeriod.getItems().addAll("Todos","Último Ano","Último Mês","Últimos 12 Meses",
                "Últimos 30 dias","Esta Semana","Hoje","Informado");
        VBox vBoxItem4 = new VBox(new Label("Intervalo:"),cbFilterPeriod);
        gridFilter.add(vBoxItem4,2,0,2,1);

        dpFilterDate1 = new DatePicker();
        dpFilterDate1.setDisable(true);
        VBox vBoxItem5 = new VBox(new Label("De:"),dpFilterDate1);
        gridFilter.add(vBoxItem5,2,1,1,1);
        dpFilterDate2 = new DatePicker();
        dpFilterDate2.setDisable(true);
        VBox vBoxItem6 = new VBox(new Label("Até:"),dpFilterDate2);
        gridFilter.add(vBoxItem6,3,1,1,1);
    }
}
