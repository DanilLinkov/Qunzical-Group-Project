# Journal - Assignment 3

## 07/09/2020 - Hyung & Danil

- Had first meeting in Discord.
- Organized Initial version of team agreement form.
- Decided to use Trello as the program to keep a list of tasks to achieve.
- Created wiki folder and uploaded Team Agreement form and the project journal.

## 08/09/2020 - Hyung & Danil

- Designed UI for each possible scenes.
- Discussed on how the workload should be divided and which part each person should take. At the end, it wasn't fully decided but it has been decided that we will divide what FXML scenes to work on.
- Solved out GitHub repo for source codes and wiki folder. 

## 10/09/2020 - Hyung & Danil

- Discussed how to implement game saving and loading
  - Decided to create a Game manager which will be a singleton that will take care of saving, loading and containing the points and the question board
- Decided on how to implement question board generation and randomization and how it will be displayed in the games module
  - Decided to have the question board read the data base and pick 5 random category files and then pick 5 random lines from each file using a method discussed in the "Class Structure" document.
- Split up the work load to be done for the next meeting
  - Hyung : 
    - Implement question board component method
    - (possibly) Convert Questions Doc to files that will be read by the code
  - Danil :
    - Saving, loading game
    - Question board random generation
    
## 15/09/2020 - Hyung & Danil

- Explained our code and implementation for the previously set tasks
- Realized a complication with category formatting that will need to be addressed at a later date
- Discussed further work that needs to be done and came up with new work to split
  - Hyung :
    - Connect controllers
    - Button handling
  - Danil : 
    - Design two fxml views for asking the question
    - Merge previous work so that both our code fits together and works

## 22/09/2020 - Hyung & Danil

- Fixed saving and loading bugs
- Discussed how to implement the asking question part and decided to have separate fxml files one for games module and one for practice module
- Discussed further work to be done and split it up
  - Hyung : 
    - Implement games module question board to redirect to the question
  - Danil : 
    - Implement practice module categories selection and question randomization after selection

## 25/09/2020 - Hyung & Danil

- Showed and explained what we worked on and what we implemented
- Discussed on a new implementation for question bank storage
- Discussed how we want to approach macrons in tts
- Realized we need to add support for multiple answers
- Changed small details in question class such as changing the answer string to string array
- Discussed further work to be done and split it up
  - Hyung : 
    - Implement answer checking process for games module
    - Figure out the best way of approaching macrons
  - Danil : 
    - Merge code and change the small implementation details
    - Implement answer checking for practice module