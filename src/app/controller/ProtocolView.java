package app.controller;

import core.controller.PersonController;
import core.controller.ProtocolController;
import core.controller.ProtocolTypeController;
import core.model.Person;
import core.model.Protocol;
import core.model.ProtocolType;
import database.PostgreSQL;
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
            actionLoad(p);
        } else {
            actionNew();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillComboStatus();
        fillComboType();
        fillComboPerson();
        buttonDel.setDisable(true);
    }

    public void actionNew() {
        Connection conn = PostgreSQL.connect();
        try {
            textfieldId.setText(String.valueOf(PostgreSQL.NextFreeId("protocol")));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            PostgreSQL.closeDatabase(conn);
        }
    }

    public void actionLoad(Protocol p) {
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
        LocalDate recorded = dateRecorded.getValue();//data protocolo
        LocalDate requested = dateRequested.getValue();//data requerimento
        LocalDate receipted = dateReceived.getValue();//data recebimento
        LocalDate forwarded = dateForwarded.getValue();//data remetimento
        LocalDate checked = dateChecked.getValue();//data deferimento

        if (personId <= 0) {
            CustomAlert.showInformation("Campo deve ser selecionado:\nRequerente");
        } else if (typeId <= 0) {
            CustomAlert.showInformation("Campo deve ser selecionado:\nTipo");
        } else if (recorded == null) {
            CustomAlert.showInformation("Campo deve ser preenchido:\nData do Protocolo");
        } else if (requested== null) {
            CustomAlert.showInformation("Campo deve ser preenchido:\nData do Requerimento");
        } else if (receipted== null) {
            CustomAlert.showInformation("Campo deve ser preenchido:\nData do Recebimento");
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
                actionCloseProtocolWindow(event);
            } else {
                CustomAlert.showError("Erro ao salvar.");
            }
        }
    }

    @FXML public void actionDel(ActionEvent event) {
        int index = Integer.parseInt(textfieldId.getText());
        boolean res = CustomAlert.showConfirmation("Confirma exclusão do protocolo:\n"+textfieldId.getText());
        if (res) {
            if (ProtocolController.delete(index)) {
                CustomAlert.showInformation("Registro excluido com sucesso.");
                actionCloseProtocolWindow(event);
            }
            else {
                CustomAlert.showError("Erro ao excluir registro.");
            }
        }
    }

    @FXML public void actionCancel(ActionEvent event) {
        actionCloseProtocolWindow(event);
    }

    public void actionCloseProtocolWindow(ActionEvent event) {
        Window window = ((Node)(event.getSource())).getScene().getWindow();
        if (window instanceof Stage){
            ((Stage) window).close();
        }
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
            comboType.setConverter(new StringConverter<>() {
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
            comboPerson.setConverter(new StringConverter<>() {
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
}
