package quinzical.Questions;

import java.util.ArrayList;

public class Category {

    private ArrayList<Question> _questions = new ArrayList<>();
    private String name;
    private int lowestValuedQuestionIndex = 0;

    private String _categoryName;

    public Category(String categoryName) {
        _categoryName = categoryName;
    }

    public String toString() {
        return _categoryName;
    }

    public void advanceLowestValuedQuestionIndex() {
        lowestValuedQuestionIndex++;
    }

    public int getLowestValuedQuestionIndex() {
        return lowestValuedQuestionIndex;
    }

    public void addQuestion(Question question){
        _questions.add(question);
    }

    public Question getQuestion(int index){
        return _questions.get(index);
    }

    public Question getQuestion(String clue){

        for (Question q : _questions){
            if (q.get_clue().equals(clue)){
                return q;
            }
        }

        return null;
    }

    public int getSize()
    {
        return _questions.size();
    }

    public void setLowestValuedQuestionIndex(int lowestValuedQuestionIndex) {
        this.lowestValuedQuestionIndex = lowestValuedQuestionIndex;
    }

}
