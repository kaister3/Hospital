package main.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.java.controllers.DBConnector;

import java.io.IOException;
import java.sql.SQLException;

public class MainApp extends Application {

    public static Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
        //
    }

    @Override
    public void start(Stage stage) throws Exception {

        primaryStage = stage;
        primaryStage.setTitle("Hospital Register System");

        try {
            DBConnector.getInstance().connectDataBase();
        } catch (SQLException e) {
            System.err.println("failed to connect to sql database");
            System.exit(0);
        }

        try {
            //Load the fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../resource/view/Login.fxml"));
            AnchorPane login = loader.load();

            //Show the scene containing the login window.
            Scene scene = new Scene(login);
            primaryStage.setResizable(true);
            primaryStage.setScene(scene);

            scene.setUserAgentStylesheet(getClass().getResource("../resource/css/Login.css").toExternalForm());

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
