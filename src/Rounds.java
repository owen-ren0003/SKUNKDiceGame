/*
 * Copyright (c) 2019, Owen Ren. All rights reserved.
 *
 */

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * This class takes the players through the rounds of skunk. The heart of the program.
 *
 * @author Owen Ren
 * @version 1.0
 * @script July 9, 2019
 */
public class Rounds {

    private boolean allStanding, allSitting, roundEnded;
    private Random r;
    private int[] scoresCurrentRound;

    /* Symbolic constants used to get rid of magic numbers */
    public static final int NUM_SKUNK_ROUNDS = 5;
    public static final int STARTING_TURN    = 0;
    public static final int STARTING_ROUND   = 0;

    public static final int WAITING_TIME_SEC           = 5;
    public static final int THREAD_SLEEP_TIME_MILLISEC = 400;

    public static final int FILL_ONE_EMPTY_SPACE = 1;
    public static final char EMPTY_CHAR          = ' ';
    public static final char VERTICAL_CHAR       = '|';
    public static final String EMPTY_STRING      = "";

    public static final int DICE_ROLLS_ONE1   = -1;
    public static final int DICE_ROLLS_TWO1   = 0;
    public static final int DICE_ROLLS_THREE1 = 100;

    public static final int ASTERIX_STRING_LENGTH = 32;
    public static final int PARITY_CHECKER        = 2;
    public static final int PARITY_MOD_CHECKER    = 0;

    public static final String[] ROUNDS = {"S----", "-K---", "--U--", "---N-", "----K"};

    /**
     * The constructor runs through one iteration of the game.
     *
     * Summary of actions:
     *
     * 1. initializes the random instance variables used to roll dice.
     * 2. prints the initialization screen of the game
     *
     * For each round iteration:
     *
     *      1. set turn to STARTING_TURN;
     *      2. resets player standing and set starting score for new round
     *      3. set starting score for current round
     *      4. initialize allStanding to true, allSitting to true, and roundEnded to false
     *
     *      For each turn iteration:
     *
     *          1. ask all human players if they want to sit and record
     *          2. generates sitting decisions for all AI players
     *          3. print those players (AI and Human) who are standing
     *          4. update allSitting and allStanding variables
     *          5. roll dice
     *          6. translate the dice to a single integer value:
     *             DICE_ROLLS_ONE1, DICE_ROLLS_TWO1 or a positive integer.
     *          7. increment turn and updates the current points for all players.
     *          8. prints the current turn stats summary.
     *          9. checks if round has ended to break out of the loop.
     *
     *      5. prints the round summary
     *
     * 3. prints the winner in console
     * 4. resets player scores
     *
     * @param bf buffered reader to read the user input.
     */
    public Rounds(BufferedReader bf) throws InterruptedException{
        r = new Random();

        /* Prints the starting initialization menu */
        startSummary();

        for (int round = STARTING_ROUND; round < NUM_SKUNK_ROUNDS; round++) {

            roundStart(round);

            /* Initializes the variables for each round */
            int turn = STARTING_TURN;
            setStartingScoreForCurrentRound();
            Player.resetAllStanding();
            roundEnded = false;
            allStanding = true;
            allSitting = true;

            while (!roundEnded) {
                doYouWantToSit(bf);

                // updates AI and Human Player's decisions as well as allStanding/allSitting variables.
                waiting("Waiting for AI to make decisions", WAITING_TIME_SEC);
                if (!(turn == STARTING_TURN && round == STARTING_ROUND)) {
                    AISitDecision();
                }
                printStanding();
                updateAllSitAndStand();

                /* rolls the dice */
                int[] curRoll = Dice.rollDice();
                int curPoints = getCurrentPoint(curRoll);

                /* updates the points for the standing players */
                updatePoints(curPoints);

                /* increments turn */
                turn++;

                /* prints the summary results for the current turn*/
                turnSummaryTable(turn);

                /* breaks out of the while loop if roundEnded is true*/
                if (roundEnded) {
                    System.out.print("\nROUND " + ROUNDS[round] + " ENDED.\n\n\n");
                }
            }

            //Round summary:
            System.out.printf("\nSUMMARY OF ROUND %s:\n", ROUNDS[round]);
            roundSummaryTable(round);
        }
        /* calculates and prints the winner */
        printWinner();

        /* reset the score for next round */
        Player.resetScores();
    }

    /**
     * Prints the starting game summary
     */
    private void startSummary() {
        System.out.print("\n\n--------------------------------------------------------------\n" +
                "\t\t\t\tSTARTING SKUNK GAME:\n" +
                "--------------------------------------------------------------\n\n");
        /* creates a pause in game with animation and a message */
        waiting("Initializing interface...", WAITING_TIME_SEC);
    }

    /**
     * Prints starting round round
     *
     * @param round the current round
     */
    private void roundStart(int round) {
        System.out.printf("\n\n--------------------------------------------------------------\n" +
                "\t\t\t\tStarting Round %s\n" +
                "--------------------------------------------------------------\n\n", ROUNDS[round]);
    }

    /**
     * Used to print tables
     *
     * @param length the length of the returned string
     * @param c the character to be filled
     * @return a string of length length with all characters c
     */
    private String fillString(int length, char c) {
        StringBuilder s = new StringBuilder();
        for (int curChar = Player.STARTING_INDEX; curChar < length; curChar++){
            s.append(c);
        }
        return s.toString();
    }

    /**
     * generates the rows given String to match the column size. We fill the col string with spaces so it is
     * the same length as the String ent.
     *
     * @param ent a string
     * @param col a string to be compared to
     * @return a string of length col, that has ent right in the middle and left right side populated with spaces.
     */
    private String generateColumn(String ent, String col) {
        String ret = EMPTY_STRING;
        if (ent.length() < col.length()) {
            ret += fillString(FILL_ONE_EMPTY_SPACE, EMPTY_CHAR) + ent +
                    fillString(col.length() - ent.length() + Player.ONE_INCREMENT, EMPTY_CHAR) + VERTICAL_CHAR;
        } else {
            ret += fillString(FILL_ONE_EMPTY_SPACE, EMPTY_CHAR) + ent.substring(Player.STARTING_INDEX, col.length()) +
                    fillString(FILL_ONE_EMPTY_SPACE, EMPTY_CHAR) + VERTICAL_CHAR;
        }
        return ret;
    }

    /**
     * prints a table summary of the current turn
     *
     * @param turn the current turn
     * @throws InterruptedException if the thread gets interrupted
     */
    private void turnSummaryTable(int turn) throws InterruptedException{
        System.out.printf("\n--------------------------------------------------------------\n" +
                "\t\t\t\tTURN %d SUMMARY:\n" +
                "--------------------------------------------------------------\n\n", turn);

        /* column titles */
        String c1 = "Name of Player Team", c2 = "Score this Round", c3 = "Total Score", c4 = "Standing?";
        System.out.printf("\n %s | %s | %s | %s \n", c1, c2, c3, c4);

        /* gets the number of players */
        int numRows = Player.getNumHuman() + Player.getNumAI();

        String[] rowsString = new String[numRows];
        Arrays.fill(rowsString, EMPTY_STRING);

        Thread.sleep(THREAD_SLEEP_TIME_MILLISEC);

        /* displays the player stats one at a time with delay */
        for (int index = Player.STARTING_INDEX; index < numRows; index ++) {

            //gets the name
            String name = (index < Player.getNumHuman()) ? Player.getHumanNames()[index] :
                    Player.getTheAINames()[index - Player.getNumHuman()];
            rowsString[index] += generateColumn(name, c1);

            //gets the score this round
            String scoreRound = Integer.toString(scoresCurrentRound[index]);
            rowsString[index] += generateColumn(scoreRound, c2);

            //gets the total score
            String totalScore = Integer.toString(Player.getScores()[index]);
            rowsString[index] += generateColumn(totalScore, c3);

            //gets if the current player is standing
            String standing = Player.getStanding()[index] ? "Yes" : "No";
            rowsString[index] += generateColumn(standing, c4);

            System.out.print("---------------------+------------------+-------------+-----------\n");
            System.out.print(rowsString[index] + "\n");

            Thread.sleep(THREAD_SLEEP_TIME_MILLISEC);
        }

        Thread.sleep(THREAD_SLEEP_TIME_MILLISEC);
        System.out.printf("\nEND OF TURN %d\n\n", turn);
    }

    /**
     * prints the stats summary for the end of a round
     *
     * @param round the current round
     * @throws InterruptedException if the thread gets interrupted
     */
    private void roundSummaryTable(int round) throws InterruptedException {
        System.out.printf("\n--------------------------------------------------------------\n" +
                "\t\t\t\tROUND %d SUMMARY:\n" +
                "--------------------------------------------------------------\n\n", round + Player.ONE_INCREMENT);

        /* column titles */
        String c1 = "Name of Player Team", c2 = "Total Score so Far";
        System.out.printf("\n %s | %s \n", c1, c2);

        Thread.sleep(THREAD_SLEEP_TIME_MILLISEC);

        int numRows = Player.getNumHuman() + Player.getNumAI();

        String[] rowsString = new String[numRows];
        Arrays.fill(rowsString, EMPTY_STRING);

        /* displays the player stats one at a time with delay */
        for (int index = Player.STARTING_INDEX; index < numRows; index ++) {
            String name = (index < Player.getNumHuman()) ? Player.getHumanNames()[index] :
                    Player.getTheAINames()[index - Player.getNumHuman()];
            rowsString[index] += generateColumn(name, c1);

            String totalScore = Integer.toString(Player.getScores()[index]);
            rowsString[index] += generateColumn(totalScore, c2);

            System.out.print("---------------------+--------------------\n");
            System.out.print(rowsString[index] + "\n");

            Thread.sleep(THREAD_SLEEP_TIME_MILLISEC);
        }
        Thread.sleep(THREAD_SLEEP_TIME_MILLISEC);
    }

    /**
     * prints those that are still standing separated by a space.
     */
    private void printStanding() {
        System.out.print("\n\nAfter deciding, these are the players still standing:\n");
        for (int index = Player.STARTING_INDEX; index < Player.getScores().length; index++){
            if (Player.getStanding()[index]) {
                String player = (index < Player.getNumHuman()) ? Player.getHumanNames()[index] :
                        Player.getTheAINames()[index - Player.getNumHuman()];
                System.out.print( player + " ");
            }
        }
        System.out.println("\n");
    }

    /**
     * initializes the starting score to scoresCurrentRound array for current round
     */
    private void setStartingScoreForCurrentRound() {
        scoresCurrentRound = new int[Player.getNumHuman() + Player.getNumAI()];
    }

    /**
     * prompts the current human player if he/she wants to sit and records it. Player must enter yes
     * or no (ignoring case). Helper function.
     *
     * @param bf to read the current player inputs
     * @param index the current index of the human player
     */
    private void promptUserToSit(BufferedReader bf, int index) {
        System.out.printf("\nDo you Player %s (# %d) want to sit? ", Player.getHumanNames()[index], (index + 1));
        while (true) {
            try {
                String ans = bf.readLine();
                if (!ans.equalsIgnoreCase("yes") && !ans.equalsIgnoreCase("No")) {
                    throw new Exception("Please enter yes or no");
                }
                Player.setStandingForIndex(index, !ans.equalsIgnoreCase("yes"));
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * loops through all the human users and asks if they want to sit.
     *
     * @param bf to read the target input
     */
    private void doYouWantToSit(BufferedReader bf) {
        boolean[] standing = Player.getStanding();
        for (int index = Player.STARTING_INDEX; index < Player.getNumHuman(); index++) {
            boolean curPlayerStand = standing[index];
            if (curPlayerStand) {
                promptUserToSit(bf, index);
            }
        }
    }

    /**
     * decides whether the computer sits or stand based on random
     */
    private void AISitDecision() {
        for (int index = Player.getNumHuman(); index < Player.getScores().length; index++) {
            if (Player.getStanding()[index]) {
                int percentage = (Dice.getNumOfDice() == Dice.TWO_DICE) ?
                                  Player.TWO_DICE_SIT_PERCENTAGE : Player.THREE_DICE_SIT_PERCENTAGE;
                boolean decision = r.nextInt(Player.ONE_HUNDRED_PERCENT) > percentage;
                Player.setStandingForIndex(index, decision);
            }
        }
    }

    /**
     * Return the points from the dice roll as a single integer:
     *    0 means two ones are rolled. So all the total score gets set to 0 for standing players.
     *    -1 means one one is rolled. So the score for the current round is set to 0 for standing players.
     *
     * @param roll array containing the values of the dice rolls
     * @return an integer -1, 0 or a positive integer.
     */
    private int getCurrentPoint(int[] roll) {
        if (roll.length == Dice.TWO_DICE) {
            return (roll[Dice.FIRST_DICE_INDEX] == Dice.ROLLED_ONE ||
                    roll[Dice.SECOND_DICE_INDEX] == Dice.ROLLED_ONE) ?
                    ( (roll[Dice.FIRST_DICE_INDEX] == Dice.ROLLED_ONE &&
                            roll[Dice.SECOND_DICE_INDEX] == Dice.ROLLED_ONE) ? DICE_ROLLS_TWO1 : DICE_ROLLS_ONE1) :
                      (roll[Dice.FIRST_DICE_INDEX] + roll[Dice.SECOND_DICE_INDEX]);
        } else {
            if (roll[Dice.FIRST_DICE_INDEX] == Dice.ROLLED_ONE && roll[Dice.SECOND_DICE_INDEX] == Dice.ROLLED_ONE &&
                    roll[Dice.THIRD_DICE_INDEX] == Dice.ROLLED_ONE) {
                return DICE_ROLLS_THREE1;
            } else if (roll[Dice.FIRST_DICE_INDEX] == Dice.ROLLED_ONE ?
                       (roll[Dice.SECOND_DICE_INDEX] == Dice.ROLLED_ONE ||
                               roll[Dice.THIRD_DICE_INDEX] == Dice.ROLLED_ONE) :
                       (roll[Dice.SECOND_DICE_INDEX] == Dice.ROLLED_ONE &&
                               roll[Dice.THIRD_DICE_INDEX] == Dice.ROLLED_ONE)) {
                return DICE_ROLLS_TWO1;
            } else if (roll[Dice.FIRST_DICE_INDEX] == Dice.ROLLED_ONE ||
                       roll[Dice.SECOND_DICE_INDEX] == Dice.ROLLED_ONE ||
                       roll[Dice.THIRD_DICE_INDEX] == Dice.ROLLED_ONE) {
                return DICE_ROLLS_ONE1;
            } else {
                return roll[Dice.FIRST_DICE_INDEX] + roll[Dice.SECOND_DICE_INDEX] + roll[Dice.THIRD_DICE_INDEX];
            }
        }
    }

    /**
     * updates if all players are standing (allStanding = True) and if all players are sitting (allSitting = True).
     */
    private void updateAllSitAndStand() {
        allSitting = true;
        allStanding = true;
        for (int index = Player.STARTING_INDEX; index < Player.getStanding().length; index++) {
            allStanding = allStanding && Player.getStanding()[index];
            allSitting = allSitting && !Player.getStanding()[index];
        }
    }

    /**
     * update points for standing players when a positive point (input) is rolled. Adds
     * the points gained to all score record keepers. Used as a helper function for updatePoints
     *
     * @param points must be positive
     */
    private void gainedInCurRound(int points) {
        for (int index = Player.STARTING_INDEX; index < scoresCurrentRound.length; index++){
            if (Player.getStanding()[index]) {
                scoresCurrentRound[index] += points;
                Player.setPointsForIndex(index, Player.getScores()[index] + points);
            }
        }
    }

    /**
     * update points for standing players when -1 (one one) is rolled. Used as a helper function
     * for updatePoints method.
     */
    private void lostInCurRound(){
        for (int index = Player.STARTING_INDEX; index < scoresCurrentRound.length; index++){
            if (Player.getStanding()[index]) {
                Player.setPointsForIndex(index, Player.getScores()[index] - scoresCurrentRound[index]);
                scoresCurrentRound[index] = Player.ZERO_SCORE;
            }
        }
    }

    /**
     * updates points for standing players when 0 (two ones) is rolled. Used as a helper function
     * for updatePoints method.
     */
    private void loseAllPoints() {
        for (int index = Player.STARTING_INDEX; index < scoresCurrentRound.length; index++){
            if (Player.getStanding()[index]) {
                Player.setPointsForIndex(index, Player.ZERO_SCORE);
                scoresCurrentRound[index] = Player.ZERO_SCORE;
            }
        }
    }

    /**
     * updates the scores for all players given the current roll of the dice. The scores are updated
     * also depending on the allSitting or allStanding variables. Here we also set the roundEnded
     * depending on the points and allSitting variable.
     *
     * @param points the translated points from a roll of dice
     */
    private void updatePoints(int points) {
        if (points < DICE_ROLLS_ONE1) {
            throw new IllegalArgumentException("Error points cannot be lower than -1");
        }

        if (points == DICE_ROLLS_TWO1 && !allStanding) {
            loseAllPoints();
            roundEnded = true;
        } else if (points == DICE_ROLLS_ONE1 && !allStanding) {
            lostInCurRound();
            roundEnded = true;
        } else if (allSitting) {
            roundEnded = true;
        } else if (points > DICE_ROLLS_TWO1) {
            gainedInCurRound(points);
        }
    }

    /**
     * For waiting animation effects. Calls upon the ConsoleHelper class for animation effects.
     *
     * @param message waiting message
     * @param iterations number of animated frames.
     */
    protected static void waiting(String message, int iterations){
        ConsoleHelper helper = new ConsoleHelper();

        for (int iter = Player.STARTING_INDEX; iter < iterations; iter++) {
            helper.animate(message);

            /* pause between each frame */
            try {
                Thread.sleep(THREAD_SLEEP_TIME_MILLISEC);
            } catch (Exception e){
                System.out.println("Error");
            }
        }
    }

    /**
     * Prints the winner
     */
    private void printWinner() {
        /* determines the index of the winning player in scores */
        int maxScore = Integer.MIN_VALUE;
        ArrayList<Integer> maxScoreIndices = new ArrayList<>();
        for (int index = Player.STARTING_INDEX; index < Player.getScores().length; index++) {
            if (maxScore < Player.getScores()[index]) {
                maxScore = Player.getScores()[index];
            }
        }

        /* adds all indices where the score is maximum score */
        for (int index = Player.STARTING_INDEX; index < Player.getScores().length; index++) {
            if (maxScore == Player.getScores()[index]) {
                maxScoreIndices.add(index);
            }
        }

        /* extracts the name of the winner (can be AI or Human) */
        String[] winners = new String[maxScoreIndices.size()];

        for (int index = Player.STARTING_INDEX; index < maxScoreIndices.size(); index++) {
            int curMaxInd = maxScoreIndices.get(index);
            winners[index] = (curMaxInd < Player.getNumHuman()) ? Player.getHumanNames()[curMaxInd] :
                    Player.getTheAINames()[curMaxInd - Player.getNumHuman()];
        }

        /* formats the winner string so that it fits into the ASCII art below */
        System.out.print("\n\n\n******************************************************************\n" +
                "                         CONGRATULATIONS!\n" +
                "*******************************************************************\n" +
                "        *                                                 *\n");
        for (int index = Player.STARTING_INDEX; index < maxScoreIndices.size(); index++) {
            String winner = winners[index];
            int strLength = winner.length();
            int front = (ASTERIX_STRING_LENGTH - strLength) / PARITY_CHECKER;
            int back = (strLength % PARITY_CHECKER == PARITY_MOD_CHECKER) ? front : front + FILL_ONE_EMPTY_SPACE;
            winner = fillString(front, EMPTY_CHAR) + winner + fillString(back, EMPTY_CHAR);

            /* prints the ASCII art displaying the winning player */

            System.out.printf("                         *--------------*\n" +
                    "                         |  WINNER IS:  |\n" +
                    "                **********************************\n" +
                    "                |%s|\n" +
                    "                **********************************\n" +
                    "        *                                                 *\n", winner);
        }
        System.out.print("*******************************************************************\n" +
                "                         END OF THE GAME.\n" +
                "*******************************************************************\n\n\n");
    }
}
