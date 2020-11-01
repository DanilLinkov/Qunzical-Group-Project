package quinzical.Utilities;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

import java.util.Optional;

/**
 * This class is a utility class which can be used to send simple notifications to the users in a form
 * of pop ups.
 *
 * @author Hyung Park
 */
public class Notification {

    /**
     * Creates a small information pop up notifying the user on an information.
     * @param title The title of the pop up window.
     * @param headerText The header text of the pop up window.
     * @param contentText The content text of the pop up window.
     */
    public static void smallInformationPopup(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        // Formats texts inside the pop up.
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        alert.showAndWait();
    }

    /**
     * Creates a large information pop up notifying the user on an information.
     * @param title The title of the pop up window.
     * @param headerText The header text of the pop up window.
     * @param contentText The content text of the pop up window.
     */
    public static void largeInformationPopup(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        // Formats texts inside the pop up.
        alert.setTitle(title);
        alert.setHeaderText(headerText);

        // Formats the pop-up.
        alert.getDialogPane().setContent(new Label(contentText));
        alert.getDialogPane().setMinWidth(alert.getDialogPane().getWidth());
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    /**
     * Creates a confirmation pop up which asks user for a confirmation of action.
     * @param title The title of the pop up window.
     * @param headerText The header text of the pop up window.
     * @param contentText The content text of the pop up window.
     * @return A boolean value that is true if the user has pressed "OK" button.
     */
    public static boolean confirmationPopup(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        // Formats texts inside the pop up.
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        // Waits for the button result
        Optional<ButtonType> result = alert.showAndWait();
        // Return true if button pressed from the pop up was OK button.
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}
