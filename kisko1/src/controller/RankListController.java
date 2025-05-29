package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.RankedUser;

import java.io.IOException;
import java.util.*;

import database.DBConnection;

public class RankListController {

    @FXML
    private Label numCorrectLabel;

    @FXML
    private Button backButton;

    @FXML
    private TableView<RankedUser> rankingTable;

    @FXML
    private TableColumn<RankedUser, String> firstNameColumn;

    @FXML
    private TableColumn<RankedUser, String> lastNameColumn;

    @FXML
    private TableColumn<RankedUser, String> emailColumn;

    @FXML
    private TableColumn<RankedUser, Integer> scoreColumn;

    @FXML
    public void initialize() {
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("correctAnswers"));

        List<RankedUser> userList = DBConnection.getRankingListForCurrentMonth();
        ObservableList<RankedUser> observableList = FXCollections.observableArrayList(userList);
        rankingTable.setItems(observableList);

        // System.out.println("Broj korisnika: " + userList.size());
        // for (RankedUser user : userList) {
        // System.out.println(user.getFirstName() + " " + user.getLastName() + " - " +
        // user.getCorrectAnswers());
        // }

        rankingTable.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(RankedUser user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setStyle("");
                } else {
                    int index = getIndex();
                    if (index >= 0 && index < 10) {
                        setStyle("-fx-font-style: italic; -fx-background-color: #e6ffe6;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });

    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main.fxml"));
            Parent mainRoot = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(mainRoot);

            stage.setScene(scene);
            stage.setTitle("Glavni meni");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

}
