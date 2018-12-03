package com.catalinionescu.adventofcode.y2018;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

import com.catalinionescu.adventofcode.common.Log;

public class Day003 {
    public static String INPUT_URL = "https://adventofcode.com/2018/day/3/input";
    public static String INPUT_FILE = "inputs/input003.txt";

    class Claim {
        private int x, y;
        private int width, height;
        private String id;

        public Claim(String rawClaim) {
            // #1 @ 551,185: 21x10
            String[] parts = rawClaim.split(" ");
            String[] startS = parts[2].substring(0, parts[2].length() - 1).split(",");
            String[] sizeS = parts[3].split("x");
            x = Integer.parseInt(startS[0]);
            y = Integer.parseInt(startS[1]);
            width = Integer.parseInt(sizeS[0]);
            height = Integer.parseInt(sizeS[1]);
            id = parts[0];
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public String getId() {
            return id;
        }

        @Override
        public String toString() {
            return String.format("%s @ %d,%d: %dx%d", id, x, y, width, height);
        }
    }

    // Once again, because size allows us to we'll use a frequency matrix. We'll increment each inch of the fabric by 1 every time a claim contains it.
    private static int FABRIC_SIZE = 1000;
    int[][] fabric = new int[FABRIC_SIZE][FABRIC_SIZE];

    private void main() throws IOException {
        Collection<String> input = Files.readAllLines(Paths.get(INPUT_FILE));

        if (input == null || input.isEmpty()) {
            Log.logErrorMessage("Input data set is empty!");
            return;
        }

        Collection<Claim> claims = new ArrayList<>();

        // ********* Part 1
        for (String line : input) {
            Claim claim = new Claim(line);
            claims.add(claim);

            for (int i = claim.getX(); i < claim.getX() + claim.getWidth(); i++) {
                for (int j = claim.getY(); j < claim.getY() + claim.getHeight(); j++) {
                    fabric[i][j]++;
                }
            }
        }

        // All the parts of the fabric where 2 or more claims overlap will have a frequency of 2 or more.
        int sum = 0;
        for (int i = 0; i < FABRIC_SIZE; i++) {
            for (int j = 0; j < FABRIC_SIZE; j++) {
                if (fabric[i][j] > 1) {
                    sum++;
                }
            }
        }

        Log.logInfoMessage("Sq. Inches: %d", sum);

        // ********* Part 2
        boolean found = false;
        for (Claim claim : claims) {
            found = true;

            for (int i = claim.getX(); i < claim.getX() + claim.getWidth(); i++) {
                for (int j = claim.getY(); j < claim.getY() + claim.getHeight(); j++) {
                    // If any square inch of this claim's fabric has a frequency of 2 or more it means it's overlapping with another claim
                    if (fabric[i][j] != 1) {
                        found = false;
                        break;
                    }
                }

                if (!found) {
                    break;
                }
            }

            if (found) {
                Log.logInfoMessage("ID of the claim that doesn't overlap: %s", claim.getId());
                break;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Day003().main();
    }
}
