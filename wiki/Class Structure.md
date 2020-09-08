# List of classes

- ## Main.java

- ## QuestionBoard.java

```java
ArrayList<Category> categoriesList;

class Category {
	ArrayList<Question> answeredQuestions;
}

class Question {
//    ... more methods ...
    boolean checkIdenticalQuestion(Question anotherQuestion) {
        if (getQuestionText().equals(anotherQuestion.getQuestionText())) and 
            (getAnswerText().equals(anotherQuestion.getAnswerText())) {
            return true;
        }
        return false;
    }
    
    // Please make an alert box for this position
    // Text: "please select jsklfjkdsfkajl"
    // OK Button
}
```

- ## AskQuestion.java





# How to split work?

- AskQuestion part (Both practice and game)
- Category Select part (Both practice: drop-down menu? and game: question board)
- Main Menu part ("main" Main Menu and "game modules" Main Menu)
- Load-and-save part (Overall loading and saving)
- (Maybe) Pop-ups overall?



Task List

Make views