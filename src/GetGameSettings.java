/*
 * Copyright (c) 2019, Owen Ren. All rights reserved.
 *
 */

import java.io.BufferedReader;

/**
 * This class requires a BufferedReader as input for all of its functions.
 *
 * @author Owen Ren
 * @version 1.0
 * @since June 30, 2019
 */
public class GetGameSettings {

    private int numberOfDie;
    private int numberOfHumanPlayers;
    private int numberOfAIPlayers;

    /* Symbolic constants used to get rid of magic numbers */
    public static final boolean HUMAN         = true;
    public static final String REG_EX_INTEGER = "^[0-9]+$|^-[0-9]+$";
    public static final int MIN_NUM_PLAYERS   = 0;

    /**
     * @param bf prompts the user for input.
     */
    public GetGameSettings(BufferedReader bf) {
        System.out.printf("\n\nEnter the game settings you want to play with:\n\n");
        inputNumberOfDie(bf);
        inputPlayerInfo(bf, HUMAN);
        inputPlayerInfo(bf, !HUMAN);
    }

    /**
     * Prompts the user to enter the number of Die in the game
     *
     * @param bf to read user inputs
     */
    private void inputNumberOfDie(BufferedReader bf) {
        while(numberOfDie != Dice.TWO_DICE && numberOfDie != Dice.THREE_DICE) {
            System.out.printf("Select number of dice (2 or 3): ");
            try {
                numberOfDie = Integer.parseInt(bf.readLine());
            } catch (Exception e) {
                System.out.println("Not a 2 or 3, please re-enter.");
            }
        }
    }

    /**
     * Prompts the user for the number of human players or AI players based on the value of human
     *
     * @param bf to read user inputs
     * @param human whether this is for human player or not
     */
    private void inputPlayerInfo(BufferedReader bf, boolean human) {
        while(true) {
            System.out.printf("Enter the number of %s players: ", human ? "human" : "AI");
            try {
                String players = bf.readLine();
                if (!players.matches(REG_EX_INTEGER)){
                    throw new IllegalArgumentException("Please enter an integer");
                }
                int numPlayers = Integer.parseInt(players);
                if (numPlayers < MIN_NUM_PLAYERS) {
                    throw new IllegalArgumentException("Not a non-negative integer, please re-enter.");
                } else if (human) {
                    numberOfHumanPlayers = numPlayers;
                } else if (!human && numPlayers == MIN_NUM_PLAYERS && numberOfHumanPlayers == MIN_NUM_PLAYERS) {
                    // Total number of players cannot be 0
                    throw new IllegalArgumentException("There must be at least ONE player.");
                } else {
                    numberOfAIPlayers = numPlayers;
                }
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * @return the number of die used.
     */
    public int getNumberOfDie() {
        return numberOfDie;
    }

    /**
     * @return the number of human players
     */
    public int getNumberOfHumanPlayers() {
        return numberOfHumanPlayers;
    }

    /**
     * @return the number of AI players
     */
    public int getNumberOfAIPlayers() {
        return numberOfAIPlayers;
    }
}
