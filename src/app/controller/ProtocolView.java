package app.controller;

import core.controller.PersonController;
import core.controller.ProtocolController;
import core.controller.ProtocolTypeController;
import core.model.Person;
import core.model.Protocol;
import core.model.ProtocolType;
import database.PostgreSQLConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
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
    @FXML private Button buttonClear;

    public void setProtocol(Protocol p) {
        if(p.getId()>0) {
            protocolLoad(p);
        } else {
            protocolNew();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillComboStatus();
        fillComboType();
        fillComboPerson();
        buttonDel.setDisable(true);
    }

    public void protocolNew() {
        Connection conn = PostgreSQLConnection.connect();
        try {
            textfieldId.setText(String.valueOf(PostgreSQLConnection.NextFreeId(conn, "protocol")));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            PostgreSQLConnection.closeDatabase(conn);
        }
    }

    public void protocolLoad(Protocol p) {
        textfieldId.setText(String.valueOf(p.getId()));
        dateRecorded.setValue(p.getRecorded());
        comboPerson.getSelectionModel().select(p.getPerson());
        comboType.getSelectionModel().select(p.getProtocolType());
        textSummary.setText(p.getSummary());
        dateRequested.setValue(p.getRequested());
        comboStatus.setValue(p.getStatus());
        dateReceived.setValue(p.getReceipted());
        dateForwarded.setValue(p.getForwarded());
        dateChecked.setValue(p.getChecked());

        buttonDel.setDisable(false);
        buttonClear.setDisable(true);
    }

    @FXML
    public void actionSave(ActionEvent event) {
        String protocolId = textfieldId.getText();
        int personId = comboPerson.getSelectionModel().getSelectedItem().getId();
        int typeId = comboType.getSelectionModel().getSelectedItem().getId();
        String summary = textSummary.getText();
        int status = comboStatus.getSelectionModel().getSelectedIndex() + 1;
        LocalDate recorded = dateRecorded.getValue();
        LocalDate requested = dateRequested.getValue();
        LocalDate receipted = dateReceived.getValue();
        LocalDate forwarded = dateForwarded.getValue();
        LocalDate checked = dateChecked.getValue();

        if (personId <= 0) {
            CustomAlert.showAlert(Alert.AlertType.INFORMATION,
                    null,"Campo deve ser selecionado: \nRequerente");
        } else if (typeId <= 0) {
            CustomAlert.showAlert(Alert.AlertType.INFORMATION,
                    null,"Campo deve ser selecionado: \nTipo");
        } else if (recorded == null) {
            CustomAlert.showAlert(Alert.AlertType.INFORMATION,
                    null,"Data do Protocolo deve ser preenchida.");
        } else if (receipted== null) {
                CustomAlert.showAlert(Alert.AlertType.INFORMATION,
                        null,"Data do Requerimento deve ser preenchida.");
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
                CustomAlert.showAlert(Alert.AlertType.ERROR,null,"Erro ao salvar.");
            }
        }
    }

    @FXML public void actionDel(ActionEvent event) {
        int index = Integer.parseInt(textfieldId.getText());

        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
        alert1.setTitle("[protB] Exclusão");
        alert1.setHeaderText(null);
        alert1.setContentText("Confirma exclusão do protocolo:\n"+textfieldId.getText());
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
            CustomAlert.showAlert(Alert.AlertType.INFORMATION,
                    null,msgAlert2);
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
        controller.updateTableView();
 */
    }
    @FXML public void actionClear() {
        textfieldId.clear();
        comboPerson.getSelectionModel().selectFirst();
        comboType.getSelectionModel().selectFirst();
        textSummary.clear();
        comboStatus.getSelectionModel().selectFirst();
        dateRecorded.setValue(LocalDate.now());
        dateRequested.setValue(null);
        dateReceived.setValue(LocalDate.now());
        dateForwarded.setValue(null);
        dateChecked.setValue(null);
        buttonDel.setDisable(true);
    }


    public void fillComboStatus() {
        try {
            comboStatus.getItems().setAll("Recebido","Remetido","Deferido","Indeferido","Cancelado");
            comboStatus.getSelectionModel().selectFirst();
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

    @FXML public void actionChangeComboPerson(){
        System.out.print("Combo Person Selected:");
        System.out.print(comboPerson.getSelectionModel().getSelectedItem().getId());
        System.out.println(comboPerson.getSelectionModel().getSelectedItem().getName());
    }

}
