package com.catalinionescu.adventofcode.y2018;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.catalinionescu.adventofcode.common.Log;

public class Day001 {
    public static String INPUT_URL = "https://adventofcode.com/2018/day/1/input";
    public static String INPUT_FILE = "inputs/input001.txt";

    private void main() throws IOException {
        Collection<String> input = Files.readAllLines(Paths.get(INPUT_FILE));

        if (input == null || input.isEmpty()) {
            Log.logErrorMessage("Input data set is empty!");
            return;
        }

        int frequency = 0;
        Integer value;

        // Part 1
        for (String line : input) {
            value = Integer.parseInt(line);
            frequency += value.intValue();
        }

        Log.logInfoMessage("Final frequency is: %d", frequency);

        // Part 2
        frequency = 0;

        Set<Integer> seenFrequencies = new HashSet<>();
        boolean foundDuplicate = false;

        // Add first frequency (!)
        seenFrequencies.add(0);

        while (!foundDuplicate) {
            for (String line : input) {
                value = Integer.parseInt(line);
                frequency += value.intValue();

                if (seenFrequencies.contains(frequency)) {
                    Log.logInfoMessage("First duplicate frequency: %d", frequency);
                    foundDuplicate = true;
                    break;
                }
                seenFrequencies.add(frequency);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Day001().main();
    }
}
