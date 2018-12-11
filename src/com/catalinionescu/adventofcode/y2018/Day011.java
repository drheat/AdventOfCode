package com.catalinionescu.adventofcode.y2018;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.catalinionescu.adventofcode.common.Log;

public class Day011 {
    public static String INPUT_URL = "https://adventofcode.com/2018/day/11/input";
    public static String INPUT_FILE = "inputs/input011.txt";

    private static int GRID_SIZE = 300;

    public int powerLevel(int serial, int x, int y) {
        int rackId = x + 10;
        int powerLevel = rackId * y;
        powerLevel += serial;
        powerLevel *= rackId;

        int digit;

        if (powerLevel < 100) {
            digit = 0;
        } else {
            // Quick and dirty hundreds extraction. Marginally faster than modulus.
            char digits[] = String.valueOf(powerLevel).toCharArray();
            digit = digits[digits.length - 3] - '0';
        }

        digit -= 5;

        return digit;
    }

    public int gridSum(int grid[][], int topLeftX, int topLeftY, int size) {
        int result = 0;

        for (int y = topLeftY + size - 1; y >= topLeftY; y--) {
            for (int x = topLeftX + size - 1; x >= topLeftX; x--) {
                result += grid[x][y];
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

        int serial = Integer.parseInt(input.get(0));

        int grid[][] = new int[GRID_SIZE][GRID_SIZE];
        for (int y = 0; y < GRID_SIZE; y++) {
            for (int x = 0; x < GRID_SIZE; x++) {
                grid[x][y] = powerLevel(serial, x, y);
            }
        }

        int maxSum = Integer.MIN_VALUE;
        int cornerX = 0;
        int cornerY = 0;
        int sqSize = 0;
        int sum;
        for (int size = 1; size <= GRID_SIZE; size++) {
            for (int y = 0; y < GRID_SIZE - size; y++) {
                for (int x = 0; x < GRID_SIZE - size; x++) {
                    sum = gridSum(grid, x, y, size);
                    if (sum > maxSum) {
                        maxSum = sum;
                        cornerX = x;
                        cornerY = y;
                        sqSize = size;
                    }
                }
            }
        }

        Log.logInfoMessage("x: %d, y: %d, size: %d", cornerX, cornerY, sqSize);
    }

    public static void main(String[] args) throws IOException {
        new Day011().main();
    }

}
