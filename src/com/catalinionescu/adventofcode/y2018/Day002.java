package com.catalinionescu.adventofcode.y2018;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.catalinionescu.adventofcode.common.Log;

public class Day002 {
    public static String INPUT_URL = "https://adventofcode.com/2018/day/2/input";
    public static String INPUT_FILE = "inputs/input002.txt";

    private void main() throws IOException {
        List<String> input = Files.readAllLines(Paths.get(INPUT_FILE));

        if (input == null || input.isEmpty()) {
            Log.logErrorMessage("Input data set is empty!");
            return;
        }

        // ********* Part 1

        int twos = 0, threes = 0;
        // Only the first repetition of a kind counts, so if e.g. a box ID has 2 'a' and 2 'b' it only counts once towards twos.
        boolean oneTwo = false, oneThree = false;

        // Taking advantage of the fact that box IDs contain only lowercase letters, we can use a frequency vector
        int[] freq = new int[26];

        for (String line : input) {
            oneTwo = false;
            oneThree = false;

            for (char ch : line.toCharArray()) {
                freq[ch - 'a']++;
            }

            for (int i = 0; i < freq.length; i++) {
                if (freq[i] == 2 && !oneTwo) {
                    twos++;
                    oneTwo = true;
                } else if (freq[i] == 3 && !oneThree) {
                    threes++;
                    oneThree = true;
                }

                // Reset for next box ID
                freq[i] = 0;
            }
        }

        Log.logInfoMessage("Twos: %d, Threes: %d, Checksum: %d", twos, threes, twos * threes);

        // ********* Part 2
        char[] id1, id2;
        int mismatches = 0, firstMistmatchPos = 0;
        boolean found = false;

        for (int i = 0; i < input.size() - 1; i++) {
            // Much faster to compare two arrays than repeatedly call String.charAt()
            id1 = input.get(i).toCharArray();

            for (int j = i + 1; j < input.size(); j++) {
                // Count the number of letters not matching between the two strings
                mismatches = 0;
                id2 = input.get(j).toCharArray();
                if (id1.length != id2.length) {
                    continue;
                }

                for (int ci = 0; ci < id1.length; ci++) {
                    if (id1[ci] != id2[ci]) {
                        mismatches++;
                        firstMistmatchPos = ci;
                    }
                }

                if (mismatches > 1) {
                    continue;
                }

                // Remove the mismatched char to get our answer
                String s1 = input.get(i);
                String answer = "";
                if (firstMistmatchPos > 0) {
                    answer = s1.substring(0, firstMistmatchPos);
                }
                if (firstMistmatchPos < s1.length() - 2) {
                    answer += s1.substring(firstMistmatchPos + 1, s1.length());
                }
                Log.logInfoMessage("First ID: %s\nSecond ID: %s\nCommon Letters: %s", s1, input.get(j), answer);

                // No point in searching further after an answer has been found
                found = true;
                break;
            }

            if (found) {
                break;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Day002().main();
    }
}
