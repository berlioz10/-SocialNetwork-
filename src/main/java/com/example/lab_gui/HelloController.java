package com.example.lab_gui;

import Control.Controller;
import Domain.Friendship;
import Domain.FriendshipDTO;
import Domain.UserDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;
import java.util.List;

public class HelloController {

    private Controller controller = Controller.getInstance();

    @FXML
    private TableColumn<UserDTO,String> userID;

    @FXML
    private TableColumn<UserDTO,String> userSurname;

    @FXML
    private TableColumn<UserDTO,String> userFirstName;

    @FXML
    private TableView<UserDTO> userTable;

    @FXML
    private TableColumn<FriendshipDTO, String> relationColumn;

    @FXML
    private TableView<FriendshipDTO> friendshipTable;

    @FXML
    private TableColumn<FriendshipDTO, String> userColumn;

    @FXML
    private TableColumn<FriendshipDTO, String> statusColumn;

    @FXML
    public void initialize() {
        load();
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void load() {
        try {

            List<FriendshipDTO> friendships = controller.getAllTypesOfFriendshipsOf(6);

            relationColumn.setCellValueFactory((data) -> new SimpleStringProperty(data.getValue().getStringRelation()));
            userColumn.setCellValueFactory((data) -> new SimpleStringProperty(data.getValue().getSecond_name()));
            statusColumn.setCellValueFactory((data) -> new SimpleStringProperty(data.getValue().getStatus()));

            friendshipTable.setItems(FXCollections.observableList(friendships));


            List<UserDTO> userDTOS = controller.getAllUsersDTO();

            userID.setCellValueFactory((data)-> new SimpleStringProperty(Integer.toString(data.getValue().getId())));
            userFirstName.setCellValueFactory((data)-> new SimpleStringProperty(data.getValue().getFirstName()));
            userSurname.setCellValueFactory((data)-> new SimpleStringProperty(data.getValue().getSurname()));

            userTable.setItems(FXCollections.observableList(userDTOS));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}