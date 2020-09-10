package quinzical.MainMenu;

import javafx.application.Application;
import javafx.stage.Stage;
import quinzical.GamesModule.GameManager;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    // Initial Program.
    @Override
    public void start(Stage stage) throws Exception {
        GameManager gameManager = new GameManager();
        stage.show();
    }
}
