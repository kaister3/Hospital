package main.java.controllers;

/*
  Hibernate是一个开放源代码的对象关系映射框架，
  它对JDBC进行了非常轻量级的对象封装，
  使得Java程序员可以随心所欲的使用对象编程思维来操纵数据库
 */

import com.jfoenix.controls.*;
import java.sql.ResultSet;
import com.sun.javafx.robot.impl.FXRobotHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.scene.layout.AnchorPane;
import main.java.Config;
import main.java.MainApp;
import sun.applet.Main;

public class Login{
    public ImageView doctorImageF;
    public ImageView doctorImageM;
    public ImageView patientImage;
    public JFXTextField usernameField;
    public JFXPasswordField passwordField;
    public JFXToggleButton isDoctorToggle;
    public JFXButton loginButton;
    public Label userLabel;
    public Label passLabel;
    public Label usernameFieldPrompt;
    public Label passwordFieldPrompt;
    AtomicBoolean isDoctor = new AtomicBoolean(false);

    @FXML
    void initialize() {
        //Initial the components

        usernameFieldPrompt.setVisible(false);
        passwordFieldPrompt.setVisible(false);

        isDoctorToggle.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                isDoctor.set(true);
                System.out.println("I'm a doctor.");
            } else {
                isDoctor.set(false);
                System.out.println("I'm a patient.");
            }
        });

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (isDoctor.get()) {
                    System.out.println("he");
                    ResultSet result = DBConnector.getInstance().getDoctorInfo("001");
                    //ResultSet result = DBConnector.getInstance().getDoctorInfo(usernameField.getText());
                    if (result == null) {
                        usernameFieldPrompt.setText("sql error");
                    }

                    try {
                        if (!result.next()) {
                            usernameFieldPrompt.setText("invalid user");
                            return;
                        //} else if (!result.getString(Config.NameTableColumnDoctorPassword).equals(passwordField.getText())) {
                        } else if (!result.getString(Config.NameTableColumnDoctorPassword).equals("001001")) {
                            passwordFieldPrompt.setText("invalid password");
                            return;
                        }

                        DoctorPanel.doctorName = result.getString(Config.NameTableColumnDoctorName);
                        DoctorPanel.doctorNumber = result.getString(Config.NameTableColumnDoctorNumber);

                        DBConnector.getInstance().updateDoctorLoginTime(
                                result.getString(Config.NameTableColumnDoctorNumber),
                                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

                        try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../resource/view/DoctorPanel.fxml"));
                        AnchorPane anchorPane = loader.load();
                        Scene scene = new Scene(anchorPane, anchorPane.getPrefWidth(), anchorPane.getPrefHeight());
                        scene.getStylesheets().add(getClass().getResource("../../resource/css/DoctorPanel.css").toExternalForm());
                        MainApp.primaryStage.setScene(scene);
                        MainApp.primaryStage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } catch (SQLException e){
                        // program shouldn't came here
                        e.printStackTrace();
                    }
                }
                else {
                    ResultSet result = DBConnector.getInstance().getPatientInfo("001");
                    //ResultSet result = DBConnector.getInstance().getDoctorInfo(usernameField.getText());
                    if (result == null) {
                        usernameFieldPrompt.setText("sql error");
                    }

                    try {
                        if (!result.next()) {
                            usernameFieldPrompt.setText("invalid user");
                            return;
                            //} else if (!result.getString(Config.NameTableColumnDoctorPassword).equals(passwordField.getText())) {
                        } else if (!result.getString(Config.NameTableColumnPatientPassword).equals("001001")) {
                            passwordFieldPrompt.setText("invalid password");
                            return;
                        }

                        DBConnector.getInstance().updateDoctorLoginTime(
                                result.getString(Config.NameTableColumnPatientNumber),
                                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../resource/view/PatientPanel.fxml"));
                            AnchorPane anchorPane = loader.load();
                            Scene scene = new Scene(anchorPane, anchorPane.getPrefWidth(), anchorPane.getPrefHeight());
                            scene.getStylesheets().add(getClass().getResource("../../resource/css/PatientPanel.css").toExternalForm());
                            MainApp.primaryStage.setScene(scene);
                            MainApp.primaryStage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } catch (SQLException e){
                        // program shouldn't came here
                        e.printStackTrace();
                    }
                }
            }
        });

    }
}