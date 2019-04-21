package main.java.controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.util.StringConverter;
import main.java.layout.Income;
import main.java.MainApp;
import main.java.layout.Patient;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DoctorPanel {
    @FXML private JFXTabPane tabPane;
    @FXML private Tab patientTab;
    @FXML private JFXTreeTableView<Patient> patientTable;
    @FXML private TreeTableColumn<Patient, String> columnId;
    @FXML private TreeTableColumn<Patient, String> columnPatientName;
    @FXML private TreeTableColumn<Patient, String> columnDate;
    @FXML private TreeTableColumn<Patient, String> columnType;
    @FXML private TreeTableView<Income> salaryTable;
    @FXML private TreeTableColumn<Income, String> columnOffice;
    @FXML private TreeTableColumn<Income, String> columnDId;
    @FXML private TreeTableColumn<Income, String> columnDName;
    @FXML private TreeTableColumn<Income, String> columnRegistered;
    @FXML private TreeTableColumn<Income, String> columnDType;
    @FXML private TreeTableColumn<Income, String> columnIncome;
    @FXML private Tab salaryTab;
    @FXML private Label toLabel;
    @FXML private Label fromLabel;
    @FXML private JFXTimePicker startTime;
    @FXML private JFXTimePicker endTime;
    @FXML private JFXDatePicker startDate;
    @FXML private JFXDatePicker endDate;
    @FXML private JFXButton reFreshButton;
    @FXML private Tab helpTab;
    @FXML private JFXButton aboutButton;
    @FXML private JFXButton exitButton;
    @FXML private JFXButton gameButton;
    @FXML private JFXToggleButton eyeProtect;
    @FXML private Label copyrightLabel;
    @FXML private Label greetingLabel;
    @FXML private ImageView doctorMale;
    @FXML private ImageView doctorFemale;
    @FXML private JFXSpinner spinner;

    // set by LoginController
    public static String doctorName;
    public static String doctorNumber;

    private ObservableList<Patient> listPatient = FXCollections.observableArrayList();
    private ObservableList<Income> listIncome = FXCollections.observableArrayList();

    private TreeItem<Patient> rootPatient;
    private TreeItem<Income> rootIncome;

    @FXML
    public void initialize() {
        // initialize patient table
        columnId.setCellValueFactory(
                new TreeItemPropertyValueFactory<Patient, String>("id")
        );
        columnPatientName.setCellValueFactory(
                new TreeItemPropertyValueFactory<Patient, String>("name")
        );
        columnDate.setCellValueFactory(
                new TreeItemPropertyValueFactory<Patient, String>("date")
        );
        columnType.setCellValueFactory(
                new TreeItemPropertyValueFactory<Patient, String>("type")
        );
        rootPatient = new RecursiveTreeItem<>(listPatient, RecursiveTreeObject::getChildren);
        patientTable.setRoot(rootPatient);
        patientTable.setShowRoot(false);

        //initialize the income table
        columnOffice.setCellValueFactory(
                new TreeItemPropertyValueFactory<Income, String>("department")
        );
        columnDId.setCellValueFactory(
                new TreeItemPropertyValueFactory<Income, String>("id")
        );
        columnDName.setCellValueFactory(
                new TreeItemPropertyValueFactory<Income, String>("name")
        );
        columnDType.setCellValueFactory(
                new TreeItemPropertyValueFactory<Income, String>("type")
        );
        columnRegistered.setCellValueFactory(
                new TreeItemPropertyValueFactory<Income, String>("number")
        );
        columnIncome.setCellValueFactory(
                new TreeItemPropertyValueFactory<Income, String>("income")
        );
        rootIncome = new RecursiveTreeItem<>(listIncome, RecursiveTreeObject::getChildren);
        salaryTable.setRoot(rootIncome);
        salaryTable.setShowRoot(false);

        //exit action
        exitButton.setOnAction(event -> MainApp.primaryStage.close());

        //refresh the data
        reFreshButton.setOnAction(event -> {
            if(tabPane.getSelectionModel().getSelectedItem() == patientTab) {
                ResultSet result = DBConnector.getInstance().getPatientForDoctor();
                try {
                    listPatient.clear();
                    while (result.next()) {
                        Patient add = new Patient(
                                result.getString("GHBH"),
                                result.getString("BRMC"),
                                result.getTimestamp("RQSJ"),
                                result.getBoolean("SFZJ"));
                        listPatient.add(add);
                        System.out.println(add.id+" "+add.name);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            else if (tabPane.getSelectionModel().getSelectedItem() == salaryTab){
                ResultSet result = DBConnector.getInstance().getIncomeInfo();
                try {
                    ResultSetMetaData rsm = result.getMetaData();
                    int a = rsm.getColumnCount();
                    for (int i = 0; i < a; i++) {
                        System.out.println(rsm.getColumnName(i+1));
                    }
                    listIncome.clear();
                    while (result.next()) {
                        listIncome.add(new Income(
                                result.getString("KSMC"),
                                //result.getString(Config.NameTableColumnDoctorNumber),
                                result.getString("YSBH"),
                                result.getString("YSMC"),
                                //result.getBoolean(Config.NameTableColumnCategoryRegisterIsSpecialist),
                                result.getBoolean("SFZJ"),
                                //result.getInt(Config.NameTableColumnRegisterCurrentRegisterCount),
                                result.getInt("GHRC"),
                                (double) 0
                        ));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void tabSelectionChanged(Event event) {
        if(((Tab)(event.getTarget())).isSelected());
    }
}

class DateConverter extends StringConverter<LocalDate> {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Override
    public String toString(LocalDate localDate) {
        if(localDate != null){
            return formatter.format(localDate);
        } else {
            return "";
        }
    }

    @Override
    public LocalDate fromString(String s) {
        if(s != null && !s.isEmpty()) {
            return LocalDate.parse(s, formatter);
        } else {
            return null;
        }
    }
}