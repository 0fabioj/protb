package app.controller;

import app.view.AboutWin;
import app.view.MainWin;
import core.datamodel.PostgreSQL;
import core.datamodel.ProtocolDataModel;
import core.datamodel.ProtocolTypeDataModel;
import core.model.Protocol;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MainWinHandle {
    MainWin ui;
    List<String> statusMsg = new ArrayList<>();
    ObservableList<Protocol> protocolList;
    FilteredList<Protocol> filteredData;
    SortedList<Protocol> sortedData;
    ProtocolDataModel dataModel;
    ProtocolTypeDataModel protocolTypeDataModel;
    public MainWinHandle() {
        Platform.runLater(() -> {
            try {
                var tryConnect = PostgreSQL.NextFreeId("protocol");
            } catch (Exception ex) {
                Platform.exit();
                System.exit(0);
            }
            ui = new MainWin();
            setUiActions();

            protocolTypeDataModel = new ProtocolTypeDataModel();
            ui.cbFilterType.setItems(protocolTypeDataModel.getProtocolTypeFXBeans());

            dataModel = new ProtocolDataModel();
            protocolList = dataModel.getProtocolFXBeans();

            filteredData = new FilteredList<>(protocolList, b -> true);
            sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(ui.tableViewProtocol.comparatorProperty());
        });

        boolean res = showLoginWin();
        if (!res) {
            Platform.exit();
            System.exit(0);
        }

        ui.tableViewProtocol.setItems(sortedData);
    }

    private void setUiActions() {
        ui.menuItemLogoff.setOnAction(event -> {
            boolean res = showLoginWin();
            if (!res) {
                Platform.exit();
                System.exit(0);
            }
        });
        ui.menuItemProtocol.setOnAction(event -> {
            ProtocolWinHandle protocolWinController = new ProtocolWinHandle();
            protocolWinController.newProtocol();
            protocolWinController.showWindow();
            if (protocolWinController.getResultAction() == ActionResult.INSERT_SUCCESS) {
                dataModel.getProtocolFXBeans().add(protocolWinController.getResultProtocol());
                statusMsg.clear();
                statusMsg.add("1");
                statusMsg.add("Row inserted.");
                ui.updateStatusBar(statusMsg);
            }
        });
        ui.menuItemPerson.setOnAction(event -> {
            PersonWinHandle pwh = new PersonWinHandle();
            pwh.showWindow();
        });
        ui.menuItemProtocolType.setOnAction(event -> {
            ProtocolTypeWinHandle ptwh = new ProtocolTypeWinHandle();
            ptwh.showWindow();
        });
        ui.menuItemAbout.setOnAction(event -> {
            AboutWin aboutWindow = new AboutWin();
            aboutWindow.show();
        });
        ui.tableViewProtocol.setOnMouseClicked(event -> {
            if (!ui.tableViewProtocol.getItems().isEmpty()) {
                int id = ui.tableViewProtocol.getSelectionModel().getSelectedItem().getId();
                int index = ui.tableViewProtocol.getSelectionModel().getSelectedIndex();
                ProtocolWinHandle protocolWinController = new ProtocolWinHandle();
                protocolWinController.loadProtocol(id);
                protocolWinController.showWindow();
                ActionResult result = protocolWinController.getResultAction();
                if (result == ActionResult.UPDATE_SUCCESS) {
                    dataModel.getProtocolFXBeans().set(index,protocolWinController.getResultProtocol());
                    statusMsg.clear();
                    statusMsg.add("1");
                    statusMsg.add("Row updated.");
                    ui.updateStatusBar(statusMsg);
                } else if (result == ActionResult.DELETE_SUCCESS) {
                    dataModel.getProtocolFXBeans().removeIf(protocol -> protocol.getId() == id);
                    statusMsg.clear();
                    statusMsg.add("1");
                    statusMsg.add("Row removed.");
                    ui.updateStatusBar(statusMsg);
                }
            } else {
                statusMsg.clear();
                statusMsg.add("Is empty.");
                ui.updateStatusBar(statusMsg);
            }
        });

        ui.tfFilterId.textProperty().addListener((observable, oldValue, newValue) ->
                filteredData.setPredicate(protocol -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    int idFilter = Utils.ValidateID(newValue);
                    if (protocol.getId() == idFilter) {
                        return true;
                    }
                    else
                        return false;
                }));
        ui.tfFilterPerson.textProperty().addListener((observable, oldValue, newValue) ->
                filteredData.setPredicate(protocol -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    if (protocol.person().getName().toLowerCase().contains(newValue.toLowerCase())) {
                        return true;
                    }
                    else
                        return false;
                }));
        ui.cbFilterType.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            filteredData.setPredicate(protocol -> {
                if (ui.cbFilterType.getSelectionModel().getSelectedIndex() >= 0) {
                    if (protocol.protocolType().getName().contains(newValue.getName())) {
                        return true;
                    }
                    else
                        return false;
                }
                else
                    return true;
            });
        });
        ui.cbFilterPeriod.getSelectionModel().selectedIndexProperty().addListener((options, oldValue, newValue) -> {
            if (newValue == null) {
                newValue = 0;
            }
            if (newValue.intValue() == 7) {
                ui.dpFilterDate1.setDisable(false);
                ui.dpFilterDate2.setDisable(false);
            } else {
                ui.dpFilterDate1.setDisable(true);
                ui.dpFilterDate2.setDisable(true);
            }
            Number finalNewValue = newValue;
            LocalDate today = LocalDate.now();
            LocalDate sevenDaysAgo = today.minusDays(7);
            int thisMonth = today.getMonth().getValue();
            int thisYear = today.getYear();
            LocalDate thirtyDaysAgo = today.minusMonths(1);
            LocalDate twelveMonthsAgo = today.minusYears(1);
            filteredData.setPredicate(protocol -> {
                return switch (finalNewValue.intValue()) {
                    case 0 -> true;
                    case 1 -> protocol.getRecorded().getYear() == thisYear;
                    case 2 -> protocol.getRecorded().getMonth().getValue() == thisMonth;
                    case 3 -> protocol.getRecorded().isAfter(thirtyDaysAgo);
                    case 4 -> protocol.getRecorded().isAfter(twelveMonthsAgo);
                    case 5 -> protocol.getRecorded().isAfter(sevenDaysAgo);
                    case 6 -> protocol.getRecorded().isEqual(today);
                    default -> false;
                };
            });
        });
    }

    private boolean showLoginWin() {
        LoginWinHandle lwh = new LoginWinHandle();
        lwh.showLoginAndWait();
        ActionResult result = lwh.getResultLogin();
        if (result == ActionResult.SUCCESS) {
            statusMsg.clear();
            statusMsg.add("Login successful");
            ui.updateStatusBar(statusMsg);
            return true;
        } else {
            return false;
        }
    }
}
