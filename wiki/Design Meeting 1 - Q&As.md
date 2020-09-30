# Design Meeting 1 - Q&As
#softeng206

- What if festival doesn’t work?
	- A: Festival works *best* with `festival —tts txtFile.txt` or other relevant commands, not `echo “hello world” | festival —tts`.

- Can we use espeak?
	- A: Catherine will only provide resources for festival so we would have to deal with maori words by ourselves if we want to use espeak.

- Will we be marked down for achieving Ass. 4/project requirements/features?
	- A: No, all they want is just a software.

- Is the text input the only way of the inputting the answer?
	- They thought typing in the answer is a better; a more “suitable” option since they wanted the users of the program to learn rather unfamiliar Māori vocabularies.

**ALWAYS REMEMBER: DON’T DO MORE WORK THAN YOU NEED TO**

- Can we change the wordings of the question bank depending on the user?
	- Sounds like a nice thing to have, 

* Is it a requirement to use NZ voices included?
	* No, it’s a supplementary.

- What is the most important factor in our program?
	- Firstly, it has to “work”.

- User opens a question, but goes back to a selectQuestion or closes a program.  How do we handle this as?
	- It is safe to assume that we treat this as incorrect.

- In terms of design aesthetics, do you have any colour preferences that should be designed inside the game?
	- A - Catherine: Colors that represents NZ? since it’s suitable to the whole theme of the program?
	- A - Nasser: HOT PINK (jks, but if you are going to stick with one colour scheme and doesn’t really support colour customisation, probably stick to colours that represent NZ.)

- How should the answer be formatted?
	- A: One of the examples that Catherine used was:
		- Answer “Who is Jacinda Ardern” as the whole thing
		- have “Who is” right next to the answer box and make the user only type in “Jacinda Ardern”
		- Completely remove “who is” and only use “Jacinda Ardern”

**The whole purpose of provided question bank was to reduce our workload.**

- For practice module, do we make sure that the questions that have already been answered in the CURRENT SESSION of game (meaning, while it has been opened) are not asked again? or do we simply allow random batch of question even though it might produce three identical questions in a row?
	- A: Doesn’t matter

- Is the answer without macron wrong?
	- A: Nasser and Catherine will discuss about this issue and catch up with us later.

- Should the user be allowed to reset the game during the game?
	- A: It’s up to a design decision. But think about the user experience, user usage, and necessity about it. When/Why/Would the user need to reset the game?

**BE GENTLE with how you treat the users; some might take even the smallest thing such as “You got it wrong” as offensive. Some people quite take it personally when they get it wrong.**

- Project: Where do we get the international question bank from?
	- Utilize services that provide jeopardy questions that are not necessarily limited to NZ topics.

- When the user exits a program, do you want the game to be saved?
	- A: The features that give marks are things that they’ve asked to see. Since “saving before exiting” has not been given as a requirement, we are more than welcomed to leave that out as a feature with a less of a priority if it gets in the way of implementing another feature that is rather required.

- Should the user be allowed to go back and forth the games module and the practice module? Cause the user might go to the games module, see the question then jump to the  practice module, check the answer, then go back to the games module to answer the question.
	- A - Nasser: “Is that what you guys have done at 24 hours exam in sem 1?”
	- A - Nasser: “Jks, allow them to do it. Don’t let that be an obstacle in the things that you (us, developers) have to do. Let them do if they want to do so. After all, it’s a program that’s intending to make them learn more, not necessarily thoroughly testing them.”

- What if the user enters an input without a macron when the answer has it?
	- A: Again, we “should” be implementing the macrons in terms of respecting the language that we use, but we also have other considerations such as “how difficult is it to develop as a developer” and “how easy is it for the user to use/type it”. 
	- *The entire class including Nasser and Catherine should be coming up with the solution about this issue.*

- Would it be possible to include virtual keyboard that will allow the user to input macrons?
	- A: That’s what happens in Language Perfect. 
	- A: That’s a nice idea and a nice suggestion. Will consider that.

## Matt’s small group questions
- Do we arbitrarily assign values to questions?
- incorrect answer -> decrease in value or stay same?
- In Practice mode, is it totally random? or do we filter out questions already asked?
- Are Nasser and Catherine normal adult clients? or university students or etc…
- should there be reset Button?
- do you want an option to show question text?