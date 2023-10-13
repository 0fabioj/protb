package app.controller;

import core.controller.PersonController;
import core.model.Person;
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

public class PersonView implements Initializable {
    @FXML
    TableView<Person> tableviewPerson;
    @FXML
    private TableColumn<Person, String> colName;
    @FXML
    private TextField textfieldId;
    @FXML
    private TextField textfieldName;
    @FXML
    private Button buttonDel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillTableViewPerson();
    }

    @FXML
    public void actionSave(ActionEvent event) {
        Person p = new Person();
        String id = textfieldId.getText();
        String name = textfieldName.getText();
        if (name == null || name.isEmpty()) {
            CustomAlert.showAlert(Alert.AlertType.INFORMATION, null,
                    "Campo deve ser preenchido.\nNome");
        }
        else {
            if (id == null || id.isEmpty()) {
                p.setId(0);
            }
            else {
                p.setId(Integer.parseInt(id));
            }
            p.setName(name);
            if (PersonController.save(p)) {
                fillTableViewPerson();
            } else {
                CustomAlert.showAlert(Alert.AlertType.ERROR,null,"Erro ao salvar.");
            }
        }
    }

    private void fillTableViewPerson() {
        tableviewPerson.getItems().clear();
        textfieldId.clear();
        textfieldName.clear();
        buttonDel.setDisable(true);
        try {
            ObservableList<Person> data = PersonController.getList();
            colName.setCellValueFactory(new PropertyValueFactory<Person, String>("name"));

            tableviewPerson.setItems(data);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void actionEdit(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Person p = tableviewPerson.getSelectionModel().getSelectedItem();
            try {
                textfieldId.setText(String.valueOf(p.getId()));
                textfieldName.setText(p.getName());
                buttonDel.setDisable(false);
            }
            catch (IndexOutOfBoundsException e) {
                System.err.println("IndexOutOfBoundsException: " + e.getMessage());
            }
            finally {
                p = null;
            }
        }
    }

    @FXML
    public void actionDel(ActionEvent event) {
        int index = Integer.parseInt(textfieldId.getText());
        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
        alert1.setTitle("[protB] Exclusão");
        alert1.setHeaderText("Deseja excluir pessoa:");
        alert1.setContentText(textfieldName.getText());
        alert1.showAndWait();
        String msgAlert2;
        if (alert1.getResult() == ButtonType.OK) {
            if (PersonController.delete(index)) {
                msgAlert2 = "Registro excluido com sucesso";
            }
            else {
                msgAlert2 = "Erro ao excluir registro";
            }
            CustomAlert.showAlert(Alert.AlertType.INFORMATION,null, msgAlert2);
        }
        fillTableViewPerson();
    }

    @FXML
    public void actionClear(ActionEvent event) {
        textfieldId.clear();
        textfieldName.clear();
        buttonDel.setDisable(true);
    }

    @FXML public void actionCancel(ActionEvent event) {
        closePersonWindow(event);
    }

    public void closePersonWindow(ActionEvent event) {
        Window window = ((Node)(event.getSource())).getScene().getWindow();
        if (window instanceof Stage){
            ((Stage) window).close();
        }
    }
}
