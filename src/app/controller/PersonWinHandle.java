package app.controller;

import app.view.PersonWin;
import core.datamodel.PersonDataModel;
import core.model.Person;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

public class PersonWinHandle {
    ObservableList<Person> personList;
    PersonDataModel dataModel;
    PersonWin ui;
    public PersonWinHandle() {
        dataModel = new PersonDataModel();
        personList = dataModel.getPersonFXBeans();

        ui = new PersonWin();
        //ui.tableViewPerson.setItems(personList);
        FilteredList<Person> filteredData = new FilteredList<>(personList, p -> true);
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
        SortedList<Person> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(ui.tableViewPerson.comparatorProperty());
        ui.tableViewPerson.setItems(sortedData);
        ui.tableViewPerson.setOnMouseClicked(event -> {
            if (!ui.tableViewPerson.getItems().isEmpty()) {
                Person item = ui.tableViewPerson.getSelectionModel().getSelectedItem();
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
                    int index = ui.tableViewPerson.getSelectionModel().getSelectedIndex();
                    dataModel.getPersonFXBeans().set(index,new Person(id,name));
                    //dataModel.getPersonFXBeans().get(index).nameProperty().set(name);
                } else {
                    dataModel.getPersonFXBeans().add(new Person(dataModel.generateID(),name));
                }
                ui.clearForm();
            }
        });
        ui.btnDel.setOnAction(event -> {
            int id = Utils.ValidateID(ui.tfId.getText().trim());
            if (id > 0) {
                dataModel.getPersonFXBeans().removeIf(person -> person.getId() == id);
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
