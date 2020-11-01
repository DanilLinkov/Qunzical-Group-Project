# Welcome to Quinzical!

### Recommended System Requirements
- A recommend system requirements for a most fluent experience of Quinzical is dual core with 4 GB of ram, 
though the program should run fine in systems that are under the recommended system requirements.
- A screen with a resolution of at least 1024x768 is required.

### How to execute game
- Place play.sh and a folder called "categories" which contains category files with .txt format in the identical 
directory as Quinzical.jar file.
- In terminal, enter `java -jar Quinzical.jar` (basically run the jar file in terminal), or you can also run play.sh in terminal by entering `./play.sh` which will do an identical thing of running the jar file.

### Adding new categories
- Addition of new categories is possible by adding new text files that follow a similar style of text.
- The format to follow is: `Clue text blah blah|what is|answer` or `Clue text blah blah|what is|answer1/answer2`.
- **WARNING**: Quinzical is currently only supported to have two types of categories; NZ and international. Hence current folder
structure for category files is **NECESSARY**. DO NOT modify directory structure in any form, and simply add or remove
files inside current folder structure. 
- **WARNING**: Make sure when changes to categories are made, either reset the game **IN PRIOR** to making any changes in categories,
or simply remove save/save.txt after changing categories to prevent any incompatibilities with previously played game and
a new game with new categories.

### Main menu

- Games Module
  - Takes you to the screen to play the main Quinzical game
- Practice Module
  - Allows for the player to practice the questions

### Games Module

- Play game
  - Start a new game/load old game and get taken to the question board
  - Select the lowest valued question in one of the 5 random categories
  - Click the play button to replay the question
  - Type in the answer and submit

- Reset game
  - Reset the existing question board and get a new one
- Return to Main Menu

### Practice Module

- Select any category from the drop down
- Click select or Back to Main Menu
- Attempt the question up to 3 times or click Don't know

