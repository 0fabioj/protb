package app.controller;

import core.controller.ProtocolController;
import core.controller.ProtocolTypeController;
import core.model.Protocol;
import core.model.ProtocolType;
import database.PostgreSQL;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class MainWindow implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML private TableView<Protocol> tableView1;
    @FXML
    private TableColumn<Protocol, Integer> colId;
    @FXML
    private TableColumn<Protocol, String> colPerson;
    @FXML
    private TableColumn<Protocol, String> colType;
    @FXML
    private TableColumn<Protocol, LocalDateTime> colRecorded;
    @FXML
    private TableColumn<Protocol, String> colStatus;
    @FXML
    private TextField filterNumber;
    @FXML
    private TextField filterPerson;
    @FXML
    private ComboBox<ProtocolType> filterType;
    @FXML
    private ComboBox<String> filterStatus;
    @FXML
    private DatePicker filterDate1;
    @FXML
    private DatePicker filterDate2;
    @FXML Label labelStatus1;

    public void updateTableView() {
        try {
            ObservableList<Protocol> listProtocol = ProtocolController.getList();
            colId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colPerson.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getPerson().getName()));
            colType.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getProtocolType().getDescription()));
            colRecorded.setCellValueFactory(new PropertyValueFactory<>("recorded"));
            colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

            FilteredList<Protocol> filteredData = new FilteredList<>(listProtocol, b -> true);
            filterPerson.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(protocol -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    if (protocol.getPerson().getName().toLowerCase().contains(newValue.toLowerCase())) {
                        //if (protocol.getPerson().getName().toLowerCase().indexOf(newValue.toLowerCase()) != -1 ) {
                        return true;
                    }
                    else
                        return false;
                });
            });
            filterType.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
                filteredData.setPredicate(protocol -> {
                    if (filterType.getSelectionModel().getSelectedIndex() > 0) {
                        //if (protocol.getProtocolType().getDescription().indexOf(newValue) != -1 ) {
                        //if (protocol.getProtocolType().getDescription().indexOf(newValue.getDescription()) != -1 ) {
                        if (protocol.getProtocolType().getDescription().contains(newValue.getDescription())) {
                            return true;
                        }
                        else
                            return false;
                    }
                    else
                        return true;
                });
            });

            SortedList<Protocol> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tableView1.comparatorProperty());
            tableView1.setItems(sortedData);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Update()");
    }

    public void fillComboType() {
        try {
            filterType.getItems().clear();
            filterType.getItems().add(0, new ProtocolType(0, "Todos"));
            for( ProtocolType protocolType : ProtocolTypeController.getList()) {
                filterType.getItems().add(protocolType);
            }
            filterType.getSelectionModel().selectFirst();
            filterType.setConverter(new StringConverter<ProtocolType>() {
                @Override
                public String toString(ProtocolType pt) {
                    return pt == null ? "" : pt.getDescription();
                }

                @Override
                public ProtocolType fromString(String s) {
                    return new ProtocolType();
                }
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void fillComboStatus() {
        try {
            filterStatus.getItems().setAll("Todos","Recebido","Remetido","Deferido","Indeferido","Cancelado");
            filterStatus.setValue("Todos");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillComboType();
        fillComboStatus();
        updateTableView();
        labelStatus1.setText("Ready");
    }

    @FXML public void newProtocolWindow() throws IOException {
        if (stage == null || !stage.isShowing()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/view/ProtocolView.fxml"));
            root = loader.load();
            ProtocolView pv = loader.getController();
            pv.setProtocol(new Protocol(0));
            stage = new Stage();
            stage.setTitle("[protB] Novo Protocolo");
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            stage.toFront();
        }
    }

    @FXML public void actionProtocolEdit(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {
            if(tableView1.getSelectionModel().getSelectedItem() != null) {
                Protocol p = tableView1.getSelectionModel().getSelectedItem();
                try {
                    if (stage == null || !stage.isShowing()) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/view/ProtocolView.fxml"));
                        root = loader.load();

                        ProtocolView pv = loader.getController();
                        pv.setProtocol(p);

                        //stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                        stage = new Stage();
                        stage.setTitle("[protB] Protocolo ["+p.getId()+"]");
                        scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    } else {
                        stage.toFront();
                    }
                }
                catch (IndexOutOfBoundsException e) {
                    System.err.println("IndexOutOfBoundsException: " + e.getMessage());
                }
            }
        }
    }

    @FXML public void openPerson() throws IOException {
        if (stage == null || !stage.isShowing()) {
            stage = new Stage();
            Parent loaderP = FXMLLoader.load(getClass().getResource("/app/view/PersonView.fxml"));
            stage.setScene(new Scene(loaderP));
            stage.setTitle("[protB] Pessoas");
            stage.show();
        } else {
            stage.toFront();
        }
    }

    @FXML public void openType() throws IOException {
        if (stage == null || !stage.isShowing()) {
            stage = new Stage();
            Parent loaderP = FXMLLoader.load(getClass().getResource("/app/view/ProtocolTypeView.fxml"));
            stage.setScene(new Scene(loaderP));
            stage.setTitle("[protB] Tipos de Protocolo");
            stage.show();
        } else {
            stage.toFront();
        }
    }
}
