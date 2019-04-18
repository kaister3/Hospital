package main.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Hospital Register System");

        try {
            //Load the fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../resource/view/Login.fxml"));
            AnchorPane login = loader.load();

            //Show the scene containing the login window.
            Scene scene = new Scene(login, login.getPrefWidth(), login.getPrefHeight());
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
