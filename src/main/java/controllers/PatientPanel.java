package main.java.controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import main.java.Config;
import main.java.MainApp;
import main.java.layout.Patient;
import main.java.layout.Register;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;

public class PatientPanel {
    @FXML
    private JFXComboBox<String> comboSection;
    @FXML
    private JFXComboBox<String> comboRegName;
    @FXML
    private JFXComboBox<String> comboRegCat;
    @FXML
    private JFXComboBox<String> comboDocName;
    @FXML
    private Label feeLabel;
    @FXML
    private JFXButton refreshButton;
    @FXML
    private JFXSpinner spinner;
    @FXML
    private JFXCheckBox checkBalance;
    @FXML
    private JFXCheckBox checkSaveBalance;
    @FXML
    private JFXButton checkOutButton;
    @FXML
    private JFXButton exitButton;
    @FXML
    private JFXTreeTableView<Register> regTable;
    @FXML
    private TreeTableColumn<Register, String> columnSection;
    @FXML
    private TreeTableColumn<Register, String> columnDoctorName;
    @FXML
    private TreeTableColumn<Register, String> columnRegisterName;
    @FXML
    private TreeTableColumn<Register, String> columnSpecialist;
    @FXML
    private TreeTableColumn<Register, Double> columnMoney;

    private String getKSMC;
    private String getYSMC;
    private String getSFZJ;
    private String getHZMC;

    AtomicBoolean save2Balance = new AtomicBoolean(false);
    AtomicBoolean useBalance = new AtomicBoolean(false);

    private ObservableList<Register> listRegister = FXCollections.observableArrayList();
    private TreeItem<Register> rootRegister;

    public void initialize() {
        //initialize the combobox.
        comboboxDataUpdate();

        //initialize the spinner.
        spinner.setVisible(false);
        //initialize register table.
        columnSection.setCellValueFactory(
                new TreeItemPropertyValueFactory<Register, String>("section")
        );
        columnDoctorName.setCellValueFactory(
                new TreeItemPropertyValueFactory<Register, String>("docName")
        );
        columnRegisterName.setCellValueFactory(
                new TreeItemPropertyValueFactory<Register, String>("regName")
        );
        columnSpecialist.setCellValueFactory(
                new TreeItemPropertyValueFactory<Register, String>("type")
        );
        columnMoney.setCellValueFactory(
                new TreeItemPropertyValueFactory<Register, Double>("money")
        );
        rootRegister = new RecursiveTreeItem<>(listRegister, RecursiveTreeObject::getChildren);
        regTable.setRoot(rootRegister);
        regTable.setShowRoot(false);

        //initialize the check out button
        checkOutButton.setOnAction(event -> {
            spinner.setVisible(true);
        });
        //initialize the exit button.
        exitButton.setOnAction(event -> {
            MainApp.primaryStage.close();
            //bye~
        });
        //initialize the refresh button (select data).
        refreshButton.setOnAction(event -> {
            //get combobox content.
            getKSMC = comboSection.getValue();
            if (getKSMC == null) {
                getKSMC = "";
            }
            getYSMC = comboDocName.getValue();
            if (getYSMC == null) {
                getYSMC = "";
            }
            getHZMC = comboRegName.getValue();
            if (getHZMC == null) {
                getHZMC = "";
            }
            getSFZJ = comboRegCat.getValue();
            if (getSFZJ == "专家号") {
                getSFZJ = "1";
            }
            else if (getSFZJ == "普通号") {
                getSFZJ = "0";
            }
            else getSFZJ = "";
            ResultSet result = DBConnector.getInstance().getRegisterInfo(getKSMC,getYSMC,getSFZJ,getHZMC);
            try {
                listRegister.clear();
                while (result.next()) {
                    Register add = new Register(
                            result.getString("KSMC"),
                            result.getString("YSMC"),
                            result.getBoolean("SFZJ"),
                            result.getString("HZMC"),
                            result.getDouble("GHFY"));
                    listRegister.add(add);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        checkBalance.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                useBalance.set(true);
                System.out.println("use balance");
            } else {
                useBalance.set(false);
                System.out.println("do not use balance");
            }
        });

        checkSaveBalance.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                save2Balance.set(true);
                System.out.println("save to balance");
            } else {
                save2Balance.set(false);
                System.out.println("do not save to balance");
            }
        });

    }

    private void comboboxDataUpdate() {
        ResultSet result = DBConnector.getInstance().getRegisterInfo("","","","");

        ObservableList<String> sectionList = FXCollections.observableArrayList();
        ObservableList<String> docNameList = FXCollections.observableArrayList();
        ObservableList<String> regNameList = FXCollections.observableArrayList();

        try {
            while (result.next()) {
                sectionList.add(result.getString("KSMC"));
                docNameList.add(result.getString("YSMC"));
                regNameList.add(result.getString("HZMC"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        comboSection.setItems(sectionList);
        comboDocName.setItems(docNameList);
        //this could be add straightly
        comboRegCat.setItems(FXCollections.observableArrayList("专家号", "普通号"));
        comboRegName.setItems(regNameList);
    }
}