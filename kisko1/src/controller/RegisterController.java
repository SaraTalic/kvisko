package controller;

import java.io.IOException;
import java.sql.ResultSet;

import database.DBConnection;
import helper.EmailSend;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.User;

public class RegisterController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField surnameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label takenUsername;

    @FXML
    private Label takenEmail;

    @FXML
    private Label wrongPassword;

    @FXML
    private Label errorLabel;

    @FXML
    private Button registerButton;

    @FXML
    private Button backButton;

    private DBConnection db = new DBConnection();

    @FXML
    public void initialize() {
        // Listener for username
        usernameField.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.isEmpty()) {
                takenUsername.setText("");
                return;
            }
            checkUsernameAvailability(newText);
        });

        // Listener for email
        emailField.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.isEmpty()) {
                takenEmail.setText("");
                return;
            }
            checkEmailAvailability(newText);
        });

        // Listener for password
        passwordField.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.isEmpty()) {
                wrongPassword.setText("");
                return;
            }
            checkPassword(newText);
        });
    }

    private void checkUsernameAvailability(String username) {
        Task<Boolean> task = new Task<>() {
            @Override
            protected Boolean call() {
                return db.isUsernameTaken(username);
            }
        };

        task.setOnSucceeded(e -> {
            boolean taken = task.getValue();
            if (taken) {
                takenUsername.setText("Korisničko ime je zauzeto.");
            } else {
                takenUsername.setText("");
            }
        });

        new Thread(task).start();
    }

    private void checkEmailAvailability(String email) {
        Task<Boolean> task = new Task<>() {
            @Override
            protected Boolean call() {
                return db.isEmailTaken(email);
            }
        };

        task.setOnSucceeded(e -> {
            boolean taken = task.getValue();
            if (taken) {
                takenEmail.setText("Imejl-adresa je zauzeta.");
            } else {
                takenEmail.setText("");
            }
        });

        new Thread(task).start();
    }

    // Regex for valid email format
    private boolean isValidEmailFormat(String email) {
        return email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    private void checkPassword(String password) {

        Task<Boolean> task = new Task<>() {
            @Override
            protected Boolean call() {
                return db.isPasswordValid(password);
            }
        };

        task.setOnSucceeded(e -> {
            boolean wrong = task.getValue();
            if (wrong) {
                wrongPassword.setText("");
            } else {
                wrongPassword.setText("Lozinka mora sadržavati 8 karaktera, veliko slovo i cifru.");
            }
        });

        new Thread(task).start();
    }

    private void showErrorMessage(String message) {
        errorLabel.setText(message);
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        String name = nameField.getText().trim();
        String surname = surnameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        String username = usernameField.getText().trim();

        if (email.isEmpty() || name.isEmpty() || surname.isEmpty() || password.isEmpty() || username.isEmpty()) {
            showErrorMessage("Neka polja nisu popunjena!");
            return;
        }
        if (!isValidEmailFormat(email)) {
            showErrorMessage("Unesite validnu imejl-adresu.");
            return;
        }

        registerButton.setDisable(true);

        Task<Boolean> registerTask = new Task<>() {
            @Override
            protected Boolean call() {
                return db.registerUser(name, surname, email, password, username);
            }
        };

        registerTask.setOnSucceeded(e -> {
            registerButton.setDisable(false);
            boolean success = registerTask.getValue();
            if (success) {

                User.setUserUsername(username);
                int userId = db.getUserIdByUsername(username);
                User.setUserId(userId);

                Task<Void> emailTask = new Task<>() {
                    @Override
                    protected Void call() throws Exception {
                        EmailSend.sendEmail(email, "Dobrodošli na Kvisko!",
                                "Hvala što ste se registrovali na Kvisko! Uživajte u igri.");
                        return null;
                    }
                };

                emailTask.setOnSucceeded(e1 -> {

                    System.out.println("Email poslat.");
                });

                emailTask.setOnFailed(e1 -> {
                    showErrorMessage("Email dobrodošlice nije poslat.");
                });

                new Thread(emailTask).start();

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main.fxml"));
                    Parent mainRoot = loader.load();

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(mainRoot);

                    stage.setScene(scene);
                    stage.setTitle("Glavni meni");
                    stage.show();

                } catch (IOException ex) {
                    ex.printStackTrace();
                    showAlert(AlertType.ERROR, "Greška", "Ne mogu da otvorim glavni meni.");
                }

                clearFields();

            } else {
                showErrorMessage(
                        "Registracija nije uspjela. Korisničko ime ili email već postoje, ili lozinka nije validna.");
            }
        });

        registerTask.setOnFailed(e -> {
            registerButton.setDisable(false);
            Throwable ex = registerTask.getException();
            ex.printStackTrace();
            showErrorMessage("Dogodila se greška tokom registracije: " + ex.getMessage());
        });

        new Thread(registerTask).start();
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        nameField.clear();
        surnameField.clear();
        emailField.clear();
        usernameField.clear();
        passwordField.clear();
    }

    @FXML
    private void handleBack(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Potvrda");
        alert.setHeaderText("Da li ste sigurni da želite da odustanete?");
        alert.setContentText("Izgubićete sve unesene podatke.");

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

}
