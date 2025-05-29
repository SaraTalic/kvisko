package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.LoginStatus;
import model.User;

import java.io.IOException;

import database.DBConnection;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField passwordVisibleField;

    @FXML
    private Button togglePasswordVisibilityButton;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    private Label errorLabel;

    private boolean isPasswordVisible = false;
    private DBConnection db = new DBConnection();

    @FXML
    private void handleLogin(ActionEvent event) {

        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        LoginStatus status = db.loginStatus(username, password);

        switch (status) {
            case EMPTY_FIELDS:
                showErrorMessage("Polja ne smiju biti prazna.");
                break;
            case USERNAME_NOT_FOUND:
                showErrorMessage("Korisničko ime ne postoji.");
                break;
            case WRONG_PASSWORD:
                showErrorMessage("Lozinka je neispravna.");
                break;
            case SUCCESS:
                errorLabel.setVisible(false);
                User.setUserUsername(username);
                int userId = db.getUserIdByUsername(username);
                User.setUserId(userId);

                boolean isAdmin = db.isUserAdmin(username);
                boolean isSuspended = db.isUserSuspended(username);
                User.setIsAdmin(isAdmin);
                User.setIsSuspended(isSuspended);

                if (isSuspended) {
                    errorLabel.setVisible(true);
                    showErrorMessage("Vaš nalog je suspendovan. Pristup nije dozvoljen.");
                    return;
                }

                try {
                    FXMLLoader loader;
                    Parent root;
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                    if (isAdmin) {
                        loader = new FXMLLoader(getClass().getResource("/view/adminMenu.fxml"));
                        root = loader.load();
                        stage.setTitle("Administratorski meni");
                    } else {
                        loader = new FXMLLoader(getClass().getResource("/view/main.fxml"));
                        root = loader.load();
                        stage.setTitle("Glavni meni");
                    }

                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                    showErrorMessage("Ne mogu da otvorim meni.");
                }
                break;

            case ERROR:
            default:
                showErrorMessage("Došlo je do greške. Pokušajte ponovo.");
                break;
        }
    }

    @FXML
    private void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;

        passwordVisibleField.setVisible(isPasswordVisible);
        passwordVisibleField.setManaged(isPasswordVisible);

        passwordField.setVisible(!isPasswordVisible);
        passwordField.setManaged(!isPasswordVisible);

        togglePasswordVisibilityButton.setText(isPasswordVisible ? "🙈" : "👁");

        passwordVisibleField.setText(passwordField.getText());
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/register.fxml"));

            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.setTitle("Registracija");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Greška", "Ne mogu da otvorim registracionu formu.");

        }

    }

    @FXML
    private void initialize() {
        passwordVisibleField.textProperty().bindBidirectional(passwordField.textProperty());
    }

    private void showErrorMessage(String message) {
        errorLabel.setText(message);
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
