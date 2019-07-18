/*
 * Copyright (c) 2019, Owen Ren. All rights reserved.
 *
 ***********************************************************
 * RUN THIS FILE
 ***********************************************************
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

/**
 * This class is used to launch the skunk game in console.
 *
 * @author Owen Ren
 * @version 1.0
 * @since July 9, 2019
 */
public class GameLauncher {

    private static boolean playAgain;

    /* Symbolic constants used to get rid of magic numbers */
    public static final int timeBetweenBigBlockTextMS = 500;
    public static final int timeBetweenSmallTextMS    = 200;

    public static void main(String[] args) throws Exception {

        /* Initializes bufferedReader so users can type in the console. */
        playAgain = true;
        InputStreamReader r = new InputStreamReader(System.in);
        BufferedReader bf = new BufferedReader(r);

        /* Continuous loop until user says no to "PLAY AGAIN" */
        while(playAgain) {

            /* Prints the introduction and asks if the user wants to see game instructions*/
            printIntro();
            printInstructions(bf);

            /* Gets the users input */
            GetGameSettings settings = new GetGameSettings(bf);

            /* Initializes the dice for the current game */
            new Dice(settings.getNumberOfDie());

            /* Initializes the players for the current game */
            new Player(settings.getNumberOfHumanPlayers(), settings.getNumberOfAIPlayers(), bf);

            /* Executes the rounds */
            new Rounds(bf);

            /* Asks if the user wants to play again */
            askPlayAgain(bf);
        }

        /* Closes the bufferedReader and InputStreamReader */
        bf.close();
        r.close();
    }

    /**
     * This methods asks if the user wants to play again, and breaks through the while loop depending on the answer.
     *
     * @param bf to prompt the user for input
     * @throws IOException if something goes wrong with the bufferedReader
     */
    private static void askPlayAgain(BufferedReader bf) throws IOException{
        System.out.println("PLAY AGAIN? (Yes or No)");
        while(true) {
            String ans = bf.readLine();
            if (ans.equalsIgnoreCase("yes")) {
                break;
            } else if (ans.equalsIgnoreCase("no")) {
                playAgain = false;
                break;
            } else {
                System.out.println("Please enter Yes or No.");
            }
        }
    }

    /**
     * This prints the character into a big font
     *
     * @param c should be an alphabetical letter
     */
    private static void printSkunk(char c) {
        if (c == 's') {
            System.out.println(".----------------.");
            System.out.println("| .--------------. |");
            System.out.println("| |    _______   | |");
            System.out.println("| |   /  ___  |  | |");
            System.out.println("| |  |  (__ \\_|  | |");
            System.out.println("| |   '.___`-.   | |");
            System.out.println("| |  |`\\____) |  | |");
            System.out.println("| |  |_______.'  | |");
            System.out.println("| |              | |");
            System.out.println("| '--------------' |");
            System.out.println("'----------------' ");
        } else if (c == 'k') {
            System.out.println(" .----------------. ");
            System.out.println("| .--------------. |");
            System.out.println("| |  ___  ____   | |");
            System.out.println("| | |_  ||_  _|  | |");
            System.out.println("| |   | |_/ /    | |");
            System.out.println("| |   |  __'.    | |");
            System.out.println("| |  _| |  \\ \\_  | |");
            System.out.println("| | |____||____| | |");
            System.out.println("| |              | |");
            System.out.println("| '--------------' |");
            System.out.println(" '----------------' ");
        } else if (c == 'u') {
            System.out.println(" .----------------. ");
            System.out.println("| .--------------. |");
            System.out.println("| | _____  _____ | |");
            System.out.println("| ||_   _||_   _|| |");
            System.out.println("| |  | |    | |  | |");
            System.out.println("| |  | '    ' |  | |");
            System.out.println("| |   \\ `--' /   | |");
            System.out.println("| |    `.__.'    | |");
            System.out.println("| |              | |");
            System.out.println("| '--------------' |");
            System.out.println(" '----------------' ");
        } else if (c == 'n') {
            System.out.println(" .-----------------.");
            System.out.println("| .--------------. |");
            System.out.println("| | ____  _____  | |");
            System.out.println("| ||_   \\|_   _| | |");
            System.out.println("| |  |   \\ | |   | |");
            System.out.println("| |  | |\\ \\| |   | |");
            System.out.println("| | _| |_\\   |_  | |");
            System.out.println("| ||_____|\\____| | |");
            System.out.println("| |              | |");
            System.out.println("| '--------------' |");
            System.out.println(" '----------------' ");
        }
    }

    /**
     * prints the introduction to the game
     *
     * @throws InterruptedException if there is something is wrong with thread sleep
     */
    private static void printIntro() throws InterruptedException {
        char[] welcome = "\n\nWELCOME TO THE GAME:\n\n".toCharArray();
        for (char c: welcome) {
            System.out.print(c);
            TimeUnit.MILLISECONDS.sleep(timeBetweenSmallTextMS);
        }

        char[] skunk = "skunk".toCharArray();
        for (char c: skunk) {
            printSkunk(c);
            TimeUnit.MILLISECONDS.sleep(timeBetweenBigBlockTextMS);
        }
    }

    /**
     * prints the instructions if the user wants to
     *
     * @param bf to prompt the user for input
     */
    private static void printInstructions(BufferedReader bf) throws IOException, InterruptedException {

        System.out.print("\nDo you want to read the game instructions (yes or no)? ");
        while(true) {
            String ans = bf.readLine();
            if (ans.equalsIgnoreCase("yes")) {
                System.out.print("\n%%============================================================%%\n" +
                        "\t\t\t  SKUNK GAME INSTRUCTIONS\n" +
                        "%%============================================================%%\n" +
                        "\n" +
                        "There are five rounds in the game. Each round corresponds with \n" +
                        "each letter of the word SKUNK starting with the first letter. \n" +
                        "Before the game starts, the user pick whether to play a two \n" +
                        "dice game or a three dice game, and then inputs the number of \n" +
                        "human or AI players.\n" +
                        "\n");
                Thread.sleep(Rounds.THREAD_SLEEP_TIME_MILLISEC);
                System.out.print("Players start the game standing up. The system rolls all the \n" +
                        "dice each turn. The players and AI all have a choice before \n" +
                        "each roll whether to remain standing or whether to sit down. \n" +
                        "If they are standing when a roll is made that does not contain \n" +
                        "a “1” in it, they get to record those points on their score \n" +
                        "sheet under the correct column for each round. If they decide \n" +
                        "to sit down at any time during the round they simply total up \n" +
                        "their points from that round and wait for the round to be \n" +
                        "completed.\n" +
                        "\n");
                Thread.sleep(Rounds.THREAD_SLEEP_TIME_MILLISEC);
                System.out.print("If they are ever standing when a “1” is rolled they lose ALL \n" +
                        "OF their points for that round. If they are ever standing when \n" +
                        "two 1’s (snake eyes) are rolled then they lose ALL of their \n" +
                        "points from ALL ROUNDS THAT HAVE BEEN PLAYED TO THAT POINT. \n" +
                        "For three dice games, if they are ever standing when triple \n" +
                        "\"1\"'s are rolled then they GAIN 100 points.\n" +
                        "\n");
                Thread.sleep(Rounds.THREAD_SLEEP_TIME_MILLISEC);
                System.out.print("Once students sit down they cannot get up again until the next \n" +
                        "round. Each round ends when a “1” is rolled (except triple \n" +
                        "1's) or when all students sit down. The object of the game is \n" +
                        "to get the highest score possible through the five rounds of \n" +
                        "Skunk. When the last round is over students should total their \n" +
                        "scores for all five rounds to determine their final score. \n" +
                        "\n" +
                        "%%============================================================%%\n\n" +
                        "%%============================================================%%\n");
                break;
            } else if (ans.equalsIgnoreCase("no")){
                break;
            } else {
                System.out.println("Please enter yes or no");
            }
        }
    }
}
