package com.example.lab_gui;

import Exceptions.BusinessException;
import Exceptions.RepoException;
import Exceptions.ValidateException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 400);
        scene.setFill(Color.ALICEBLUE);

        stage.getIcons().add(new Image("file:src/main/resources/Images/Webber.png"));
        stage.setTitle("Webber");
        stage.setScene(scene);

        stage.setMinHeight(320);
        stage.setMinWidth(550);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}