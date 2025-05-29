package model;

import java.util.List;

public class Question {
    private int id;
    private String text;
    private List<Answer> answers;
    private boolean isActive;

    public Question(int id, String text, List<Answer> answers, boolean isActive) {
        this.id = id;
        this.text = text;
        this.answers = answers;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }
}
