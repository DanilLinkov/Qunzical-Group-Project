package quinzical.Questions;

import java.util.ArrayList;

public class Category {

    private ArrayList<Question> _questions = new ArrayList<>();
    private int _lowestValuedQuestionIndex = 0;

    private String _categoryName;

    public Category(String categoryName) {
        _categoryName = categoryName;
    }

    public String toString() {
        return _categoryName;
    }

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
