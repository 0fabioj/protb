package app.controller;

import core.controller.PersonController;
import core.model.Person;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class PersonView implements Initializable {
    @FXML
    TableView<Person> tableviewPerson;
    @FXML
    private TableColumn<Person, String> colName;
    @FXML
    private TableColumn<Person, Integer> colId;
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
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("[protB] Aviso");
            alert.setHeaderText("Campo deve ser preenchido.");
            alert.setContentText("Nome");

            alert.showAndWait();
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
            colId.setCellValueFactory(new PropertyValueFactory<Person, Integer>("id"));
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
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setTitle("[protB] Exclusão");
            alert2.setHeaderText(msgAlert2);
            alert2.showAndWait();
        }
        fillTableViewPerson();
    }
}
