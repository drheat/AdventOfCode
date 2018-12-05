package com.catalinionescu.adventofcode.y2018;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.catalinionescu.adventofcode.common.Log;

public class Day005 {
    public static String INPUT_URL = "https://adventofcode.com/2018/day/5/input";
    public static String INPUT_FILE = "inputs/input005.txt";

    private static int REACT_DIFF = 'a' - 'A';

    private void main() throws IOException {
        List<String> input = Files.readAllLines(Paths.get(INPUT_FILE));

        if (input == null || input.isEmpty()) {
            Log.logErrorMessage("Input data set is empty!");
            return;
        }

        String polymer = input.get(0);

        // ********* Part 1
        char[] chain = polymer.toCharArray();
        char curr, next;
        int idx = 0, diff, reactionCount = 0;
        boolean doneWork = true;
        while (doneWork && chain.length > 0) {
            curr = chain[idx];
            if (idx + 1 < chain.length) {
                idx++;
                next = chain[idx];
                diff = next - curr;
                if (diff < 0) {
                    diff = -diff;
                }

                if (diff == REACT_DIFF) {
                    // found a match
                    chain[idx] = ' ';
                    chain[idx - 1] = ' ';
                    reactionCount++;
                }
            } else {
                // Reduce the polymer by eliminating the spaces.
                char[] newChain = new char[chain.length - reactionCount * 2];
                idx = 0;
                for (int i = 0; i < newChain.length; i++) {
                    while (chain[idx] == ' ') {
                        idx++;
                    }
                    newChain[i] = chain[idx];
                    idx++;
                }
                chain = newChain;

                // Get ready for the next pass
                idx = 0;
                doneWork = reactionCount > 0;
                reactionCount = 0;
            }
        }

        polymer = String.valueOf(chain);

        // Log.logInfoMessage("Resulting polymer: %s", polymer);
        Log.logInfoMessage("Length: %d", polymer.length());

        // ********* Part 2
        int shortestLength = Integer.MAX_VALUE;
        char removedUnit = 'a';
        int removeCount;
        for (char unit = 'a'; unit <= 'z'; unit++) {
            polymer = input.get(0);
            chain = polymer.toCharArray();
            removeCount = 0;
            // Remove it from the polymer
            for (int i = 0; i < chain.length; i++) {
                if ((chain[i] == unit) || (chain[i] == unit - REACT_DIFF)) {
                    chain[i] = ' ';
                    removeCount++;
                }
            }

            // Reduce the polymer by eliminating the spaces.
            char[] newChain = new char[chain.length - removeCount];
            idx = 0;
            for (int i = 0; i < newChain.length; i++) {
                while (chain[idx] == ' ') {
                    idx++;
                }
                newChain[i] = chain[idx];
                idx++;
            }
            chain = newChain;

            // Perform the reduction
            doneWork = true;
            idx = 0;
            reactionCount = 0;
            while (doneWork && chain.length > 0) {
                curr = chain[idx];
                if (idx + 1 < chain.length) {
                    idx++;
                    next = chain[idx];
                    diff = next - curr;
                    if (diff < 0) {
                        diff = -diff;
                    }

                    if (diff == REACT_DIFF) {
                        // found a match
                        chain[idx] = ' ';
                        chain[idx - 1] = ' ';
                        reactionCount++;
                    }
                } else {
                    // Reduce the polymer by eliminating the spaces.
                    newChain = new char[chain.length - reactionCount * 2];
                    idx = 0;
                    for (int i = 0; i < newChain.length; i++) {
                        while (chain[idx] == ' ') {
                            idx++;
                        }
                        newChain[i] = chain[idx];
                        idx++;
                    }
                    chain = newChain;

                    // Get ready for the next pass
                    idx = 0;
                    doneWork = reactionCount > 0;
                    reactionCount = 0;
                }
            }

            polymer = String.valueOf(chain);
            if (polymer.length() < shortestLength) {
                shortestLength = polymer.length();
                removedUnit = unit;
            }
        }

        Log.logInfoMessage("Shortest length: %d, obtained by removing %c/%c", shortestLength, removedUnit - REACT_DIFF, removedUnit);
    }

    public static void main(String[] args) throws IOException {
        new Day005().main();
    }
}
