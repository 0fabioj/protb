package app.controller;

import core.controller.PersonController;
import core.controller.ProtocolTypeController;
import core.model.ProtocolType;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

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
    public void actionSave(ActionEvent event) {
        ProtocolType pt = new ProtocolType();
        String id = textfieldId.getText();
        String desc = textfieldDesc.getText();
        if (desc == null || desc.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("[protB] Aviso");
            alert.setHeaderText("Campo deve ser preenchido.");
            alert.setContentText("Descrição");

            alert.showAndWait();
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
            colDesc.setCellValueFactory(new PropertyValueFactory<ProtocolType, String>("description"));

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
            finally {
                pt = null;
            }
        }
    }

    @FXML
    public void actionDel(ActionEvent event) {
        int index = Integer.parseInt(textfieldId.getText());
        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
        alert1.setTitle("[protB] Exclusão");
        alert1.setHeaderText("Deseja excluir tipo de protocolo:");
        alert1.setContentText(textfieldDesc.getText());
        alert1.showAndWait();
        String msgAlert2;
        if (alert1.getResult() == ButtonType.OK) {
            if (ProtocolTypeController.delete(index)) {
                msgAlert2 = "Registro excluido com sucesso";
            }
            else {
                msgAlert2 = "Erro ao excluir registro";
            }
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setTitle("[protB] Exclusão");
            alert2.setHeaderText(msgAlert2);
            alert2.showAndWait();
        }
        fillTableViewType();
    }

    @FXML
    public void actionClear(ActionEvent event) {
        textfieldId.clear();
        textfieldDesc.clear();
        buttonDel.setDisable(true);
    }
}
