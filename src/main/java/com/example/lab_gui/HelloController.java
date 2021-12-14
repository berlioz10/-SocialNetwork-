package com.example.lab_gui;

import Control.Controller;
import Domain.Friendship;
import Domain.FriendshipDTO;
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
    private TableView<FriendshipDTO> friendshipTable;

    @FXML
    private TableColumn<FriendshipDTO, String> userColumn;

    @FXML
    private TableColumn<FriendshipDTO, String> statusColumn;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void load() {
        try {
            List<FriendshipDTO> friendships = controller.getAllTypesOfFriendshipsOf(6);

            userColumn.setCellValueFactory((data) -> new SimpleStringProperty(data.getValue().getFirst_name()));
            statusColumn.setCellValueFactory((data) -> new SimpleStringProperty(data.getValue().getStatus()));

            friendshipTable.setItems(FXCollections.observableList(friendships));


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onClick() {
        load();
    }
}