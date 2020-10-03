package quinzical.PracticeModule.AskQuestion;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import quinzical.PracticeModule.PracticeGameManager;
import quinzical.PracticeModule.PracticeMenuController;
import quinzical.Utilities.AskQuestionUtilities;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class AskPracticeQuestionController implements Initializable {

    public Label categoryLabel;
    public Label questionLabel;
    public Label hintLabel;
    public Label questionTypeLabel;
    public Slider speedAdjustSlider;
    public TextField answerInput;
    public Button submitButton;
    public Button dontKnowButton;
    public Button playClueButton;

    private String question;
    private String[] answer;
    private String qType;
    private int _questionReadingSpeed = 160;
    private int attempts;
    private Process _espeakProcess;

    private PracticeGameManager _practiceGameManager;

    private static AskPracticeQuestionController _instance;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        _instance = this;
        _practiceGameManager = new PracticeGameManager();
        attempts = 3;

        speedAdjustSlider.setValue(_questionReadingSpeed);
        speedAdjustSlider.valueProperty().addListener((e, oldSpeed, newSpeed) -> {
            _questionReadingSpeed = newSpeed.intValue();
        });
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
            qType = randomQuestionSplit.get(1);
            answer = randomQuestionSplit.get(2).trim().split("/");

            questionLabel.setText("Question: " + question);
            questionTypeLabel.setText(qType.substring(0, 1).toUpperCase() + qType.substring(1));

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void handlePlayClueButton() {
        speak(question);
    }

    public void onAnswerSubmit() {
        String playerAnswerInput = answerInput.getText().toLowerCase().trim();

        boolean eventFinished = false;
        for (String correctAnswer : answer) {
            if (playerAnswerInput.equals(correctAnswer.toLowerCase().trim())) {
                eventFinished = true;
                correctAnswerGiven();
                PracticeMenuController.getInstance().setMainStageToPracticeMenuScene();;
            }
        }

        if (!eventFinished) {
            incorrectAnswerGiven();
            if (attempts==0) {
                PracticeMenuController.getInstance().setMainStageToPracticeMenuScene();
            }
        }

    }

    public void handleDontKnowButtonAction() {
        AskQuestionUtilities.answerUnknown(answer[0]);
        PracticeMenuController.getInstance().setMainStageToPracticeMenuScene();
    }

    public void correctAnswerGiven() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Correct");
        alert.setHeaderText("Correct!");
        String contentText = "You are correct!";

        revertReadingSpeedToDefault();
        speak("Correct");

        alert.getDialogPane().setContent(new Label(contentText));
        alert.getDialogPane().setMinWidth(alert.getDialogPane().getWidth());
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    public void incorrectAnswerGiven() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        attempts--;

        alert.setTitle("Incorrect");
        alert.setHeaderText("Incorrect!");
        revertReadingSpeedToDefault();
        String contentText = "";

        if (attempts>0) {
            contentText = "You have " + attempts + " attempt(s) left!";
            speak(contentText);
        }

        if (attempts==1) {

            String answers = "";
            if (answer.length>1) {
                for(int i=0;i<answer.length;i++) {

                    if(!isNumeric(answer[i])){
                        answers+=answer[i].charAt(0);

                        if(i!=answer.length-1){
                            answers+=" or ";
                        }
                    }
                }
            }
            else {
                answers += answer[0].charAt(0);
            }

            hintLabel.setText("Hint: the first letter of the answer is " + answers);
        }

        if (attempts==0) {
            String answers = "";

            if (answer.length>1) {
                for(int i=0;i<answer.length;i++) {
                    answers+=answer[i];
                    if(i!=answer.length-1){
                        answers+=" or ";
                    }
                }
            }
            else {
                answers+=answer[0];
            }

            contentText = "You have run out of attempts!" + "\n\nThe answer to the question \n\n" + question + "\n\nWas " + answers;
            speak(contentText);
        }

        alert.getDialogPane().setContent(new Label(contentText));
        alert.getDialogPane().setMinWidth(alert.getDialogPane().getWidth());
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private void speak(String text) {
        if (_espeakProcess != null && _espeakProcess.isAlive()) {
            _espeakProcess.destroy();
        }

        text = text.replaceAll("\"", "\\\\\"");

        String command = "espeak \"" + text + "\"" + " -s " + _questionReadingSpeed;
        try {
            ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
            _espeakProcess = pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void revertReadingSpeedToDefault() {
        _questionReadingSpeed = 175;
    }

}
