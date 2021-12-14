package com.example.lab_gui;

import Control.Controller;
import Domain.Friendship;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;
import java.util.List;

public class HelloController {
    private Controller controller;

    @FXML
    private TableView friendshipTable;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void load() {
        try {
            List<Friendship> friendships = (List<Friendship>) controller.getFriendships();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}