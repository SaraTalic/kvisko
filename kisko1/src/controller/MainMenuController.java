package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;

public class MainMenuController {

    @FXML
    private Button startButton;

    @FXML
    private Button rankingButton;

    @FXML
    private Button logOutButton;

    @FXML
    private Label messageLabel;

    private Stage stage;

    @FXML
    private void initialize() {
        startButton.setVisible(true);
        rankingButton.setVisible(true);
    }

    @FXML
    private void handleStart(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/quiz.fxml"));
           // System.out.println("prelaz");
            Parent quizPane = loader.load();

            Scene quizScene = new Scene(quizPane);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(quizScene);
            currentStage.setTitle("Kviz");
            currentStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void handleRanking(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ranking.fxml"));
            AnchorPane rankingPane = loader.load();

            Scene rankingScene = new Scene(rankingPane);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(rankingScene);
            currentStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogOut(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Odjava");
        alert.setHeaderText("Da li ste sigurni da želite da se odjavite?");

        ButtonType da = new ButtonType("Da");
        ButtonType ne = new ButtonType("Ne", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(da, ne);

        alert.showAndWait().ifPresent(response -> {
            if (response == da) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
                    Parent mainRoot = loader.load();

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(mainRoot);

                    stage.setScene(scene);
                    stage.setTitle("Login");
                    stage.show();

                } catch (IOException ex) {
                    ex.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Greška", "Ne mogu da otvorim login stranicu.");
                }
            }
        });
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
