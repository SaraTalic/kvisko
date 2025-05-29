package controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import model.Answer;
import model.Question;

import java.util.ArrayList;
import java.util.List;

import database.DBConnection;

public class AddQuestionController {

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
    private Button backButton;

    @FXML
    public void initialize() {
        saveButton.setOnAction(e -> saveQuestion());
        quitButton.setOnAction(e -> ((Stage) quitButton.getScene().getWindow()).close());
        backButton.setOnAction(e -> ((Stage) backButton.getScene().getWindow()).close());
    }

    private void saveQuestion() {
        String questionText = questionArea.getText().trim();
        List<TextArea> answerFields = List.of(answer1Area, answer2Area, answer3Area, answer4Area);
        List<Answer> answers = new ArrayList<>();
        boolean correctFound = false;

        for (TextArea field : answerFields) {
            String rawText = field.getText().trim();
            boolean isCorrect = rawText.endsWith("*");

            if (isCorrect) {
                rawText = rawText.substring(0, rawText.length() - 1).trim();
                correctFound = true;
            }

            if (!rawText.isEmpty()) {
                answers.add(new Answer(0, rawText, isCorrect));
            }
        }

        if (questionText.isEmpty() || answers.size() != 4 || !correctFound) {
            showAlert("Greška", "Unesite pitanje, 4 odgovora i označite jedan tačan odgovor zvjezdicom '*'.");
            return;
        }

        Question q = new Question(0, questionText, answers, true);

        Task<Boolean> saveTask = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                return DBConnection.addQuestion(q);
            }
        };

        saveTask.setOnSucceeded(e -> {
            boolean success = saveTask.getValue();
            if (success) {
                showAlert("Uspjeh", "Pitanje je uspješno sačuvano.");
                ((Stage) saveButton.getScene().getWindow()).close();
            } else {
                showAlert("Greška", "Došlo je do greške pri čuvanju pitanja.");
            }
        });

        saveTask.setOnFailed(e -> {
            showAlert("Greška", "Došlo je do greške pri čuvanju pitanja.");
        });

        new Thread(saveTask).start();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
