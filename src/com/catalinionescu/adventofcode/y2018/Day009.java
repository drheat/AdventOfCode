package com.catalinionescu.adventofcode.y2018;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.catalinionescu.adventofcode.common.Log;

public class Day009 {
    public static String INPUT_URL = "https://adventofcode.com/2018/day/9/input";
    public static String INPUT_FILE = "inputs/input009.txt";

    class Node {
        int value;
        Node next;
        Node prev;

        public Node(Node prev, int value, Node next) {
            this.value = value;
            if (next == null) {
                next = this;
            }
            this.next = next;
            if (prev == null) {
                prev = this;
            }
            this.prev = prev;
        }
    }

    /**
     * Adds a node 2 places to the right of the current node.
     * 
     * @param current Current node
     * @param value Value of the new node
     * @return The new node
     */
    private Node addAfter(Node current, int value) {
        Node newNode = new Node(current.next, value, current.next.next);
        current.next.next.prev = newNode;
        current.next.next = newNode;
        return newNode;
    }

    /**
     * Removes the node 7 places to the left of the current node.
     * 
     * @param current Current node
     * @return Node that was removed
     */
    private Node removeBefore(Node current) {
        for (int i = 0; i < 7; i++) {
            current = current.prev;
        }

        current.prev.next = current.next;
        current.next.prev = current.prev;

        return current;
    }

    private void main() throws IOException {
        List<String> input = Files.readAllLines(Paths.get(INPUT_FILE));

        if (input == null || input.isEmpty()) {
            Log.logErrorMessage("Input data set is empty!");
            return;
        }

        String parts[] = input.get(0).split(" ");
        int playerCount = Integer.parseInt(parts[0]);
        int configRounds = Integer.parseInt(parts[6]);

        Log.logInfoMessage("Players: %d Rounds: %d", playerCount, configRounds);

        int bothPartRounds[] = { configRounds, configRounds * 100 };
        int part = 1;
        for (int round : bothPartRounds) {
            long elvesScores[] = new long[playerCount];

            Node current = new Node(null, 0, null);
            int currentPlayer;
            for (int i = 1; i <= round; i++) {
                if (i % 23 == 0) {
                    currentPlayer = i % playerCount;
                    elvesScores[currentPlayer] += i;
                    current = removeBefore(current);
                    elvesScores[currentPlayer] += current.value;
                    current = current.next;
                } else {
                    current = addAfter(current, i);
                }
            }

            long highestScore = 0;
            for (int i = 0; i < elvesScores.length; i++) {
                if (elvesScores[i] > highestScore) {
                    highestScore = elvesScores[i];
                }
            }

            Log.logInfoMessage("High score for part %d is: %d", part, highestScore);
            part++;
        }
    }

    public static void main(String[] args) throws IOException {
        new Day009().main();
    }
}
