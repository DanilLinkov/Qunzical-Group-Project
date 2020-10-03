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

public class QuestionBoard {

    private final int _numQuestions = 5;
    private final int _numCategories = 5;
    private ArrayList<Category> _categoriesList = new ArrayList<>();

    public GridPane initializeBoard() {
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

        return questionBoardComponent;
    }

    /**
     * This method is used to generate a brand new question board with 5 random categories
     * each having 5 random questions
     */
    public void createBoard() {
        // Getting the string path to the categories folder outside the application
        String categoriesPath = new File("").getAbsolutePath();
        categoriesPath+="/categories";

        // Creating a folder file given that path
        File categoriesFolder = new File(categoriesPath);

        // Getting all the categories (txt files) from that folder into an array list of paths
        ArrayList<String> filePaths = new ArrayList(Arrays.asList(categoriesFolder.list()));

        // Generating a random index list of size 5 where the range is based on the number of categories
        ArrayList<Integer> randomCategoryIndexList = randomIndexArray(filePaths.size());

        // Outer loop supposed to represent the 5 random categories selected
        for (int i = 0; i < 5; i++) {
            // Getting the random category index from the index list
            int randomCategoryIndex = randomCategoryIndexList.get(i);
            // Creating the new category with the file name minus the .txt at the end
            Category newCategory = new Category(filePaths.get(randomCategoryIndex).replace(".txt","")); // parent

            try {
                // Getting all the lines in that category as a list of strings
                List<String> allLines = Files.readAllLines(Paths.get(categoriesPath+"/"+filePaths.get(randomCategoryIndex)));

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

                    // Pre-processing the string
                    line.replaceAll("\\s+","");

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
                    newQuestion.set_whatIs(whatIs);

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

    public GridPane getQuestionBoard() {
        // generate Pressable Question board based on which questions need to be emptied out or pressable or disabled.

        GridPane questionBoardComponent = initializeBoard();

        Button pointButton;
        for (int categoryIndex = 0; categoryIndex < _numCategories; categoryIndex++) {
            int lowestValuedQuestionIndex = _categoriesList.get(categoryIndex).getLowestValuedQuestionIndex();
            for (int questionIndex = 0; questionIndex < _numQuestions; questionIndex++) {
                if (questionIndex < lowestValuedQuestionIndex) {
                    // Advance to the next question and make the cell empty.
                    continue;
                } else {
                    pointButton = createPointButton(categoryIndex, questionIndex);
                    // Make the button fill in each cell.
                    pointButton.setMaxWidth(Integer.MAX_VALUE);
                    pointButton.setMaxHeight(Integer.MAX_VALUE);
                    // Allocate a button to the corresponding cell.
                    GridPane.setConstraints(pointButton, categoryIndex, questionIndex+1);

                    if (questionIndex > lowestValuedQuestionIndex) {  // Create unclickable buttons
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

    private Button createPointButton(int categoryIndex, int questionIndex) {
        Button button = new Button(Integer.toString((questionIndex+1)*100));
        button.setStyle("-fx-font-size:18px;");
        button.setPadding(new Insets(15, 0, 15, 0));
        button.setOnAction(e ->
                SelectQuestionController.getInstance().handlePointButtonAction(categoryIndex, questionIndex)
        );
        return button;
    }

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

    public int getSize()
    {
        return _categoriesList.size();
    }

    public void addCategory(Category category){
        _categoriesList.add(category);
    }

    public Category getCategory(int index){
        return _categoriesList.get(index);
    }

}
