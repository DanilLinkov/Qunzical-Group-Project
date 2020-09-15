package quinzical.MainMenu;

import javafx.application.Application;
import javafx.stage.Stage;
import quinzical.GamesModule.GameManager;
import quinzical.GamesModule.QuestionBoard;

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
