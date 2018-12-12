package com.catalinionescu.adventofcode.y2018;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.catalinionescu.adventofcode.common.Log;

public class Day012 {
    public static String INPUT_URL = "https://adventofcode.com/2018/day/12/input";
    public static String INPUT_FILE = "inputs/input012.txt";

    // *** Part 1
    private static int GENERATIONS = 20;

    // *** For part 2, pick a number high enough to produce stabilization of the plant growth pattern (e.g. same pattern, just perhaps shifted to the right or
    // left)
    // private static int GENERATIONS = 200;
    private static long PART2_GENERATIONS = 50000000000L;

    class Pattern {
        private final String pattern;
        private final char chpattern[];
        private final char outcome;

        public Pattern(String rawPattern) {
            // ..### => .
            String parts[] = rawPattern.split(" => ");
            pattern = parts[0];
            chpattern = pattern.toCharArray();
            outcome = parts[1].charAt(0);
        }

        public boolean matches(String pattern) {
            return this.pattern.equals(pattern);
        }

        public boolean matches(char[] input, int pos) {
            for (int i = 0; i < 5; i++) {
                if (chpattern[i] != input[pos + i]) {
                    return false;
                }
            }
            return true;
        }

        public char getOutcome() {
            return outcome;
        }
    }

    int leftmostPot = 0;

    /**
     * Makes sure we always have 5 empty pots to the left of the first non-empty pot. This is to match the pattern ".....".
     * 
     * @param input Current string starting at first "#"
     * @return Prepended string
     */
    public String prepend(String input) {
        leftmostPot -= 5;
        return "....." + input;
    }

    /**
     * Makes sure we always have 5 empty pots to the right of the last non-empty pot.
     * 
     * @param input Current string ending in "#"
     * @return Appended string
     */
    public String append(String input) {
        return input + ".....";
    }

    /**
     * Trims all empty pots to the left and right.
     * 
     * @param input Current string
     * @return String starting and ending with "#"
     */
    public String trim(String input) {
        int first = input.indexOf('#');
        int last = input.lastIndexOf('#');
        if (first == -1) {
            return "";
        }

        leftmostPot += first;
        return input.substring(first, last + 1);
    }

    /**
     * Counts the number of pots alive.
     * 
     * @param input Current generation
     * @return Number of pots alive
     */
    public int countAlive(String input) {
        char pots[] = input.toCharArray();
        int result = 0;
        for (int i = 0; i < pots.length; i++) {
            if (pots[i] == '#') {
                result += (i + leftmostPot);
            }
        }

        return result;
    }

    private void main() throws IOException {
        List<String> input = Files.readAllLines(Paths.get(INPUT_FILE));

        if (input == null || input.isEmpty()) {
            Log.logErrorMessage("Input data set is empty!");
            return;
        }

        Iterator<String> iter = input.iterator();

        String parts[] = iter.next().split(":");
        String currentPots = parts[1].trim();
        // skip the blank line
        iter.next();

        List<Pattern> patterns = new ArrayList<>();
        // Parse the patterns
        while (iter.hasNext()) {
            patterns.add(new Pattern(iter.next()));
        }

        // Log.logInfoMessage("0: %s (%d)", currentPots, leftmostPot);
        String initialPots = currentPots;
        int prevSum = 0;
        for (int i = 0; i < GENERATIONS; i++) {
            leftmostPot = 0;
            currentPots = initialPots;
            for (int j = 0; j <= i; j++) {
                currentPots = append(prepend(trim(currentPots)));
                char oldPots[] = currentPots.toCharArray();
                char pots[] = new char[oldPots.length];
                Arrays.fill(pots, '.');
                for (int potIdx = 0, len = currentPots.length() - 5; potIdx < len; potIdx++) {
                    for (Pattern pattern : patterns) {
                        if (pattern.matches(oldPots, potIdx)) {
                            pots[potIdx + 2] = pattern.outcome;
                            break;
                        }
                    }
                }
                currentPots = new String(pots);
                // Log.logInfoMessage("%d: %s (%d)", i + 1, currentPots, leftmostPot);
            }

            int currentSum = countAlive(currentPots);
            Log.logInfoMessage("Generation: %d Sum: %d Diff: %d", i, currentSum, currentSum - prevSum);
            prevSum = currentSum;
        }

        /*
         * Part 2
         * 
         * To run the program for second puzzle, comment first GENERATIONS and uncomment the second.
         * 
         * After running the program once, we notice at generation 102 the output pattern stabilizes and it just shifts right but doesn't change. The sum of
         * the alive plant pot indices increases by 69 per generation. So, to produce the puzzle answer, we just multiply the generations after 102 by 69 and
         * add the fixed (non-shifting) part we got at generation 102.
         */
        long part2Response = 9306 + (PART2_GENERATIONS - 102) * 69;
        Log.logInfoMessage("After %d generations, sum of all pots is: %d", PART2_GENERATIONS, part2Response);
    }

    public static void main(String[] args) throws IOException {
        new Day012().main();
    }
}
