package quinzical.GamesModule.AskQuestion;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import quinzical.GamesModule.GameManager;
import quinzical.GamesModule.GamesMenuController;
import quinzical.GamesModule.SelectQuestion.SelectQuestionController;
import quinzical.Questions.Question;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class askQuestionController implements Initializable {

    public Label questionInfoLabel;
    public Label whatIsLabel;
    public Button submitAnswerButton;
    public TextField answerField;
    public Slider speedAdjustSlider;
    public Button playClueButton;
    public Button dontKnowButton;

    private int _questionReadingSpeed = 160;
    private Question _question;
    private Process _espeakProcess;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        _question = SelectQuestionController.getInstance().getSelectedQuestion();

        questionInfoLabel.setText(_question.getParent() + ": $" + _question.getValue());
        whatIsLabel.setText(
                _question.get_whatIs().substring(0, 1).toUpperCase() + _question.get_whatIs().substring(1)
        );

        speedAdjustSlider.setValue(_questionReadingSpeed);
        speedAdjustSlider.valueProperty().addListener((e, oldSpeed, newSpeed) -> {
            _questionReadingSpeed = newSpeed.intValue();
        });
        handlePlayClueButton();
    }

    public void handlePlayClueButton() {
        speak(_question.get_clue());
    }

    public void handleSubmitAnswerButtonAction() {
        _question.getParent().advanceLowestValuedQuestionIndex();
        String playerAnswer = answerField.getText().toLowerCase().trim();

        boolean eventFinished = false;
        for (String correctAnswer : _question.get_answer()) {
            if (playerAnswer.equals(correctAnswer.toLowerCase().trim())) {
                eventFinished = true;
                GameManager.getInstance().incrementCurrentScore(_question.getValue());
                correctAnswerGiven();
            }
        }

        if (!eventFinished) {
            GameManager.getInstance().decrementCurrentScore(_question.getValue());
            incorrectAnswerGiven(false);
        }

        checkEveryQuestionAnswered();

        // Return to the Games menu scene.
        GamesMenuController.getInstance().setMainStageToGamesMenuScene();
    }

    public void handleDontKnowButtonAction() {
        _question.getParent().advanceLowestValuedQuestionIndex();
        incorrectAnswerGiven(true);

        checkEveryQuestionAnswered();

        // Return to the main menu scene.
        GamesMenuController.getInstance().setMainStageToGamesMenuScene();
    }

    private void correctAnswerGiven() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Correct");
        alert.setHeaderText("Correct!");
        String contentText = "Added $" + _question.getValue() + " to the current score.\n\n"
                + "Your current score is now $" + GameManager.getInstance().getCurrentScore();
        GameManager.getInstance().updateBestScore();
        revertReadingSpeedToDefault();
        speak("Correct");

        // Formats the pop-up.
        alert.getDialogPane().setContent(new Label(contentText));
        alert.getDialogPane().setMinWidth(alert.getDialogPane().getWidth());
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    private void incorrectAnswerGiven(boolean dontKnowQuestion) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Incorrect");
        alert.setHeaderText("Incorrect!");
        String contentText = "The correct answer was: " + _question.get_answer()[0];
        if (!dontKnowQuestion) {
            contentText += "\n"
                    + "$" + _question.getValue() + " has been deducted from your current winning.\n\n"
                    + "Your current winning is now $" + GameManager.getInstance().getCurrentScore();
        }

        revertReadingSpeedToDefault();
        speak("The correct answer was " + _question.get_answer()[0]);

        // Formats the pop-up.
        alert.getDialogPane().setContent(new Label(contentText));
        alert.getDialogPane().setMinWidth(alert.getDialogPane().getWidth());
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    public void checkEveryQuestionAnswered() {
        if (GameManager.getInstance().isEveryQuestionAnswered()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("Alert");
            alert.setHeaderText("Every Question Answered");
            String contentText = "Congratulations!" + "\n\n"
                    + "You have completed every question in the question board," + "\n"
                    + "and your total score was $" + GameManager.getInstance().getCurrentScore() + ".\n\n"
                    + "The game will now reset and a new set of question board will be ready.";

            // Resetting the game.
            GameManager.getInstance().newGame();

            // Formats the pop-up.
            alert.getDialogPane().setContent(new Label(contentText));
            alert.getDialogPane().setMinWidth(alert.getDialogPane().getWidth());
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
        }
    }

    /**
     * Using a bash function "espeak", a string inside the argument is read by "espeak".
     * @param text A string for espeak to read.
     */
    private void speak(String text) {

        if (_espeakProcess != null && _espeakProcess.isAlive()) {
            _espeakProcess.destroy();
        }

        text.replaceAll("\"", "\\\"");

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
