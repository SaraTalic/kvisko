package controller;

import java.io.IOException;
import java.util.List;

import database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.stage.Stage;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Question;
import model.UserTableModel;

public class AdminMenuController {

    @FXML
    private ComboBox<String> viewSelector;

    @FXML
    private VBox userBox;

    @FXML
    private VBox questionBox;

    // --- User table and columns ---
    @FXML
    private TableView<UserTableModel> userTable;
    @FXML
    private TableColumn<UserTableModel, String> usernameCol;
    @FXML
    private TableColumn<UserTableModel, String> nameCol;
    @FXML
    private TableColumn<UserTableModel, String> surnameCol;
    @FXML
    private TableColumn<UserTableModel, String> emailCol;
    @FXML
    private TableColumn<UserTableModel, Void> userActionCol;

    // --- Question table and columns ---
    @FXML
    private TableView<Question> questionTable;
    @FXML
    private TableColumn<Question, String> textCol;
    @FXML
    private TableColumn<Question, Void> activeCol;
    @FXML
    private TableColumn<Question, Void> questionActionCol;

    @FXML
    private Button addQuesButton;

    @FXML
    private Button logOutButton;

    private DBConnection db = new DBConnection();

    @FXML
    public void initialize() {
        viewSelector.getItems().addAll("Korisnici", "Pitanja");

        // USER TABLE setup
        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameCol.setCellValueFactory(new PropertyValueFactory<>("surname"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        textCol.setCellValueFactory(new PropertyValueFactory<>("text"));
        questionTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Button "Suspenduj"/"Odsuspenduj" user
        userActionCol
                .setCellFactory(new Callback<TableColumn<UserTableModel, Void>, TableCell<UserTableModel, Void>>() {
                    @Override
                    public TableCell<UserTableModel, Void> call(final TableColumn<UserTableModel, Void> param) {
                        return new TableCell<UserTableModel, Void>() {

                            private final Button btn = new Button();

                            {
                                btn.setStyle(
                                        "-fx-background-color: #b87333; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
                                btn.setOnAction(event -> {
                                    UserTableModel user = getTableView().getItems().get(getIndex());
                                    boolean suspended = user.getIsSuspended();

                                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                    alert.setTitle("Potvrda akcije");
                                    alert.setHeaderText(null);
                                    alert.setContentText(suspended
                                            ? "Da li ste sigurni da želite da odsuspendujete korisnika?"
                                            : "Da li ste sigurni da želite da suspendujete korisnika?");

                                    alert.showAndWait().ifPresent(response -> {
                                        if (response == ButtonType.OK) {
                                            boolean success = db.updateSuspendedStatus(user.getUsername(),
                                                    !suspended);
                                            if (success) {
                                                user.setIsSuspended(!suspended);
                                                getTableView().refresh();
                                            } else {
                                                Alert error = new Alert(Alert.AlertType.ERROR);
                                                error.setTitle("Greška");
                                                error.setHeaderText(null);
                                                error.setContentText("Došlo je do greške pri ažuriranju statusa.");
                                                error.showAndWait();
                                            }
                                        }
                                    });
                                });

                            }

                            @Override
                            protected void updateItem(Void item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                } else {
                                    UserTableModel user = getTableView().getItems().get(getIndex());
                                    btn.setText(user.getIsSuspended() ? "Odsuspenduj" : "Suspenduj");
                                    setGraphic(btn);
                                }
                            }
                        };
                    }
                });
        // Button "Aktivira"/"Deaktiviraj" question
        activeCol.setCellFactory(param -> new TableCell<Question, Void>() {
            private final Button activateButton = new Button();

            {
                activateButton.setOnAction(event -> {
                    Question question = getTableView().getItems().get(getIndex());
                    boolean newStatus = !question.getIsActive();

                    Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmAlert.setTitle("Potvrda akcije");
                    confirmAlert.setHeaderText(null);
                    confirmAlert.setContentText(newStatus
                            ? "Da li ste sigurni da želite da aktivirate pitanje?"
                            : "Da li ste sigurni da želite da deaktivirate pitanje?");

                    confirmAlert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            // putting first new status
                            question.setActive(newStatus);

                            // update the status
                            boolean updateSuccess = db.updateQuestionStatus(question);

                            if (updateSuccess) {
                                questionTable.refresh();

                                Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                                infoAlert.setTitle("Informacija");
                                infoAlert.setHeaderText(null);
                                infoAlert.setContentText(
                                        newStatus ? "Pitanje je aktivirano." : "Pitanje je deaktivirano.");
                                infoAlert.showAndWait();
                            } else {
                                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                                errorAlert.setTitle("Greška");
                                errorAlert.setHeaderText(null);
                                errorAlert.setContentText("Došlo je do greške pri ažuriranju statusa pitanja.");
                                errorAlert.showAndWait();
                            }
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Question question = getTableView().getItems().get(getIndex());
                    activateButton.setText(question.getIsActive() ? "Deaktiviraj" : "Aktiviraj");
                    setGraphic(activateButton);
                }
            }
        });

        // button "Izmijeni"
        questionActionCol.setCellFactory(param -> new TableCell<Question, Void>() {
            private final Button editButton = new Button("Izmijeni");

            {
                editButton.setOnAction(event -> {
                    Question question = getTableView().getItems().get(getIndex());

                    openEditWindow(question);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                }
            }
        });

        viewSelector.setOnAction(e -> {
            String selected = viewSelector.getValue();

            if ("Korisnici".equals(selected)) {
                userBox.setVisible(true);
                questionBox.setVisible(false);
                loadUsers();
            } else if ("Pitanja".equals(selected)) {
                userBox.setVisible(false);
                questionBox.setVisible(true);
                loadQuestions();

            }
        });

        // Default view
        viewSelector.setValue("Korisnici");
        userBox.setVisible(true);
        questionBox.setVisible(false);
        loadUsers();
    }

    public void loadUsers() {
        List<UserTableModel> userList = db.getAllNonAdminUsers();
        ObservableList<UserTableModel> observableUserList = FXCollections.observableArrayList(userList);
        userTable.setItems(observableUserList);
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
                    showAlert(AlertType.ERROR, "Greška", "Ne mogu da otvorim login stranicu.");
                }
            }
        });
    }

    public void loadQuestions() {
        List<Question> questions = db.getAllQuestions();
        ObservableList<Question> observableQuestionList = FXCollections.observableArrayList(questions);
        questionTable.setItems(observableQuestionList);
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void openEditWindow(Question question) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/editQuestion.fxml"));
            Parent root = loader.load();

            EditQuestionController controller = loader.getController();
            controller.setQuestion(question);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Uredi pitanje");
            stage.show();

            controller.setOnCloseCallback(() -> {
                loadQuestions(); // load from db
                refreshTableView(); // refresh table
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshTableView() {
        questionTable.refresh();
    }

    @FXML
    private void handleAddQuestion(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/addQuestion.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Dodaj novo pitanje");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadQuestions();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška");
            alert.setHeaderText(null);
            alert.setContentText("Greška pri otvaranju forme za dodavanje pitanja.");
            alert.showAndWait();
        }
    }

}
