package quinzical.GamesModule.AskQuestion;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import quinzical.GamesModule.SelectQuestion.SelectQuestionController;
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

    private int _questionReadingSpeed = 160;
//    private String _userAnswer;
    private Question _question;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        _question = SelectQuestionController.getInstance().getSelectedQuestion();
        questionInfoLabel.setText("Places: $400");
        speedAdjustSlider.setValue(_questionReadingSpeed);
        speedAdjustSlider.valueProperty().addListener((e, oldSpeed, newSpeed) -> {
//            System.out.println(newSpeed);
            _questionReadingSpeed = newSpeed.intValue();
        });
        handlePlayClueButton();
    }

    public void handlePlayClueButton() {
        speak(_question.get_clue());
    }

    public void handleSubmitAnswerButtonAction() {
        String playerAnswer = answerField.getText();

        // Answer checking process goes here...
    }

    /**
     * Using a bash function "espeak", a string inside the argument is read by "espeak".
     * @param text A string for espeak to read.
     */
    public void speak(String text) {
        String command = "espeak \"" + text + "\"" + " -s " + _questionReadingSpeed;
//        System.out.println(command);
        try {
            ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
            pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
