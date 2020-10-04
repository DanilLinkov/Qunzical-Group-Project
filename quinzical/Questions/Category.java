package quinzical.Questions;

import java.util.ArrayList;

/**
 * This class is used to represent everything about a category
 * which is stored in a question board.
 *
 * Authors: Hyung Park, Danil Linkov
 */
public class Category {

    // Array list used to store the questions belonging to this category that have
    // been randomly selected
    private ArrayList<Question> _questions = new ArrayList<>();
    // Shows the current index of the first un answered question
    private int _lowestValuedQuestionIndex = 0;

    // Category name
    private String _categoryName;

    public Category(String categoryName) {
        _categoryName = categoryName;
    }

    public String toString() {
        return _categoryName;
    }

    // Standard getters, setters and Array list operations

    public void advanceLowestValuedQuestionIndex() {
        _lowestValuedQuestionIndex++;
    }

    public int getLowestValuedQuestionIndex() {
        return _lowestValuedQuestionIndex;
    }

    public void addQuestion(Question question){
        _questions.add(question);
    }

    public Question getQuestion(int index){
        return _questions.get(index);
    }

    public void setLowestValuedQuestionIndex(int lowestValuedQuestionIndex) {
        _lowestValuedQuestionIndex = lowestValuedQuestionIndex;
    }

}
