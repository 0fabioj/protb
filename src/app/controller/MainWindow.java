package app.controller;

import core.controller.ProtocolController;
import core.controller.ProtocolTypeController;
import core.model.Protocol;
import core.model.ProtocolType;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

public class MainWindow implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    private ObservableList<Protocol> listProtocol;


    @FXML private TableView<Protocol> tableView1;
    @FXML private TableColumn<Protocol, String> colSummary;
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

    @FXML public void refreshTableViewProtocol(ActionEvent event) {
        if (tableView1.getItems().size() > 0) {
            tableView1.getItems().clear();
        }
        listProtocol = ProtocolController.getList();
    }
    public void fillProtocolTableView() {
        try {
            System.out.println("fillProtocolTableView");
            System.out.println("1");
            listProtocol = ProtocolController.getList();
            colId.setCellValueFactory(new PropertyValueFactory<Protocol, Integer>("id"));
            colPerson.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getPerson().getName()));
            colType.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getProtocolType().getDescription()));
            colRecorded.setCellValueFactory(new PropertyValueFactory<Protocol, LocalDateTime>("recorded"));
            /*colStatus.setCellValueFactory(new PropertyValueFactory<Protocol, Integer>("status"));*/

            FilteredList<Protocol> filteredData = new FilteredList<>(listProtocol, b -> true);
            filterPerson.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(protocol -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    if (protocol.getPerson().getName().toLowerCase().indexOf(newValue.toLowerCase()) != -1 ) {
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
                        if (protocol.getProtocolType().getDescription().indexOf(newValue.getDescription()) != -1 ) {
                            return true;
                        }
                        else
                            return false;
                    }
                    else
                        return true;
                });
            });

            System.out.println("2");

            SortedList<Protocol> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tableView1.comparatorProperty());
            System.out.println("3");
            tableView1.setItems(sortedData);
            System.out.println("4");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void fillComboType() {
        try {
            filterType.getItems().clear();
            List<ProtocolType> listType = ProtocolTypeController.getList();
            for (var i = 0; i < listType.size(); i++) {
                filterType.getItems().add(i, listType.get(i));
            }
            filterType.setVisibleRowCount(listType.size());
            filterType.setConverter(new StringConverter<ProtocolType>() {
                @Override
                public String toString(ProtocolType pt) {
                    return pt == null ? "" : pt.getDescription();
                }

                @Override
                public ProtocolType fromString(String s) {
                    ProtocolType pt = new ProtocolType();
                    return pt;
                }
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void fillComboStatus() {
        try {
            filterStatus.getItems().setAll("Todos","Recebido","Remetido","Deferido","Indeferido");
            filterStatus.setValue("Todos");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //fillComboType();
        fillComboStatus();
        fillProtocolTableView();
    }

    @FXML public void newProtocolWindow() throws IOException {
        if (stage == null || !stage.isShowing()) {
            stage = new Stage();
            Parent loaderDetail = FXMLLoader.load(getClass().getResource("/app/view/ProtocolView.fxml"));
            stage.setScene(new Scene(loaderDetail));
            stage.setTitle("[protB] Novo Protocolo");
            stage.show();
        } else {
            stage.toFront();
        }
    }

    @FXML public void actionProtocolEdit(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {
            Protocol p = tableView1.getSelectionModel().getSelectedItem();
            try {
                if (stage == null || !stage.isShowing()) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/view/ProtocolView.fxml"));
                    root = loader.load();

                    ProtocolView pv = loader.getController();
                    pv.protocolLoad(p);

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
            finally {
                p = null;
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
