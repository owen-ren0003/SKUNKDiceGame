/*
 * Copyright (c) 2019, Owen Ren. All rights reserved.
 *
 */

/**
 * This class is used to show command line progress animation in the console. The animation frames are:
 * [ \ ], [ | ], [ / ], [ - ]
 * Warning: This animation does not work in Eclipse console
 *
 * @author Owen Ren
 * @version 1.0
 * @since July 9, 2019
 */
public class ConsoleHelper {
    private String lastLine;
    private int anim;

    /* Symbolic constants used to get rid of magic numbers */
    public static final String EMPTY_STRING = "";
    public static final int CRITICAL_LENGTH = 1;

    public static final int STEP_ONE    = 1;
    public static final int STEP_TWO    = 2;
    public static final int STEP_THREE  = 3;
    public static final int RESET_VALUE = 0;

    public ConsoleHelper(){
        lastLine = EMPTY_STRING;
    }

    /**
     * @param line prints progress bars for each frame.
     */
    public void print(String line) {

        // clears the last line if needed
        if (lastLine.length() > line.length()) {
            String temp = Rounds.EMPTY_STRING;
            for (int i = Player.STARTING_INDEX; i < lastLine.length(); i++) {
                temp += Rounds.EMPTY_CHAR;
            }
            if (temp.length() > CRITICAL_LENGTH)
                System.out.print("\n" + temp);
        }
        System.out.print("\r" + line);
        lastLine = line;
    }

    /**
     * @param line animates the progress bar. Must be used in conjunction with a for loop.
     */
    public void animate(String line) {
        switch (anim) {
            case STEP_ONE:
                print("[ \\ ] " + line);
                break;
            case STEP_TWO:
                print("[ | ] " + line);
                break;
            case STEP_THREE:
                print("[ / ] " + line);
                break;
            default:
                anim = RESET_VALUE;
                print("[ - ] " + line);
        }
        anim++;
    }
}
