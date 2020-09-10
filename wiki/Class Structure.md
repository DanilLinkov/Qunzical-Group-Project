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



We would make a java.io.File objects of every category file in categories folder, then store them in `List<File>` and generate a random index within that range, and use that file to create a Category object. Then, that file object is removed from `List<File>` and a new random index within a new range is selected to use a new random category file.

Selecting a random question works the same. Make a list of every Questions in a category file, select a random one, store that in the list of questions to ask (max 5) then remove that from the list of every questions, and then repeat this until all five random questions have been chosen.





- ## Save File Structure

400,12600

category_name1,2,3,6,1,4,2

category_name1,2,3,6,1,4,2

category_name1,2,3,6,1,4,2



- ## AskQuestion.java









# How to split work?

- AskQuestion part (Both practice and game)
- Category Select part (Both practice: drop-down menu? and game: question board)
- Main Menu part ("main" Main Menu and "game modules" Main Menu)
- Load-and-save part (Overall loading and saving)
- (Maybe) Pop-ups overall?



Task List

Make views