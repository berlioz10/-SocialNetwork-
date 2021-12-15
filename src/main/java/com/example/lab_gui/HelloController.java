package com.example.lab_gui;

import Control.Controller;
import Domain.Friendship;
import Domain.FriendshipDTO;
import Domain.UserDTO;
import Exceptions.BusinessException;
import Exceptions.RepoException;
import Exceptions.ValidateException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class HelloController {



    private Controller controller = Controller.getInstance();
    private UserDTO currentUserControl=null;
    private UserDTO passiveUserControl=null;

    // all users
    @FXML
    private TextField selectedUser;

    @FXML
    private TableColumn<UserDTO,String> userID;

    @FXML
    private TableColumn<UserDTO,String> userSurname;

    @FXML
    private TableColumn<UserDTO,String> userFirstName;

    @FXML
    private TableView<UserDTO> userTable;

    @FXML
    private Button changeFriendStatus;

    @FXML
    private Label passiveUserName;


    //hiddenUsers

    @FXML
    private TableColumn<UserDTO,String> hiddenID;

    @FXML
    private TableColumn<UserDTO,String> hiddenSurname;

    @FXML
    private TableColumn<UserDTO,String> hiddenFirstName;

    @FXML
    private TableView<UserDTO> hiddenTable;

    @FXML
    public HBox changeStatusSection;


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

    public void load_friendships(){

        List<FriendshipDTO> friendships=null;
        if(currentUserControl!=null){
            try {
                friendships = controller.getAllTypesOfFriendshipsOf(currentUserControl.getId());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            friendshipTable.setItems(FXCollections.observableList(friendships));
        }

    }

    public void load() {
        try {

            List<FriendshipDTO> friendships=null;
            if(currentUserControl!=null){
                friendships = controller.getAllTypesOfFriendshipsOf(currentUserControl.getId());
                friendshipTable.setItems(FXCollections.observableList(friendships));
            }

            relationColumn.setCellValueFactory((data) -> new SimpleStringProperty(data.getValue().getStringRelation()));
            userColumn.setCellValueFactory((data) -> new SimpleStringProperty(data.getValue().getSecond_name()));
            statusColumn.setCellValueFactory((data) -> new SimpleStringProperty(data.getValue().getStatus()));




            List<UserDTO> userDTOS = controller.getAllUsersDTO();

            userID.setCellValueFactory((data)-> new SimpleStringProperty(Integer.toString(data.getValue().getId())));
            userFirstName.setCellValueFactory((data)-> new SimpleStringProperty(data.getValue().getFirstName()));
            userSurname.setCellValueFactory((data)-> new SimpleStringProperty(data.getValue().getSurname()));

            userTable.setItems(FXCollections.observableList(userDTOS));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void selectedUser(MouseEvent mouseEvent) {
        currentUserControl = userTable.getSelectionModel().getSelectedItem();
        selectedUser.setText(
                currentUserControl.toString()
        );
        load_friendships();
    }

    public void revealPotentialFriends(ActionEvent actionEvent) {
        if(currentUserControl!=null)
        {
            List<UserDTO> hiddenUserDTO;
            hiddenTable.setVisible(true);
            changeStatusSection.setVisible(true);
            changeFriendStatus.setText("Send request");
            try {
                hiddenUserDTO = controller.getAllUsersDTO().stream().filter(
                        x-> {
                            try {
                                return !controller.areFriends(currentUserControl.getId(),x.getId()) &&
                                        x.getId() != currentUserControl.getId();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            return false;
                        }
                ).toList();



                hiddenID.setCellValueFactory((data)-> new SimpleStringProperty(Integer.toString(data.getValue().getId())));
                hiddenFirstName.setCellValueFactory((data)-> new SimpleStringProperty(data.getValue().getFirstName()));
                hiddenSurname.setCellValueFactory((data)-> new SimpleStringProperty(data.getValue().getSurname()));

                hiddenTable.setItems(FXCollections.observableList(hiddenUserDTO));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public void revealCurrentFriends(ActionEvent actionEvent) {
        if(currentUserControl!=null){

            List<UserDTO> hiddenUserDTO;
            hiddenTable.setVisible(true);
            changeStatusSection.setVisible(true);
            changeFriendStatus.setText("Unfriend");
            try {
                hiddenUserDTO = controller.getAllUsersDTO().stream().filter(
                        x-> {
                            try {
                                return controller.areFriends(currentUserControl.getId(),x.getId());
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            return false;
                        }
                ).toList();

                hiddenID.setCellValueFactory((data)-> new SimpleStringProperty(Integer.toString(data.getValue().getId())));
                hiddenFirstName.setCellValueFactory((data)-> new SimpleStringProperty(data.getValue().getFirstName()));
                hiddenSurname.setCellValueFactory((data)-> new SimpleStringProperty(data.getValue().getSurname()));

                hiddenTable.setItems(FXCollections.observableList(hiddenUserDTO));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void changeStatusOfFriendship(ActionEvent actionEvent) {
        if(passiveUserName!=null && currentUserControl!=null){
            AtomicInteger hideFlag = new AtomicInteger();
            if(Objects.equals(changeFriendStatus.getText(), "Send request")){
                try {
                    controller.sendFriendship(currentUserControl.getId(),passiveUserControl.getId());
                    hideFlag.set(1);
                } catch (ValidateException | BusinessException | SQLException | RepoException e) {
                    e.printStackTrace();
                }
            }
            else if(Objects.equals(changeFriendStatus.getText(), "Unfriend")){
                try {
                    Iterable<Friendship> friendshipList = controller.getFriendshipsOf(currentUserControl.getId());
                    friendshipList.forEach(friendship -> {
                                if (friendship.getOne() == passiveUserControl.getId() ||
                                        friendship.getTwo() == passiveUserControl.getId()) {
                                    try {
                                        controller.deleteFriendship(friendship.getId());
                                        hideFlag.set(1);
                                    } catch (RepoException | SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                    );
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if(hideFlag.get() == 1){
                changeStatusSection.setVisible(false);
                hiddenTable.setVisible(false);
                changeFriendStatus.setText("Unfriend\\Befriend");
                load_friendships();
            }
        }

    }

    public void setPassiveUser(MouseEvent mouseEvent) {
        passiveUserControl = hiddenTable.getSelectionModel().getSelectedItem();
        passiveUserName.setText(
                passiveUserControl.toString()
        );
    }
}