package app.controller;

import app.view.ProtocolTypeWin;
import core.datamodel.ProtocolTypeDataModel;
import core.model.ProtocolType;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

public class ProtocolTypeWinHandle {
    ObservableList<ProtocolType> protocolTypeList;
    ProtocolTypeDataModel dataModel;
    ProtocolTypeWin ui;
    public ProtocolTypeWinHandle() {
        dataModel = new ProtocolTypeDataModel();
        protocolTypeList = dataModel.getProtocolTypeFXBeans();

        ui = new ProtocolTypeWin();
        //ui.tableViewPerson.setItems(personList);
        FilteredList<ProtocolType> filteredData = new FilteredList<>(protocolTypeList, p -> true);
        ui.tfFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                if (person.getName().toLowerCase().contains(newValue.toLowerCase())) {
                    return true;
                }
                else
                    return false;
            });
        });
        SortedList<ProtocolType> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(ui.tableViewProtocolType.comparatorProperty());
        ui.tableViewProtocolType.setItems(sortedData);
        ui.tableViewProtocolType.setOnMouseClicked(event -> {
            if (!ui.tableViewProtocolType.getItems().isEmpty()) {
                ProtocolType item = ui.tableViewProtocolType.getSelectionModel().getSelectedItem();
                ui.tfId.setText(String.valueOf(item.getId()));
                ui.tfName.setText(item.getName());
            } else {
                System.out.println("tabela vazia");
            }
        });

        ui.btnSave.setOnAction(event -> {
            int id = Utils.ValidateID(ui.tfId.getText().trim());
            String name = ui.tfName.getText().trim();

            if (name.isBlank()) {
                System.out.println("Nome em branco");
            } else {
                if (id > 0) {
                    int index = ui.tableViewProtocolType.getSelectionModel().getSelectedIndex();
                    dataModel.getProtocolTypeFXBeans().set(index,new ProtocolType(id,name));
                    //dataModel.getPersonFXBeans().get(index).nameProperty().set(name);
                } else {
                    dataModel.getProtocolTypeFXBeans().add(new ProtocolType(dataModel.generateID(),name));
                }
                ui.clearForm();
            }
        });
        ui.btnDel.setOnAction(event -> {
            int id = Utils.ValidateID(ui.tfId.getText().trim());
            if (id > 0) {
                dataModel.getProtocolTypeFXBeans().removeIf(protocolType -> protocolType.getId() == id);
            } else {
                System.out.println("É zero ou Nao existe na lista");
            }
            ui.clearForm();
        });

        Utils.addTextLimiter(ui.tfName,50);
    }

    public void showWindow() {
        ui.show();
    }
}

