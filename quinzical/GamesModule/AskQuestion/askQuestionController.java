package quinzical.GamesModule.AskQuestion;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import quinzical.GamesModule.GameManager;
import quinzical.GamesModule.GamesMenuController;
import quinzical.GamesModule.SelectQuestion.SelectQuestionController;
import quinzical.MainMenu.MainMenu;
import quinzical.Questions.Question;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class askQuestionController implements Initializable {

    public Label questionInfoLabel;
    public Button submitAnswerButton;
    public TextField answerField;
    public Slider speedAdjustSlider;
    public Button playClueButton;
    public Button dontKnowButton;

    private int _questionReadingSpeed = 160;
    private Question _question;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        _question = SelectQuestionController.getInstance().getSelectedQuestion();
        questionInfoLabel.setText(_question.getParent() + ": $" + _question.getValue());
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
                correctAnswerGiven();
            }
        }

        if (!eventFinished) {
            incorrectAnswerGiven();
        }

        // Return to the Games menu scene.
        GamesMenuController.getInstance().setMainStageToGamesMenuScene();
    }

    public void handleDontKnowButtonAction() {
        _question.getParent().advanceLowestValuedQuestionIndex();
        incorrectAnswerGiven();

        // Return to the main menu scene.
        GamesMenuController.getInstance().setMainStageToGamesMenuScene();
    }

    private void correctAnswerGiven() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        GameManager.getInstance().incrementCurrentScore(_question.getValue());
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

    private void incorrectAnswerGiven() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        // If we want to decrement the winning for incorrect answer, uncomment below line.
//        GameManager.getInstance().decrementCurrentScore(_question.getValue());
        alert.setTitle("Incorrect");
        alert.setHeaderText("Incorrect!");
        String contentText = "The correct answer was: " + _question.get_answer()[0]; // + "\n"
//                + "$" + _question.getValue() + " has been deducted from your current winning.\n\n"
//                + "Your current winning is now $" + Main.getInstance().getCurrentWinning();
        revertReadingSpeedToDefault();
        speak("The correct answer was " + _question.get_answer()[0]);

        // Formats the pop-up.
        alert.getDialogPane().setContent(new Label(contentText));
        alert.getDialogPane().setMinWidth(alert.getDialogPane().getWidth());
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    /**
     * Using a bash function "espeak", a string inside the argument is read by "espeak".
     * @param text A string for espeak to read.
     */
    private void speak(String text) {
        String command = "espeak \"" + text + "\"" + " -s " + _questionReadingSpeed;
        try {
            ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
            pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void revertReadingSpeedToDefault() {
        _questionReadingSpeed = 175;
    }

}
