/*
 * Copyright (c) 2019, Owen Ren. All rights reserved.
 *
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * This class record tracks and stores all of the players information from one play-through the skunk game.
 * The instance variables are defined as follows:
 *     numHumans  = number of human players
 *     numAI      = number of AI (computer) players.
 *     humanNames = an array that stores all of the human player/team names.
 *     theAINames = an array that stores all of the AI player names. They are named with format AI(#number).
 *     scores     = an array that stores the scores of each player (both human and AI). This array will be
 *                  updated throughout the course of the game as players lose and gain points. The first
 *                  consecutive entries are those scores for the human players. The remaining entries are
 *                  for the AI players.
 *     standing   = an array that stores whether or not each player is standing (both human and AI).
 *                  This array will updated throughout the course of the game as players choose to sit or
 *                  stand. The first consecutive entries are for the human players. The remaining entries
 *                  are for the AI players.
 * AI Difficulty: 2 dice - AI sits with a percentage of 30
 *                3 dice - AI sits with a percentage of 45
 *
 * @author Owen Ren
 * @version 1.0
 * @since July 9, 2019
 */
public class Player {
    private static int       numHuman;
    private static int       numAI;
    private static String[]  humanNames;
    private static String[]  theAINames;
    private static int[]     scores;
    private static boolean[] standing;

    /* Symbolic constants used to get rid of magic numbers */
    public static final int STARTING_INDEX = 0;
    public static final int ZERO_SCORE     = 0;
    public static final int ONE_INCREMENT  = 1;

    public static final int TWO_DICE_SIT_PERCENTAGE   = 30;
    public static final int THREE_DICE_SIT_PERCENTAGE = 45;
    public static final int ONE_HUNDRED_PERCENT       = 100;

    /**
     * The inputs are extracted from the launch of GetGameSettings class.
     *
     * @param numHuman the number of human players
     * @param numAI the number of AI players
     * @param bf to take in user inputs
     * @throws IOException if something goes wrong with bufferedReader
     */
    public Player(int numHuman, int numAI, BufferedReader bf) throws IOException {
        Player.numHuman = numHuman;
        Player.numAI = numAI;
        humanNames = new String[numHuman];
        setHumanNames(bf);
        theAINames = new String[numAI];
        setTheAINames();
        scores = new int[Player.numHuman + Player.numAI];
        standing = new boolean[Player.numHuman + Player.numAI];
        resetAllStanding();
    }

    /**
     * @return the number of human players
     */
    public static int getNumHuman() {
        return numHuman;
    }

    /**
     * @return the number of AI players
     */
    public static int getNumAI() {
        return numAI;
    }

    /**
     * @return the array of scores of all the players
     */
    public static int[] getScores() {
        return scores;
    }

    /**
     * @return the array of those players who are standing
     */
    public static boolean[] getStanding() {
        return standing;
    }

    /**
     * @return an array of those human player names
     */
    public static String[] getHumanNames() {
        return humanNames;
    }

    /**
     * @return an array of those AI player names
     */
    public static String[] getTheAINames() {
        return theAINames;
    }

    /* The methods below are used to in the constructor or used from the Round class */

    /**
     * This method prompts the player to set their (team) names and stores them in humanNames.
     *
     * @param bf for taking user inputs
     * @throws IOException if something goes wrong with buffered reader.
     */
    private static void setHumanNames(BufferedReader bf) throws IOException {
        for (int index = STARTING_INDEX; index < numHuman; index++){
            System.out.printf("Enter name for Player #%d: ", (index + ONE_INCREMENT));
            /* prompts the current player for name */
            String name = bf.readLine();

            /* stores the name in the current index */
            humanNames[index] = name;
        }
    }

    /**
     * This method sets the names for the AI players. Name is of the form: AI(#number) where
     * number indexes the AI players.
     */
    private static void setTheAINames() {
        for (int index = STARTING_INDEX; index < numAI; index++) {
            String name = "AI(#" + (index + ONE_INCREMENT) + ")";
            theAINames[index] = name;
        }
    }

    /**
     * Resets everyone to standing position
     */
    protected static void resetAllStanding() {
        Arrays.fill(standing, true);
    }

    /**
     * Resets the scores of all the players
     */
    protected static void resetScores(){
        Arrays.fill(scores, ZERO_SCORE);
    }

    /**
     * This methods sets the standing position for a particular player
     *
     * @param index the index
     * @param sit the value to be set
     */
    protected static void setStandingForIndex(int index, boolean sit) {
        Player.standing[index] = sit;
    }

    /**
     * This methods sets the score for a particular player
     *
     * @param index the index
     * @param score the score to be set
     */
    protected static void setPointsForIndex(int index, int score){
        Player.scores[index] = score;
    }
}
