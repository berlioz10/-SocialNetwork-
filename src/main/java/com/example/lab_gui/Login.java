package com.example.lab_gui;

import Control.Controller;
import Domain.User;
import Domain.UserDTO;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.sql.SQLException;

public class Login {
    @FXML
    public TextField usernameBox;
    @FXML
    public TextField passwordBox;
    @FXML
    public Button loginButton;
    @FXML
    public Button leaveButton;

    @FXML
    public void onLoginClicked(ActionEvent actionEvent) throws IOException {
        int id = Integer.parseInt(usernameBox.getText());
        User user = null;

        String err = "";
        try {
            user = Controller.getInstance().findUser(id);
            if(user == null)
                err += "User does not exist!\n";
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(err.length() > 0) {
            Alert.AlertType.valueOf(err);
        }
        else {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 320, 400);

            //makes all windows related to this application to use the same icon given by the relative path
            Window.getWindows().addListener((ListChangeListener<Window>) c -> {
                while (c.next()) {
                    for (Window window : c.getAddedSubList()) {
                        if (window instanceof Stage) {
                            ((Stage) window).getIcons().setAll(new Image("file:src/main/resources/Images/Webber.png"));
                        }
                    }
                }
            });

            Stage stage = new Stage();

            stage.setTitle("Webber");
            stage.setScene(scene);

            stage.setUserData(new UserDTO(user.getId(), user.getFirstName(), user.getSurname()));

            stage.setMinHeight(315);
            stage.setMinWidth(580);

            stage.setMaxHeight(490);
            stage.setMaxWidth(980);
            stage.show();
            Stage stageClosing = (Stage) leaveButton.getScene().getWindow();

            stageClosing.close();
        }
    }
}
