package database;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javafx.scene.control.Alert.AlertType;
import model.Answer;
import model.LoginStatus;
import model.Question;
import model.RankedUser;
import model.UserTableModel;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DBConnection {

    // Connection
    public static Connection getConnection() {
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/kvisko", "root", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }

    // Login
    public boolean login(String username, String password) {
        String hashedPassword = md5Hash(password);

        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection con = getConnection();
                PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);

            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Registration
    public boolean registerUser(String name, String surname, String email, String password, String username) {
        if (!isPasswordValid(password)) {

            return false;
        }

        String checkUserQuery = "SELECT * FROM users WHERE username = ? OR email = ?";
        String insertQuery = "INSERT INTO users (name, surname, email, password, username) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = getConnection();
                PreparedStatement checkStmt = con.prepareStatement(checkUserQuery)) {

            checkStmt.setString(1, username);
            checkStmt.setString(2, email);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                System.out.println("Korisnicko ime ili email vec postoji.");
                return false;
            }

            try (PreparedStatement insertStmt = con.prepareStatement(insertQuery)) {
                insertStmt.setString(1, name);
                insertStmt.setString(2, surname);
                insertStmt.setString(3, email);

                String hashedPassword = md5Hash(password);
                insertStmt.setString(4, hashedPassword);

                insertStmt.setString(5, username);
                int rowsInserted = insertStmt.executeUpdate();
                return rowsInserted > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Password validate
    public boolean isPasswordValid(String password) {
        // Check for 8 char
        if (password.length() < 8) {
            return false;
        }

        boolean capsLk = false;
        boolean digit = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                capsLk = true;
            } else if (Character.isDigit(c)) {
                digit = true;
            }
        }

        return capsLk && digit;
    }

    public LoginStatus loginStatus(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            return LoginStatus.EMPTY_FIELDS;
        }

        String query = "SELECT password FROM users WHERE username = ?";
        try (Connection con = getConnection();
                PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                return LoginStatus.USERNAME_NOT_FOUND;
            }

            String storedPasswordHash = rs.getString("password");
            String hashedInputPassword = md5Hash(password);

            if (storedPasswordHash.equals(hashedInputPassword)) {
                return LoginStatus.SUCCESS;
            } else {
                return LoginStatus.WRONG_PASSWORD;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return LoginStatus.ERROR;
        }
    }

    // MD5 hash password
    private String md5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Check if username is taken
    public boolean isUsernameTaken(String username) {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection con = getConnection();
                PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Check if email is taken
    public boolean isEmailTaken(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (Connection con = getConnection();
                PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Choose 15 question for quiz
    public List<Question> get15OrderedRandomQuestions() {
        String query = "SELECT * FROM questions WHERE is_active = TRUE ORDER BY times_asked ASC";
        String answersQuery = "SELECT * FROM answers WHERE question_id = ?";

        List<Question> selectedQuestions = new ArrayList<>();

        try (Connection con = getConnection();
                PreparedStatement stmt = con.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {

            Map<Integer, List<Question>> groupedByAsked = new LinkedHashMap<>();

            // group questions on times_asked
            while (rs.next()) {
                int id = rs.getInt("id");
                String text = rs.getString("text");
                int timesAsked = rs.getInt("times_asked");
                boolean isActive = rs.getBoolean("is_active");

                List<Answer> answers = new ArrayList<>();
                try (PreparedStatement aStmt = con.prepareStatement(answersQuery)) {
                    aStmt.setInt(1, id);
                    ResultSet ars = aStmt.executeQuery();
                    while (ars.next()) {
                        int answerId = ars.getInt("id");
                        String answerText = ars.getString("answer_text");
                        boolean isCorrect = ars.getInt("is_correct") == 1;
                        answers.add(new Answer(answerId, answerText, isCorrect));
                    }
                }

                Collections.shuffle(answers);
                Question q = new Question(id, text, answers, isActive);

                groupedByAsked.computeIfAbsent(timesAsked, k -> new ArrayList<>()).add(q);
            }

            for (List<Question> group : groupedByAsked.values()) {
                Collections.shuffle(group);
                for (Question q : group) {
                    if (selectedQuestions.size() >= 15)
                        break;
                    selectedQuestions.add(q);
                }
                if (selectedQuestions.size() >= 15)
                    break;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        // System.out.println("Broj pitanja u listi: " + selectedQuestions.size());

        return selectedQuestions;
    }

    // Increment of times_asked
    public void incrementTimesAsked(int questionId) {
        String updateQuery = "UPDATE questions SET times_asked = times_asked + 1 WHERE id = ?";

        try (Connection con = getConnection();
                PreparedStatement stmt = con.prepareStatement(updateQuery)) {

            stmt.setInt(1, questionId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // get UserId by Username
    public int getUserIdByUsername(String username) {
        String query = "SELECT id FROM users WHERE username = ?";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // if user is not found
    }

    // Save results
    public void saveQuizResult(int userId, int numCorrectAnswers) {
        String query = "INSERT INTO results (user_id, num_correct_answers, quiz_date) VALUES (?, ?, NOW())";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, numCorrectAnswers);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Forming a ranking list for current month, only the best result of one user will be shown
    public static List<RankedUser> getRankingListForCurrentMonth() {
        List<RankedUser> rankedUsers = new ArrayList<>();

        String query = """
                SELECT u.name, u.surname, u.email, MAX(r.num_correct_answers) AS max_correct
                FROM results r
                JOIN users u ON r.user_id = u.id
                WHERE MONTH(r.quiz_date) = ? AND YEAR(r.quiz_date) = ? AND u.is_suspended = FALSE
                GROUP BY r.user_id
                """;

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            LocalDate today = LocalDate.now();
            stmt.setInt(1, today.getMonthValue());
            stmt.setInt(2, today.getYear());
            System.out.println(today);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                rankedUsers.add(new RankedUser(
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("email"),
                        rs.getInt("max_correct")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // sorting
        rankedUsers.sort(Comparator
                .comparingInt(RankedUser::getCorrectAnswers).reversed()
                .thenComparing(RankedUser::getFirstName));

        return rankedUsers;
    }

    // Check if user is Admin
    public boolean isUserAdmin(String username) {
        String query = "SELECT is_admin FROM users WHERE username = ?";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("is_admin");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Check if user is Suspended
    public boolean isUserSuspended(String username) {
        String query = "SELECT is_suspended FROM users WHERE username = ?";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("is_suspended");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // List of all non admin users (needed for users table in admin panel)
    public static List<UserTableModel> getAllNonAdminUsers() {
        List<UserTableModel> users = new ArrayList<>();

        String query = "SELECT username, name, surname, email, is_admin, is_suspended FROM users WHERE is_admin = FALSE";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String username = rs.getString("username");
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                String email = rs.getString("email");
                boolean isAdmin = rs.getBoolean("is_admin");
                boolean isSuspended = rs.getBoolean("is_suspended");

                UserTableModel user = new UserTableModel(username, name, surname, email, isAdmin, isSuspended);
                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    // Update suspended status of user
    public static boolean updateSuspendedStatus(String username, boolean newStatus) {
        String query = "UPDATE users SET is_suspended = ? WHERE username = ?";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setBoolean(1, newStatus);
            stmt.setString(2, username);
            int affectedRows = stmt.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get answers of question
    public static List<Answer> getAnswersByQuestionId(int questionId) {
        List<Answer> answers = new ArrayList<>();
        String answersQuery = "SELECT * FROM answers WHERE question_id = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(answersQuery)) {
            ps.setInt(1, questionId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String answerText = rs.getString("answer_text");
                    boolean isCorrect = rs.getBoolean("is_correct");

                    Answer answer = new Answer();
                    answer.setId(id);
                    answer.setText(answerText);
                    answer.setCorrect(isCorrect);
                    answers.add(answer);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return answers;
    }

    // List of all questions (needed for admin panel)
    public static List<Question> getAllQuestions() {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT id, text, is_active FROM questions";

        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String text = rs.getString("text");
                boolean isActive = rs.getBoolean("is_active");

                Question question = new Question(id, text, null, isActive);
                List<Answer> answers = getAnswersByQuestionId(id);
                question.setAnswers(answers);
                questions.add(question);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }

    // Update question status - is active/non active
    public static boolean updateQuestionStatus(Question question) {
        String sql = "UPDATE questions SET is_active = ? WHERE id = ?";

        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, question.getIsActive());
            ps.setInt(2, question.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // Update question text
    public static void updateQuestionText(int questionId, String newText) {
        String sql = "UPDATE questions SET text = ? WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newText);
            stmt.setInt(2, questionId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update Answer Text
    public static void updateAnswerText(int answerId, String newText) {
        String sql = "UPDATE answers SET answer_text = ? WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newText);
            stmt.setInt(2, answerId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete question and its answers
    public static void deleteQuestionAndAnswers(int questionId) {
        String deleteAnswers = "DELETE FROM answers WHERE question_id = ?";
        String deleteQuestion = "DELETE FROM questions WHERE id = ?";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false); // because we are deleting more things

            try (
                    PreparedStatement stmtAnswers = conn.prepareStatement(deleteAnswers);
                    PreparedStatement stmtQuestion = conn.prepareStatement(deleteQuestion)) {
                stmtAnswers.setInt(1, questionId);
                stmtAnswers.executeUpdate();

                stmtQuestion.setInt(1, questionId);
                stmtQuestion.executeUpdate();

                conn.commit();
            } catch (SQLException ex) {
                conn.rollback(); 
                ex.printStackTrace();
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Adding a new question
    public static boolean addQuestion(Question question) {
        String insertQuestionSQL = "INSERT INTO questions (text, is_active) VALUES (?, ?)";
        String insertAnswerSQL = "INSERT INTO answers (question_id, answer_text, is_correct) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
                PreparedStatement psQuestion = conn.prepareStatement(insertQuestionSQL,
                        Statement.RETURN_GENERATED_KEYS);
                PreparedStatement psAnswer = conn.prepareStatement(insertAnswerSQL)) {

            conn.setAutoCommit(false);

            psQuestion.setString(1, question.getText());
            psQuestion.setBoolean(2, question.getIsActive());
            psQuestion.executeUpdate();

            ResultSet generatedKeys = psQuestion.getGeneratedKeys();
            if (generatedKeys.next()) {
                int questionId = generatedKeys.getInt(1);

                for (Answer ans : question.getAnswers()) {
                    psAnswer.setInt(1, questionId);
                    psAnswer.setString(2, ans.getAnswerText());
                    psAnswer.setBoolean(3, ans.isCorrect());
                    psAnswer.addBatch();
                }

                psAnswer.executeBatch();
                conn.commit();
                return true;

            } else {
                conn.rollback();
                System.err.println("Ne mogu dobiti ID novounesenog pitanja.");
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
