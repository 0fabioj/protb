package app.controller;

import app.view.ProtocolWin;
import core.datamodel.PersonDataModel;
import core.datamodel.PostgreSQL;
import core.datamodel.ProtocolDataModel;
import core.datamodel.ProtocolTypeDataModel;
import core.model.Person;
import core.model.Protocol;
import core.model.ProtocolType;
import javafx.application.Platform;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProtocolWinHandle {
    ProtocolWin ui;
    int protocolID;
    Protocol protocol;
    public ActionResult winResult;

    public ProtocolWinHandle() {
        ui = new ProtocolWin();
        setUiActions();
        ui.clearForm();
        winResult = ActionResult.CANCEL;
    }

    public void showWindow() {
        ui.show();
    }

    private void setUiActions() {
        Utils.addTextLimiter(ui.taSummary,250);

        ui.btnSave.setOnAction(event -> {
            int id = Utils.ValidateID(ui.tfId.getText().trim());
            LocalDate recorded = ui.dpDate1.getValue();
            Person person = ui.cbPerson.getSelectionModel().getSelectedItem();
            ProtocolType protocolType = ui.cbProtocolType.getSelectionModel().getSelectedItem();
            String summary = ui.taSummary.getText();
            String path = "";
            LocalDate requested = ui.dpDate1.getValue();
            int status = ui.cbStatus.getSelectionModel().getSelectedIndex();
            LocalDate receipted = ui.dpDate1.getValue();
            LocalDate forwarded = ui.dpDate1.getValue();
            LocalDate checked = ui.dpDate1.getValue();

            if (id > 0) {
                protocol = new Protocol(
                        id,
                        recorded,
                        person,
                        protocolType,
                        summary,
                        path,
                        requested,
                        status,
                        receipted,
                        forwarded,
                        checked
                );
                winResult = protocolID == 0 ? ActionResult.INSERT_SUCCESS : ActionResult.UPDATE_SUCCESS;
                ui.close();
            } else {
                winResult = protocolID == 0 ? ActionResult.INSERT_ERROR : ActionResult.UPDATE_ERROR;
            }
        });
        ui.btnDel.setOnAction(event -> {
            int id = Utils.ValidateID(ui.tfId.getText().trim());
            if (id > 0) {
                winResult = ActionResult.DELETE_SUCCESS;
                ui.close();
            } else {
                winResult = ActionResult.DELETE_ERROR;
                System.out.println("É zero ou Nao existe na lista");
            }
        });
        ui.cbStatus.getSelectionModel().selectedIndexProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue == null) {
                newValue = 0;
            }
            switch (newValue.intValue()) {
                case 0 -> { ui.dpDate3.setDisable(false); ui.dpDate4.setDisable(true); ui.dpDate5.setDisable(true); }
                case 1 -> { ui.dpDate3.setDisable(false); ui.dpDate4.setDisable(false); ui.dpDate5.setDisable(true); }
                case 2,3,4,5 -> { ui.dpDate3.setDisable(false); ui.dpDate4.setDisable(false); ui.dpDate5.setDisable(false); }
                default -> { ui.dpDate3.setDisable(true); ui.dpDate4.setDisable(true); ui.dpDate5.setDisable(true); }
            }
        });
        Platform.runLater(() -> {
            List<String> statusMgs = new ArrayList<>();
            statusMgs.add("Loading...");
            ui.setStatusBar(statusMgs);
            ui.cbPerson.setItems(new PersonDataModel().getPersonFXBeans());
            ui.cbProtocolType.setItems(new ProtocolTypeDataModel().getProtocolTypeFXBeans());
            ui.cbStatus.getItems().setAll("Recebido","Remetido","Deferido","Indeferido","Cancelado");

            if (ui.tfId.getText().isBlank()) {
                ui.tfId.setText(String.valueOf(PostgreSQL.NextFreeId("protocol")));
            }
            statusMgs.set(0,"Done");
            ui.setStatusBar(statusMgs);
        });
    }

    public void newProtocol() {
        protocolID = 0;
        ui.btnDel.setDisable(true);
        ui.btnClear.setDisable(false);
    }

    public void loadProtocol(int id) {
        protocolID = id;
        ui.btnClear.setDisable(true);

        protocol = ProtocolDataModel.load(id);
        assert protocol != null;
        ui.tfId.setText(String.valueOf(protocol.getId()));
        ui.dpDate1.setValue(protocol.getRecorded());
        ui.cbPerson.getSelectionModel().select(protocol.person());
        ui.cbProtocolType.getSelectionModel().select(protocol.protocolType());
        ui.taSummary.setText(protocol.getSummary());
        ui.dpDate2.setValue(protocol.getRequested());
        ui.cbStatus.setValue(protocol.getStatus());
        ui.dpDate4.setValue(protocol.getReceipted());
        ui.dpDate4.setValue(protocol.getForwarded());
        ui.dpDate5.setValue(protocol.getChecked());
    }

    public ActionResult getResultAction() {
        return winResult;
    }

    public Protocol getResultProtocol() {
        return protocol;
    }
}
