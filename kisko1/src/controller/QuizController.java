package controller;

import java.io.IOException;
import java.util.List;

import database.DBConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import model.Answer;
import model.Question;
import model.User;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

import javafx.event.ActionEvent;

public class QuizController {
    @FXML
    private Label questionLabel;

    @FXML
    private Button answer1Field;

    @FXML
    private Button answer2Field;

    @FXML
    private Button answer3Field;

    @FXML
    private Button answer4Field;

    @FXML
    private Button jokerField;

    @FXML
    private Button quitButton;

    @FXML
    private Label timerLabel;

    @FXML
    private Label resultLabel;

    @FXML
    private Button returnToMenuButton;

    @FXML
    private Button rankListButton;

    private Timeline timer;
    private int secondsLeft = 60;
    private int remainingJokers = 3;
    private boolean jokerUsedOnCurrentQuestion = false;

    private DBConnection db = new DBConnection();

    private List<Question> quizQuestions;

    private int currentIndex = 0;
    private Answer correctAnswer;
    private int correctCount = 0;

    private void useJoker5050() {
        if (jokerUsedOnCurrentQuestion) {
            System.out.println("Dzoker je iskoristen za ovo pitanje.");
            return;
        }

        if (remainingJokers <= 0) {
            System.out.println("Nema vise dostupnih dzokera.");
            return;
        }

        List<Answer> answers = quizQuestions.get(currentIndex).getAnswers();
        Answer correct = answers.stream().filter(Answer::isCorrect).findFirst().orElse(null);
        List<Answer> incorrect = answers.stream().filter(a -> !a.isCorrect()).toList();

        if (correct == null || incorrect.size() < 1)
            return;

        Answer randomIncorrect = incorrect.get((int) (Math.random() * incorrect.size()));
        Button[] buttons = { answer1Field, answer2Field, answer3Field, answer4Field };

        for (int i = 0; i < answers.size(); i++) {
            Answer a = answers.get(i);
            if (a != correct && a != randomIncorrect) {
                buttons[i].setVisible(false);
            }
        }

        jokerUsedOnCurrentQuestion = true; // Set that joker is used for current question

        remainingJokers--;
        jokerField.setText("50:50 (" + remainingJokers + ")");
        if (remainingJokers == 0) {
            jokerField.setDisable(true);
        }
    }

    @FXML
    public void initialize() {
        jokerField.setOnAction(e -> useJoker5050());
        loadQuizQuestions();
    }

    public void loadQuizQuestions() {
        quizQuestions = db.get15OrderedRandomQuestions();
        // System.out.println("Broj pitanja u listi: " + quizQuestions.size());
        currentIndex = 0;
        showQuestion();
    }

    private void startTimer() {
        if (timer != null) {
            timer.stop();
        }

        secondsLeft = 60;
        timerLabel.setText("60");

        // Timer implemented using JavaFX Timeline to update the countdown every second on the UI thread
        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            secondsLeft--;
            timerLabel.setText(String.valueOf(secondsLeft));

            if (secondsLeft <= 0) {
                timer.stop();
                endQuiz();
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    public void showQuestion() {

        jokerUsedOnCurrentQuestion = false;

        answer1Field.setVisible(true);
        answer2Field.setVisible(true);
        answer3Field.setVisible(true);
        answer4Field.setVisible(true);

        if (quizQuestions == null || quizQuestions.isEmpty()) {
            return;
        }

        if (currentIndex >= quizQuestions.size()) {
            endQuiz();
            return;
        }

        Question question = quizQuestions.get(currentIndex);

        db.incrementTimesAsked(question.getId());

        questionLabel.setText(question.getText());

        List<Answer> answers = question.getAnswers();

        answer1Field.setText(answers.get(0).getAnswerText());
        answer2Field.setText(answers.get(1).getAnswerText());
        answer3Field.setText(answers.get(2).getAnswerText());
        answer4Field.setText(answers.get(3).getAnswerText());

        correctAnswer = answers.stream().filter(Answer::isCorrect).findFirst().orElse(null);

        enableAnswerButtons(true);
        setAnswerButtonHandlers();
        startTimer();
    }

    private void enableAnswerButtons(boolean enable) {
        answer1Field.setDisable(!enable);
        answer2Field.setDisable(!enable);
        answer3Field.setDisable(!enable);
        answer4Field.setDisable(!enable);
    }

    private void setAnswerButtonHandlers() {
        answer1Field.setOnAction(e -> checkAnswer(0));
        answer2Field.setOnAction(e -> checkAnswer(1));
        answer3Field.setOnAction(e -> checkAnswer(2));
        answer4Field.setOnAction(e -> checkAnswer(3));
    }

    private void checkAnswer(int selectedIndex) {
        timer.stop();
        List<Answer> answers = quizQuestions.get(currentIndex).getAnswers();
        Answer selectedAnswer = answers.get(selectedIndex);

        if (selectedAnswer.isCorrect()) {
            correctCount++;
            // System.out.println(correctCount);
            currentIndex++;
            showQuestion();
        } else {
            endQuiz();
        }

    }

    @FXML
    private void handleQuit(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Odustajanje");
        alert.setHeaderText("Da li ste sigurni da želite da odustanete?");

        ButtonType da = new ButtonType("Da");
        ButtonType ne = new ButtonType("Ne", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(da, ne);

        alert.showAndWait().ifPresent(response -> {
            if (response == da) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main.fxml"));
                    Parent mainRoot = loader.load();

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(mainRoot);

                    stage.setScene(scene);
                    stage.setTitle("Main");
                    stage.show();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void endQuiz() {
        int userId = User.getUserId();
        new Thread(() -> {
            db.saveQuizResult(userId, correctCount);
            // System.out.println("Rezultat sacuvan: " + correctCount + " tacnih odgovora");
        }).start();

        enableAnswerButtons(false);
        answer1Field.setVisible(false);
        answer2Field.setVisible(false);
        answer3Field.setVisible(false);
        answer4Field.setVisible(false);
        timerLabel.setVisible(false);
        jokerField.setVisible(false);
        quitButton.setVisible(false);

        questionLabel.setText("Kviz je završen. Hvala na učešću, " + User.getUserUsername() + "!");

        resultLabel.setText("Rezultat: " + correctCount + "/15");
        resultLabel.setVisible(true);

        returnToMenuButton.setVisible(true);
        rankListButton.setVisible(true);
    }

    @FXML
    private void handleReturnToMenu(ActionEvent event) {
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

    @FXML
    private void handleRankList(ActionEvent event) {
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

}
