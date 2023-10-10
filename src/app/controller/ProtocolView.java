package app.controller;

import core.controller.PersonController;
import core.controller.ProtocolController;
import core.controller.ProtocolTypeController;
import core.model.Person;
import core.model.Protocol;
import core.model.ProtocolType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ProtocolView implements Initializable {
    @FXML private TextField textfieldId;
    @FXML private ComboBox<Person> comboPerson;
    @FXML private ComboBox<ProtocolType> comboType;
    @FXML private TextArea textSummary;
    @FXML private ComboBox<String> comboStatus;
    @FXML private DatePicker dateRecorded;
    @FXML private DatePicker dateRequested;
    @FXML private DatePicker dateReceived;
    @FXML private DatePicker dateForwarded;
    @FXML private DatePicker dateChecked;
    @FXML private Button buttonDel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillComboStatus();
        fillComboType();
        fillComboPerson();
        buttonDel.setDisable(true);
    }

    void protocolLoad(Protocol p) {
        textfieldId.setText(String.valueOf(p.getId()));
        dateRecorded.setValue(p.getRecorded());
        comboPerson.setValue(p.getPerson());
        comboType.setValue(p.getProtocolType());
        textSummary.setText(p.getSummary());
        dateRequested.setValue(p.getRequested());
        comboStatus.setValue(String.valueOf(p.getStatus()));
        dateReceived.setValue(p.getReceipted());
        dateForwarded.setValue(p.getForwarded());
        dateChecked.setValue(p.getChecked());

        buttonDel.setDisable(false);
    }

    @FXML
    public void actionSave(ActionEvent event) {
        String protocolId = textfieldId.getText();
        int personId = comboPerson.getSelectionModel().getSelectedItem().getId();
        int typeId = comboType.getSelectionModel().getSelectedItem().getId();
        String summary = textSummary.getText();
        int status = 1;
        LocalDate recorded = dateRecorded.getValue();
        LocalDate requested = dateRequested.getValue();
        LocalDate receipted = dateReceived.getValue();
        LocalDate forwarded = dateForwarded.getValue();
        LocalDate checked = dateChecked.getValue();

        if (personId <= 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("[protB] Aviso");
            alert.setHeaderText("Campo deve ser selecionado.");
            alert.setContentText("Requerente");

            alert.showAndWait();
        } else if (typeId <= 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("[protB] Aviso");
            alert.setHeaderText("Campo deve ser selecionado.");
            alert.setContentText("Tipo");

            alert.showAndWait();
        } else {
            Protocol p = new Protocol();
            if (protocolId == null || protocolId.isEmpty()) {
                p.setId(0);
            }
            else {
                p.setId(Integer.parseInt(protocolId));
            }
            p.setPerson(new Person(personId));
            p.setProtocolType(new ProtocolType(typeId));
            p.setSummary(summary);
            p.setStatus(status);
            p.setRecorded(recorded);
            p.setRequested(requested);
            p.setReceipted(receipted);
            p.setForwarded(forwarded);
            p.setChecked(checked);
            if (ProtocolController.save(p)) {
                closeProtocolWindow(event);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("[protB] Erro");
                alert.setHeaderText("Erro.");
                alert.setContentText("err");

                alert.showAndWait();
            }
        }
    }

    @FXML public void actionDel(ActionEvent event) {
        int index = Integer.parseInt(textfieldId.getText());
        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
        alert1.setTitle("[protB] Exclusão");
        alert1.setHeaderText("Deseja excluir protocolo:");
        alert1.setContentText(textfieldId.getText());
        alert1.showAndWait();
        String msgAlert2;
        if (alert1.getResult() == ButtonType.OK) {
            if (ProtocolController.delete(index)) {
                msgAlert2 = "Registro excluido com sucesso";
                closeProtocolWindow(event);
            }
            else {
                msgAlert2 = "Erro ao excluir registro";
            }
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setTitle("[protB] Exclusão");
            alert2.setHeaderText(msgAlert2);
            alert2.showAndWait();
        }
    }

    @FXML public void actionCancel(ActionEvent event) {
        closeProtocolWindow(event);
    }

    public void closeProtocolWindow(ActionEvent event) {
        Window window = ((Node)(event.getSource())).getScene().getWindow();
        if (window instanceof Stage){
            ((Stage) window).close();
        }
/*
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/view/MainWindow.fxml"));
        MainWindow controller = (MainWindow) loader.getController();
        controller.fillProtocolTableView();
 */
    }

    public void fillComboStatus() {
        try {
            comboStatus.getItems().setAll("Recebido","Remetido","Deferido","Indeferido");
            comboStatus.setValue("Recebido");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void fillComboType() {
        try {
            comboType.getItems().clear();
            comboType.getItems().setAll(ProtocolTypeController.getList());
            comboType.getSelectionModel().selectFirst();
            comboType.setConverter(new StringConverter<ProtocolType>() {
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

    public void fillComboPerson() {
        try {
            comboPerson.getItems().clear();
            comboPerson.getItems().setAll(PersonController.getList());
            comboPerson.getSelectionModel().selectFirst();
            comboPerson.setConverter(new StringConverter<Person>() {
                @Override
                public String toString(Person p) {
                    return p == null ? "" : p.getName();
                }

                @Override
                public Person fromString(String s) {
                    return new Person();
                }
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML public void actionChangeComboPerson(ActionEvent event){
        System.out.print("Combo Person Selected:");
        System.out.print(comboPerson.getSelectionModel().getSelectedItem().getId());
        System.out.println(comboPerson.getSelectionModel().getSelectedItem().getName());
    }

}
