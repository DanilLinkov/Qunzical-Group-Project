package quinzical.Questions;

public class Category {

    private Question[] _questions = new Question[5];

    public Category() {
        // Decide whether to randomly select a question whenever playing with a new question, -> No need for _questions
        // or preset a list of questions to use. -> Need for _questions
    }

}
