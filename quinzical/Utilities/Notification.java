package quinzical.Utilities;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

public class Notification {

    public static void smallPopup(String title, String headerText, String contentText, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);

        // Formats texts inside the pop up.
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        alert.showAndWait();
    }

    public static void largePopup(String title, String headerText, String contentText, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);

        // Formats texts inside the pop up.
        alert.setTitle(title);
        alert.setHeaderText(headerText);

        // Formats the pop-up.
        alert.getDialogPane().setContent(new Label(contentText));
        alert.getDialogPane().setMinWidth(alert.getDialogPane().getWidth());
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }
}
