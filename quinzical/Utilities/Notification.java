package quinzical.Utilities;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

import java.util.Optional;

public class Notification {

    public static void smallInformationPopup(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        // Formats texts inside the pop up.
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        alert.showAndWait();
    }

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

    public static boolean confirmationPopup(String title, String headerText, String contentText) {
        Alert confirmReset = new Alert(Alert.AlertType.CONFIRMATION);

        // Formats texts inside the pop up.
        confirmReset.setTitle(title);
        confirmReset.setHeaderText(headerText);
        confirmReset.setContentText(contentText);

        // Waits for the button result
        Optional<ButtonType> result = confirmReset.showAndWait();
        // Return true if button pressed from the pop up was OK button.
        return result.get() == ButtonType.OK;
    }
}
