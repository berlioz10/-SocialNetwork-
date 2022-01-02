package com.example.lab_gui;

import Exceptions.BusinessException;
import Exceptions.RepoException;
import Exceptions.ValidateException;
import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 400);

        stage.setScene(scene);
        stage.setMinHeight(200);
        stage.setMinWidth(271);

        stage.setMaxHeight(200);
        stage.setMaxWidth(271);
        stage.setMaxHeight(200);
        stage.setHeight(200);
        stage.setWidth(271);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}