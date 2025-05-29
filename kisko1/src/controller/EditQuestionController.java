package controller;

import java.util.List;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import model.Answer;
import model.Question;
import javafx.scene.control.Button;

import java.util.Optional;

import database.DBConnection;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class EditQuestionController {

    @FXML
    private TextArea questionArea;

    @FXML
    private TextArea answer1Area;

    @FXML
    private TextArea answer2Area;

    @FXML
    private TextArea answer3Area;

    @FXML
    private TextArea answer4Area;

    @FXML
    private Button saveButton;

    @FXML
    private Button quitButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button backButton;

    private Question question;

    private AdminMenuController adminMenuController;
    private QuestionUpdateListener listener;
    private Runnable onCloseCallback;

    @FXML
    private void initialize() {
        // populateFieldsFromStatic();
        saveButton.setOnAction(e -> saveChanges());
        quitButton.setOnAction(e -> closeWindow());
        backButton.setOnAction(e -> closeWindow());
        deleteButton.setOnAction(e -> deleteQuestion());
    }

    // Callback that is called when the window is closed to refresh the question
    // display
    public void setOnCloseCallback(Runnable callback) {
        this.onCloseCallback = callback;
    }

    // Sets a listener that is triggered when a question is updated (to refresh the
    // GUI)
    public void setQuestionUpdateListener(QuestionUpdateListener listener) {
        this.listener = listener;
    }

    public void setAdminMenuController(AdminMenuController controller) {
        this.adminMenuController = controller;
    }

    // Selected question for editing
    public void setQuestion(Question question) {
        this.question = question;
        populateFields();
    }

    // Answers of selected question
    public void populateFields() {
        if (question != null) {
            questionArea.setText(question.getText());

            List<Answer> answers = question.getAnswers();

            answer1Area.setText(answers.get(0).getAnswerText());
            answer2Area.setText(answers.get(1).getAnswerText());
            answer3Area.setText(answers.get(2).getAnswerText());
            answer4Area.setText(answers.get(3).getAnswerText());
            highlightCorrectAnswer(answers);

        }
    }

    // For correct answer to be different then others
    private void highlightCorrectAnswer(List<Answer> answers) {
        resetAnswerStyles();

        if (answers.get(0).isCorrect()) {
            answer1Area.setStyle("-fx-control-inner-background: #c8f7c5;");
        }
        if (answers.get(1).isCorrect()) {
            answer2Area.setStyle("-fx-control-inner-background: #c8f7c5;");
        }
        if (answers.get(2).isCorrect()) {
            answer3Area.setStyle("-fx-control-inner-background: #c8f7c5;");
        }
        if (answers.get(3).isCorrect()) {
            answer4Area.setStyle("-fx-control-inner-background: #c8f7c5;");
        }
    }

    private void resetAnswerStyles() {
        answer1Area.setStyle("");
        answer2Area.setStyle("");
        answer3Area.setStyle("");
        answer4Area.setStyle("");
    }

    private void saveChanges() {
        questionArea.setText(questionArea.getText().trim());
        answer1Area.setText(answer1Area.getText().trim());
        answer2Area.setText(answer2Area.getText().trim());
        answer3Area.setText(answer3Area.getText().trim());
        answer4Area.setText(answer4Area.getText().trim());

        String newQuestionText = questionArea.getText();
        List<Answer> answers = question.getAnswers();

        if (answers.size() >= 4) {
            Task<Void> saveTask = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    // thread for saving
                    DBConnection.updateQuestionText(question.getId(), newQuestionText);

                    DBConnection.updateAnswerText(answers.get(0).getId(), answer1Area.getText());
                    DBConnection.updateAnswerText(answers.get(1).getId(), answer2Area.getText());
                    DBConnection.updateAnswerText(answers.get(2).getId(), answer3Area.getText());
                    DBConnection.updateAnswerText(answers.get(3).getId(), answer4Area.getText());

                    return null;
                }
            };

            saveTask.setOnSucceeded(e -> {
                // back to UI thread to update GUI
                if (adminMenuController != null) {
                    adminMenuController.loadQuestions();
                }
                question.setText(newQuestionText);

                List<Answer> answers1 = question.getAnswers();
                answers1.get(0).setText(answer1Area.getText());
                answers1.get(1).setText(answer2Area.getText());
                answers1.get(2).setText(answer3Area.getText());
                answers1.get(3).setText(answer4Area.getText());

                if (listener != null) {
                    listener.onQuestionUpdated();
                }
                if (onCloseCallback != null) {
                    onCloseCallback.run();
                }

                showAlert("Uspjeh", "Pitanje je uspješno sačuvano.", Alert.AlertType.INFORMATION);
                closeWindow();
            });

            saveTask.setOnFailed(e -> {
                showAlert("Greška", "Nije moguće sačuvati izmjene.", Alert.AlertType.ERROR);
            });

            new Thread(saveTask).start();
        }
    }

    private void deleteQuestion() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Potvrda");
        confirm.setHeaderText("Da li ste sigurni da želite obrisati pitanje?");
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Task<Void> deleteTask = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    DBConnection.deleteQuestionAndAnswers(question.getId());
                    return null;
                }
            };

            deleteTask.setOnSucceeded(e -> {
                if (adminMenuController != null) {
                    adminMenuController.loadQuestions();
                }
                showAlert("Obrisano", "Pitanje uspješno obrisano.", Alert.AlertType.INFORMATION);
                if (onCloseCallback != null) {
                    onCloseCallback.run();
                }
                closeWindow();
            });

            deleteTask.setOnFailed(e -> {
                showAlert("Greška", "Pitanje nije obrisano.", Alert.AlertType.ERROR);
            });

            new Thread(deleteTask).start();
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) questionArea.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public interface QuestionUpdateListener {
        void onQuestionUpdated();
    }

}
