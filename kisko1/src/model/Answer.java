package model;

public class Answer {
    private int id;
    private String answerText;
    private boolean isCorrect;

    public Answer() {

    }

    public Answer(int id, String answerText, boolean isCorrect) {
        this.id = id;
        this.answerText = answerText;
        this.isCorrect = isCorrect;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setCorrect(boolean correct) {
        this.isCorrect = correct;
    }

    public void setText(String text) {
        this.answerText = text;
    }

    public boolean isCorrect() {
        return isCorrect;
    }
}
