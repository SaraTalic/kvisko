package helper;

import java.io.File;
import java.sql.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class QuestionsImporter {

    public static void main(String[] args) {
        try {

            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/kvisko", "root", "");

            // Parsing XML file
            File xmlFile = new File("src/resources/questions.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);

            doc.getDocumentElement().normalize();

            NodeList questionNodes = doc.getElementsByTagName("question");

            for (int i = 0; i < questionNodes.getLength(); i++) {
                Node qNode = questionNodes.item(i);
                if (qNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element questionElement = (Element) qNode;

                    String questionText = questionElement.getElementsByTagName("text").item(0).getTextContent();

                    String insertQuestionSQL = "INSERT INTO questions (text, times_asked) VALUES (?, 0)";
                    PreparedStatement psQuestion = con.prepareStatement(insertQuestionSQL,
                            Statement.RETURN_GENERATED_KEYS);
                    psQuestion.setString(1, questionText);
                    psQuestion.executeUpdate();

                    ResultSet rs = psQuestion.getGeneratedKeys();
                    int questionId = -1;
                    if (rs.next()) {
                        questionId = rs.getInt(1);
                    }
                    rs.close();
                    psQuestion.close();

                    NodeList answerNodes = questionElement.getElementsByTagName("answer");

                    for (int j = 0; j < answerNodes.getLength(); j++) {
                        String answerText = answerNodes.item(j).getTextContent();
                        boolean isCorrect = false;

                        if (answerText.endsWith("*")) {
                            isCorrect = true;
                            answerText = answerText.substring(0, answerText.length() - 1).trim();
                        }

                        String insertAnswerSQL = "INSERT INTO answers (question_id, answer_text, is_correct) VALUES (?, ?, ?)";
                        PreparedStatement psAnswer = con.prepareStatement(insertAnswerSQL);
                        psAnswer.setInt(1, questionId);
                        psAnswer.setString(2, answerText);
                        psAnswer.setBoolean(3, isCorrect);
                        psAnswer.executeUpdate();
                        psAnswer.close();
                    }
                }
            }

            con.close();
            System.out.println("Pitanja ubacena u bazu.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
