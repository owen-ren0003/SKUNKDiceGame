/*
 * Copyright (c) 2019, Owen Ren. All rights reserved.
 *
 */


import java.util.Random;

/**
 * This class is used to simulate dice rolls. The number of dice should only be two or three.
 *
 * @author Owen Ren
 * @version 1.0
 * @since July 9, 2019
 */
public class Dice {
    private static int[] dices;
    private static Random r;

    /* Symbolic constants used to get rid of magic numbers */
    public static final int DICE_FACES  = 6;
    public static final int RAND_START  = 1;
    public static final int BEGIN_INDEX = 0;
    public static final int TWO_DICE    = 2;
    public static final int THREE_DICE  = 3;

    public static final int FIRST_DICE_INDEX  = 0;
    public static final int SECOND_DICE_INDEX = 1;
    public static final int THIRD_DICE_INDEX  = 2;

    public static final int ROLLED_ONE      = 1;
    public static final int DIE_ART_NUM_COL = 34;
    public static final int DECREMENT_ONE   = -1;
    public static final int DIE_ART_NUM_ROW = 20;

    public static final int DIE_ONE   = 1;
    public static final int DIE_TWO   = 2;
    public static final int DIE_THREE = 3;
    public static final int DIE_FOUR  = 4;
    public static final int DIE_FIVE  = 5;
    public static final int DIE_SIX   = 6;

    /**
     * @param n the number of dice.
     */
    public Dice(int n){
        if (n != TWO_DICE && n != THREE_DICE) {
            throw new IllegalArgumentException("Invalid number of dice.");
        }
        dices = new int[n];
        r = new Random();
    }

    /**
     * @return one roll of all of the dice.
     */
    public static int[] rollDice(){
        for (int i = BEGIN_INDEX; i < dices.length; i++){
            dices[i] = RAND_START + r.nextInt(DICE_FACES);
        }

        System.out.println("The dice rolls are: ");
        Rounds.waiting("Rolling Dice...", Rounds.WAITING_TIME_SEC);
        System.out.println("\n");
        if (dices.length == Dice.TWO_DICE) {

            int index = BEGIN_INDEX;

            for (int row = BEGIN_INDEX; row < DIE_ART_NUM_ROW; row++){
                String curRow = getRollArt(dices[FIRST_DICE_INDEX]).substring(index, index + DIE_ART_NUM_COL + DECREMENT_ONE) + "    " +
                        getRollArt(dices[SECOND_DICE_INDEX]).substring(index, index + DIE_ART_NUM_COL);
                System.out.print(curRow);
                index += DIE_ART_NUM_COL;
            }
        } else {
            int index = BEGIN_INDEX;

            for (int row = BEGIN_INDEX; row < DIE_ART_NUM_ROW; row++){
                String curRow = getRollArt(dices[FIRST_DICE_INDEX]).substring(index,
                                index + DIE_ART_NUM_COL + DECREMENT_ONE) + "    " +
                        getRollArt(dices[SECOND_DICE_INDEX]).substring(index,
                                index + DIE_ART_NUM_COL + DECREMENT_ONE) + "    " +
                        getRollArt(dices[THIRD_DICE_INDEX]).substring(index, index + DIE_ART_NUM_COL);
                System.out.print(curRow);
                index += DIE_ART_NUM_COL;
            }
        }
        Rounds.waiting(Rounds.EMPTY_STRING, Rounds.WAITING_TIME_SEC);
        return dices;
    }

    /**
     * @return the number of dices
     */
    public static int getNumOfDice() {
        return dices.length;
    }

    /**
     * For aesthetic purposes, to display the rolls of dice
     *
     * @param number the number corresponding to one die roll
     * @return an art displaying the number
     */
    public static String getRollArt(int number) {

        /* length of one row is DIE_ART_ROW_LENGTH */
        if (number == DIE_ONE) {
            return "*********************************\n" +
                    "*                               *\n" +
                    "*            1111111            *\n" +
                    "*           1::::::1            *\n" +
                    "*          1:::::::1            *\n" +
                    "*          111:::::1            *\n" +
                    "*             1::::1            *\n" +
                    "*             1::::1            *\n" +
                    "*             1::::1            *\n" +
                    "*             1::::l            *\n" +
                    "*             1::::l            *\n" +
                    "*             1::::l            *\n" +
                    "*             1::::l            *\n" +
                    "*             1::::l            *\n" +
                    "*          111::::::111         *\n" +
                    "*          1::::::::::1         *\n" +
                    "*          1::::::::::1         *\n" +
                    "*          111111111111         *\n" +
                    "*                               *\n" +
                    "*********************************\n";
        } else if (number == DIE_TWO){
            return "*********************************\n" +
                    "*                               *\n" +
                    "*       222222222222222         *\n" +
                    "*      2:::::::::::::::22       *\n" +
                    "*      2::::::222222:::::2      *\n" +
                    "*      2222222     2:::::2      *\n" +
                    "*                  2:::::2      *\n" +
                    "*                  2:::::2      *\n" +
                    "*               2222::::2       *\n" +
                    "*          22222::::::22        *\n" +
                    "*        22::::::::222          *\n" +
                    "*       2:::::22222             *\n" +
                    "*      2:::::2                  *\n" +
                    "*      2:::::2                  *\n" +
                    "*      2:::::2       222222     *\n" +
                    "*      2::::::2222222:::::2     *\n" +
                    "*      2::::::::::::::::::2     *\n" +
                    "*      22222222222222222222     *\n" +
                    "*                               *\n" +
                    "*********************************\n";
        } else if (number == DIE_THREE) {
            return "*********************************\n" +
                    "*                               *\n" +
                    "*      333333333333333          *\n" +
                    "*     3:::::::::::::::33        *\n" +
                    "*     3::::::33333::::::3       *\n" +
                    "*     3333333     3:::::3       *\n" +
                    "*                 3:::::3       *\n" +
                    "*                 3:::::3       *\n" +
                    "*         33333333:::::3        *\n" +
                    "*         3:::::::::::3         *\n" +
                    "*         33333333:::::3        *\n" +
                    "*                 3:::::3       *\n" +
                    "*                 3:::::3       *\n" +
                    "*                 3:::::3       *\n" +
                    "*     3333333     3:::::3       *\n" +
                    "*     3::::::33333::::::3       *\n" +
                    "*     3:::::::::::::::33        *\n" +
                    "*      333333333333333          *\n" +
                    "*                               *\n" +
                    "*********************************\n";
        } else if (number == DIE_FOUR){
            return "*********************************\n" +
                    "*                               *\n" +
                    "*             444444444         *\n" +
                    "*            4::::::::4         *\n" +
                    "*           4:::::::::4         *\n" +
                    "*          4::::44::::4         *\n" +
                    "*         4::::4 4::::4         *\n" +
                    "*        4::::4  4::::4         *\n" +
                    "*       4::::4   4::::4         *\n" +
                    "*      4::::444444::::444       *\n" +
                    "*      4::::::::::::::::4       *\n" +
                    "*      4444444444:::::444       *\n" +
                    "*                4::::4         *\n" +
                    "*                4::::4         *\n" +
                    "*                4::::4         *\n" +
                    "*              44::::::44       *\n" +
                    "*              4::::::::4       *\n" +
                    "*              4444444444       *\n" +
                    "*                               *\n" +
                    "*********************************\n";
        } else if (number == DIE_FIVE){
            return "*********************************\n" +
                    "*                               *\n" +
                    "*     555555555555555555        *\n" +
                    "*     5::::::::::::::::5        *\n" +
                    "*     5::::::::::::::::5        *\n" +
                    "*     5:::::555555555555        *\n" +
                    "*     5:::::5                   *\n" +
                    "*     5:::::5                   *\n" +
                    "*     5:::::5555555555          *\n" +
                    "*     5:::::::::::::::5         *\n" +
                    "*     555555555555:::::5        *\n" +
                    "*                 5:::::5       *\n" +
                    "*                 5:::::5       *\n" +
                    "*     5555555     5:::::5       *\n" +
                    "*     5::::::55555::::::5       *\n" +
                    "*      55:::::::::::::55        *\n" +
                    "*        55:::::::::55          *\n" +
                    "*          555555555            *\n" +
                    "*                               *\n" +
                    "*********************************\n";
        } else if (number == DIE_SIX){
            return "*********************************\n" +
                    "*                               *\n" +
                    "*              66666666         *\n" +
                    "*             6::::::6          *\n" +
                    "*            6::::::6           *\n" +
                    "*           6::::::6            *\n" +
                    "*          6::::::6             *\n" +
                    "*         6::::::6              *\n" +
                    "*        6::::::6               *\n" +
                    "*       6::::::::66666          *\n" +
                    "*      6::::::::::::::66        *\n" +
                    "*     6::::::66666:::::6        *\n" +
                    "*     6:::::6     6:::::6       *\n" +
                    "*     6:::::6     6:::::6       *\n" +
                    "*     6::::::66666::::::6       *\n" +
                    "*      66:::::::::::::66        *\n" +
                    "*        66:::::::::66          *\n" +
                    "*          666666666            *\n" +
                    "*                               *\n" +
                    "*********************************\n";
        }
        return null;
    }
}
