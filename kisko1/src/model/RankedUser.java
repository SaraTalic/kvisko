package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class RankedUser {
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty lastName;
    private final SimpleStringProperty email;
    private final SimpleIntegerProperty correctAnswers;

    public RankedUser(String firstName, String lastName, String email, int correctAnswers) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.email = new SimpleStringProperty(maskEmail(email));
        this.correctAnswers = new SimpleIntegerProperty(correctAnswers);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public String getLastName() {
        return lastName.get();
    }

    public String getEmail() {
        return email.get();
    }

    public int getCorrectAnswers() {
        return correctAnswers.get();
    }

    private String maskEmail(String email) {
        int atIndex = email.indexOf("@");
        if (atIndex < 3)
            return email;

        String visible = email.substring(0, 3);
        String hidden = "*".repeat(atIndex - 3);

        return visible + hidden + email.substring(atIndex);
    }
}
