package quinzical.Utilities;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A utility class that contains methods that are used in AskQuestion views across different modules.
 * <p></p>
 * It provides three major functionalities; unknownAnswer pop up, cleaning up answers,
 * and speak methods.
 *
 * @author Danil Linkov, Hyung Park
 */
public class AskQuestionUtilities {

    private static final String[] macronsLowerCase = {"ā", "ē", "ī", "ō", "ū"};
    private static final String[] macronsUpperCase = {"Ā", "Ē", "Ī", "Ō", "Ū"};
    private static final ArrayList<String> questionTypes = new ArrayList<>(Arrays.asList("What is", "What are", "Who is", "Who are"));

    public static List<String> getQuestionTypes() {
        return questionTypes;
    }

    /**
     * Configures the event action of an array of macron buttons and returns a boolean value on
     * whether a upper case macrons has been added. (simply, a boolean value of isMacronCaps)
     * Such output was decided to simplify any further implementation changes.
     * @param macronButtons an array of buttons with respective macrons to add to the answer field.
     * @param answerField an answer field for macrons to be added when the buttons are pressed.
     * @param isMacronCaps a boolean value on whether the macrons set for buttons are in upper case; capitalized.
     */
    public static void configureMacronButtons(Button[] macronButtons, TextField answerField, boolean isMacronCaps) {
        for (int i = 0; i < macronButtons.length; i++) {
            int finalI = i;
            macronButtons[i].setOnAction(e -> answerField.appendText(isMacronCaps ? macronsUpperCase[finalI] : macronsLowerCase[finalI]));
        }
    }

    public static void macronSwitchCaps(Button[] macronButtons, boolean isMacronCaps, TextField answerField) {
        for (int i = 0; i < macronButtons.length; i++) {
            macronButtons[i].setText(isMacronCaps ? macronsLowerCase[i] : macronsUpperCase[i]);
        }
        configureMacronButtons(macronButtons, answerField, !isMacronCaps);
    }

    /**
     * Displays a pop up notifying the player what the answer to the unknown question was.
     * @param questionAnswer The answer to the question.
     */
    public static void answerUnknown(String questionAnswer, String questionType) {
        StringBuilder contentText = new StringBuilder();
        contentText.append("That's alright, we all learn new things everyday.\n\n")
                .append("The correct answer was: ")
                .append(questionType.substring(0,1).toUpperCase()).append(questionType.substring(1)).append(" ")
                .append(questionAnswer.replaceAll("`", ""));

        // Revert currently reading speed to default, then say "Correct".
        TTSUtility.revertReadingSpeedToDefault();
        TTSUtility.speak("The correct answer was " + questionType + " " + questionAnswer);

        Notification.largeInformationPopup("Don't Know", "Don't know the question?", contentText.toString());
    }

    /**
     * User or actual answer input clean up method so that
     * the player's answer and the actual answer are comparable
     * even if the player used a different way of expressing the answer
     * @param answer
     * @return
     */
    public static String answerCleanUp(String answer) {
        // Removing a, the, an and changing mt to mount, nz to new zealand
        // Also trimming and lower casing the answer
        return answer.toLowerCase()
                .trim()
                .replace("`","")
                .replace("a ","")
                .replace("the ","")
                .replace("an ","")
                .replace("mt","mount")
                .replace("nz","new zealand");
    }

}
