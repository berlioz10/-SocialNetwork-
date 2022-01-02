package com.example.lab_gui;

import Control.Controller;
import Domain.User;
import Domain.UserDTO;
import Utils.CurrentUserSingleTon;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {
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
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(err);
            alert.show();
        }
        else {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            Parent parent = fxmlLoader.load();
            Scene scene = new Scene(parent, 320, 400);
            HelloController helloController = fxmlLoader.getController();

            try {
                helloController.login(new UserDTO(user.getId(), user.getFirstName(), user.getSurname()));
            } catch (SQLException e) {
                e.printStackTrace();
            }

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

            Stage stage = (Stage) loginButton.getScene().getWindow();

            stage.close();
            // Stage stage = new Stage();

            stage.setTitle("Webber");
            stage.setScene(scene);

            stage.setMinHeight(315);
            stage.setMinWidth(580);

            stage.setMaxHeight(490);
            stage.setMaxWidth(980);
            stage.show();
        }
    }
}