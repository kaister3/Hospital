package main.java.controllers;

import com.jfoenix.controls.*;
import javafx.fxml.Initializable;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.stage.StageStyle;

import javax.swing.event.ChangeListener;
import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Login implements Initializable {
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Initial the components

        usernameFieldPrompt.setVisible(false);
        passwordFieldPrompt.setVisible(false);

        boolean isDoctor;

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (usernameField.getText().isEmpty()) {
                    usernameFieldPrompt.setVisible(true);
                }
                else if (passwordField.getText().isEmpty()) {
                    passwordFieldPrompt.setVisible(true);
                }
                else {
                    usernameFieldPrompt.setVisible(false);
                    passwordFieldPrompt.setVisible(false);

                    String id = usernameField.getText();
                    String password = passwordField.getText();
                    System.out.println(id + password);
                }
            }
        });

        isDoctorToggle.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                System.out.println("I'm a doctor");
            }
        });
    }
}
