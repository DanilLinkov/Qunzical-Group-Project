package quinzical.GamesModule;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import javafx.scene.layout.RowConstraints;
import quinzical.GamesModule.SelectQuestion.SelectQuestionController;
import quinzical.Questions.Category;
import quinzical.Questions.Question;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A question board used in games module.
 * <p></p>
 * A class which stores question board information and
 * returns a component with the information in it.
 *
 * @author Danil Linkov, Hyung Park
 */
public class QuestionBoard {

    // Constants in question board.
    private final int _numQuestions = 5;
    private final int _numCategories = 5;

    // List of categories in the question board.
    private final ArrayList<Category> _categoriesList = new ArrayList<>();
    private boolean initialized = false;

    private GameType _gameType;

    public QuestionBoard(GameType gameType) {
        _gameType = gameType;
    }

    /**
     * This method is used to generate a brand new question board with 5 random categories
     * each having 5 random questions
     */
    public void createBoard(ArrayList<String> selectedCategories) {
        initialized = true;
        // Getting the string path to the categories folder outside the application
        String categoriesPath = new File("").getAbsolutePath();
        categoriesPath += _gameType == GameType.NZ ? "/categories/NZ" : "/categories/international";

        // Creating a folder file given that path
        File categoriesFolder = new File(categoriesPath);

        // Getting all the categories (txt files) from that folder into an array list of paths
        ArrayList<String> filePaths = new ArrayList<>(Arrays.asList(categoriesFolder.list()));

        // Generating a random index list of size 5 where the range is based on the number of categories
        // ArrayList<Integer> randomCategoryIndexList = randomIndexArray(filePaths.size());

        // Outer loop supposed to represent the 5 random categories selected
        for (int i = 0; i < 5; i++) {
            // Getting the random category index from the index list
            // int randomCategoryIndex = randomCategoryIndexList.get(i);
            // Creating the new category with the file name minus the .txt at the end
            Category newCategory = new Category(selectedCategories.get(i)); // parent

            try {
                // Getting all the lines in that category as a list of strings
                List<String> allLines = Files.readAllLines(Paths.get(categoriesPath+"/"+selectedCategories.get(i) + ".txt"));

                // Generating a random index list of size 5 where the range is based on the number of questions/lines in that categories file
                ArrayList<Integer> randomQuestionIndexList = randomIndexArray(allLines.size());

                // Inner loop supposed to represent the 5 random questions selected
                for (int j = 0; j < 5; j++) {
                    // Getting the random question index from the index list
                    int randomLineIndex = randomQuestionIndexList.get(j);
                    // Getting the question line based on that index
                    String line = allLines.get(randomLineIndex);

                    String question;
                    String answer;
                    String whatIs;

                    // Splitting it based on | character
                    List<String> questionSplit = Arrays.asList(line.split("\\s*\\|\\s*"));

                    // Setting the question answer and question type
                    question = questionSplit.get(0);
                    whatIs = questionSplit.get(1);
                    answer = questionSplit.get(2);

                    // Splitting the answer into an answer[] since there could be multiple answers
                    String[] answerSplit = answer.split("/");

                    // Creating the new question object
                    Question newQuestion = new Question(question,answerSplit,newCategory,(j+1)*100);
                    newQuestion.setLineNumber(randomLineIndex);
                    newQuestion.setQuestionType(whatIs);

                    // Adding it to the new category
                    newCategory.addQuestion(newQuestion);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Adding the category to the question board
            _categoriesList.add(newCategory);
        }
    }

    public boolean isQuestionBoardCreated() {
        return _categoriesList != null && _categoriesList.size() == 5;
    }

    /**
     * This is a private utility method for this class which returns an array of size 5
     * containing random index values from 0 to the length-1 specific
     * @param length of the range of the index values
     * @return
     */
    private ArrayList<Integer> randomIndexArray (int length) {
        ArrayList<Integer> shuffledArray = new ArrayList<>();

        for(int i = 0; i< length;i++) {
            shuffledArray.add(i);
        }

        // Shuffling the array list and returning it
        Collections.shuffle(shuffledArray);

        return shuffledArray;
    }

    /**
     * Creates a GridPane that contains the question board with buttons in it.
     * @return A GridPane component of the question board.
     */
    public GridPane getQuestionBoard() {
        // Generates and formats overall question board.
        GridPane questionBoardComponent = new GridPane();
        questionBoardComponent.setGridLinesVisible(true);
        questionBoardComponent.setStyle("-fx-background-color:#FFFFFF");

        // Pre-initializes variables to be frequently re-defined.
        Label categoryLabel;
        int categoryIndex = 0;
        // Add categories to the top bar of question board
        for (Category category : _categoriesList) {
            // Extract category name, make it into upper case, and store it in the list of labels of categories.
            categoryLabel = new Label(category.toString().toUpperCase());
            // Format each cell of category
            categoryLabel.setPadding(new Insets(10, 5, 10, 5));
            // Allocate a category to the corresponding cell.
            GridPane.setConstraints(categoryLabel, categoryIndex++, 0);
            // Adds the category label in the question board.
            questionBoardComponent.getChildren().add(categoryLabel);
        }

        Button pointButton;
        for (categoryIndex = 0; categoryIndex < _numCategories; categoryIndex++) {
            int lowestValuedQuestionIndex = _categoriesList.get(categoryIndex).getLowestValuedQuestionIndex();
            for (int questionIndex = 0; questionIndex < _numQuestions; questionIndex++) {
                // Only create buttons for questions that are not yet answered.
                if (questionIndex >= lowestValuedQuestionIndex) {
                    pointButton = createPointButton(categoryIndex, questionIndex);
                    // Make the button fill in each cell.
                    pointButton.setMaxWidth(Integer.MAX_VALUE);
                    pointButton.setMaxHeight(Integer.MAX_VALUE);
                    // Allocate a button to the corresponding cell.
                    GridPane.setConstraints(pointButton, categoryIndex, questionIndex+1);

                    // If a question is not the lowest valued question,
                    if (questionIndex > lowestValuedQuestionIndex) {
                        pointButton.setDisable(true);
                    }

                    // Add the button in the question board.
                    questionBoardComponent.getChildren().add(pointButton);
                }
            }
        }

        evenlySpreadOut(questionBoardComponent);
        return questionBoardComponent;
    }

    /**
     * Creates a button that has the point of a given question as a text.
     * <p></p>
     * When the button is pressed, it handles the event by invoking a method in
     * SelectQuestionController: handlePointButtonAction().
     * @param categoryIndex The index of the category of the question in the list of categories.
     * @param questionIndex The index of the question in its categories.
     * @return A button that has a value of the given question as its text.
     */
    private Button createPointButton(int categoryIndex, int questionIndex) {
        Button button = new Button(Integer.toString((questionIndex+1)*100));
        button.setStyle("-fx-font-size:18px;");
        button.setPadding(new Insets(15, 0, 15, 0));
        button.setOnAction(e ->
                SelectQuestionController.getInstance().handlePointButtonAction(categoryIndex, questionIndex)
        );
        return button;
    }

    /**
     * Formats the overall GridPane in order to evenly spread out each cells both horizontally and vertically.
     * It also formats every cell to be center aligned vertically and horizontally.
     * @param questionBoard The question board to evenly spread out every cell.
     */
    private void evenlySpreadOut(GridPane questionBoard) {
        // Format each rows to be center aligned and have identical height.
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setValignment(VPos.CENTER);
        rowConstraints.setPercentHeight(100d / _numQuestions);
        for (int i = 0; i <= _numQuestions; i++) {
            questionBoard.getRowConstraints().add(rowConstraints);
        }

        // Format each columns to be center aligned and have identical width.
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHalignment(HPos.CENTER);
        columnConstraints.setPercentWidth(100d / (_numCategories));
        for (int i = 0; i < _numCategories; i++) {
            questionBoard.getColumnConstraints().add(columnConstraints);
        }
    }

    /**
     * Returns the number of categories in a question board, which is 5.
     * @return the number of categories in a question board; 5.
     */
    public int getNumCategories() {
        return _numCategories;
    }

    /**
     * Returns the number of questions in each category in a question board, which is 5.
     * @return the number of questions in each category in a question board.
     */
    public int getNumQuestions() {
        return _numQuestions;
    }

    /**
     * Adds a given category to the list of categories in the question board.
     * @param category A category to add to the list of categories of this question board.
     */
    public void addCategory(Category category) {
        _categoriesList.add(category);
    }

    /**
     * Returns a category at given index in the list of categories of this question board.
     * @param index The index of the category in the list of categories of this question board.
     * @return The category at the given index.
     */
    public Category getCategory(int index) {
        return _categoriesList.get(index);
    }

    public boolean isInitialized() {
        return initialized;
    }
}
