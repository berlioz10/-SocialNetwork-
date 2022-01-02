package com.example.lab_gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class SignUpController {
    @FXML
    public Button signUpButton;

    @FXML
    public void signUpClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 400);

        Stage stage = (Stage) signUpButton.getScene().getWindow();

        stage.close();

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
}
