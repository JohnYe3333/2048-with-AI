# 2048 with AI

This is a Java implementation of the popular game 2048, with a Swing GUI and an AI component included.

The AI uses **Monte Carlo simulation** to play the game. The AI first **pick a starting move** (move up, down, left, or right), and then plays the game **using random moves** until the game ends. For each starting move, the AI plays a equal number of such random games. After each random game ends, the AI calculates how many extra points have been gained since the beginning of simulation, and add the number of extra points to the corresponding starting move's score. **The starting move with the highest score is chosen as the next move.**

The random games that the AI plays is, unsurprisingly, pretty bad, usually yielding only a couple hundred extra points. The AI only chooses the *"least bad"* starting move. However, the AI plays quite well, achieving the 2048 tile almost every time and sometimes achieving the 4096 tile. It's very interesting that the AI is able to choose the moves that achieve good results without actually foreseeing the good results.

I also provided a benchmark tool for you to test the AI's performance. You can specify the number of simulation runs per starting move or a range of such numbers. (Generally, the more simulation runs (random games) per starting move, the better the AI's performance. **The increase in performance, however, diminishes fast as the number increases.**) You can also specify the number of repeated runs for each different AI strength to get more accurate results.

For more information on AI strategies for 2048, visit this link: [http://stackoverflow.com/questions/22342854/what-is-the-optimal-algorithm-for-the-game-2048](http://stackoverflow.com/questions/22342854/what-is-the-optimal-algorithm-for-the-game-2048 "2048 AI on stackoverflow"). Many AI strategies (including a Monte Carlo AI the same as mine) for this game are discussed there.