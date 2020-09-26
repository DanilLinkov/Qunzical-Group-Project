package quinzical.PracticeModule;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class AskPracticeQuestionController implements Initializable {

    public Label categoryLabel;
    public Label questionLabel;
    public Label hintLabel;
    public Slider speedSlider;
    public TextField answerInput;
    public Button submit;

    private String question;
    private String answer;
    private String qType;
    private int lineNumber;

    private PracticeGameManager _practiceGameManager;

    private static AskPracticeQuestionController _instance;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        _instance = this;
        _practiceGameManager = new PracticeGameManager();

    }

    public static AskPracticeQuestionController getInstance() {
        return _instance;
    }

    public void setCategoryName(String name) {
        categoryLabel.setText("Category: " + name);

        String categoriesPath = new File("").getAbsolutePath() + "/categories/" + name + ".txt";

        try {
            List<String> allLines = Files.readAllLines(Paths.get(categoriesPath));
            int randomLineIndex = (int)(Math.random() * (allLines.size()-1));

            String randomQuestion = allLines.get(randomLineIndex);
            randomQuestion.replaceAll("\\s+","");

            List<String> randomQuestionSplit = Arrays.asList(randomQuestion.split("\\s*\\|\\s*"));

            question = randomQuestionSplit.get(0);
            answer = randomQuestionSplit.get(1);
            lineNumber = randomLineIndex;

            questionLabel.setText("Question: " + question);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public AskPracticeQuestionController() {

    }

}
