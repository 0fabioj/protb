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
    public void actionSave() {
        Person p = new Person();
        String id = textfieldId.getText();
        String name = textfieldName.getText();
        if (name == null || name.isEmpty()) {
            CustomAlert.showInformation("Campo deve ser preenchido.\nNome");
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
                CustomAlert.showError("Erro ao salvar.");
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
            colName.setCellValueFactory(new PropertyValueFactory<>("name"));

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
        }
    }

    @FXML
    public void actionDel() {
        int index = Integer.parseInt(textfieldId.getText());
        boolean res = CustomAlert.showConfirmation("Deseja excluir pessoa:");
        if (res) {
            if (PersonController.delete(index)) {
                CustomAlert.showInformation("Registro excluido com sucesso");
            }
            else {
                CustomAlert.showError("Erro ao excluir registro");
            }
        }
        fillTableViewPerson();
    }

    @FXML
    public void actionClear() {
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
