package app.controller;

import core.controller.ProtocolTypeController;
import core.model.ProtocolType;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URL;
import java.util.ResourceBundle;

public class ProtocolTypeView implements Initializable {
    @FXML
    TableView<ProtocolType> tableviewType;
    @FXML
    private TableColumn<ProtocolType, String> colDesc;
    @FXML
    private TextField textfieldId;
    @FXML
    private TextField textfieldDesc;
    @FXML
    private Button buttonDel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillTableViewType();
    }

    @FXML
    public void actionSave() {
        ProtocolType pt = new ProtocolType();
        String id = textfieldId.getText();
        String desc = textfieldDesc.getText();
        if (desc == null || desc.isEmpty()) {
            CustomAlert.showInformation("Campo deve ser preenchido:\nDescrição");
        }
        else {
            if (id == null || id.isEmpty()) {
                pt.setId(0);
            }
            else {
                pt.setId(Integer.parseInt(id));
            }
            pt.setDescription(desc);
            if (ProtocolTypeController.save(pt)) {
                fillTableViewType();
            }
        }
    }

    private void fillTableViewType() {
        tableviewType.getItems().clear();
        textfieldId.clear();
        textfieldDesc.clear();
        buttonDel.setDisable(true);
        try {
            ObservableList<ProtocolType> data = ProtocolTypeController.getList();
            colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
            tableviewType.setItems(data);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void actionEdit(MouseEvent event) {
        if (event.getClickCount() == 2) {
            ProtocolType pt = tableviewType.getSelectionModel().getSelectedItem();
            try {
                textfieldId.setText(String.valueOf(pt.getId()));
                textfieldDesc.setText(pt.getDescription());
                buttonDel.setDisable(false);
            }
            catch (IndexOutOfBoundsException e) {
                System.err.println("IndexOutOfBoundsException: " + e.getMessage());
            }
        }
    }

    @FXML
    public void actionDel() {
        int index = Integer.parseInt(textfieldId.getText());
        boolean res = CustomAlert.showConfirmation("Deseja excluir tipo de protocolo:");
        if (res) {
            if (ProtocolTypeController.delete(index)) {
                CustomAlert.showInformation("Registro excluido com sucesso");
                fillTableViewType();
            }
            else {
                CustomAlert.showError("Erro ao excluir registro");
            }
        }
    }

    @FXML
    public void actionClear() {
        textfieldId.clear();
        textfieldDesc.clear();
        buttonDel.setDisable(true);
    }

    @FXML public void actionCancel(ActionEvent event) {
        closeTypeWindow(event);
    }

    public void closeTypeWindow(ActionEvent event) {
        Window window = ((Node)(event.getSource())).getScene().getWindow();
        if (window instanceof Stage){
            ((Stage) window).close();
        }
    }
}
