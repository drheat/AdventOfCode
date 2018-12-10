package com.catalinionescu.adventofcode.y2018;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.catalinionescu.adventofcode.common.Log;

public class Day010 {
    public static String INPUT_URL = "https://adventofcode.com/2018/day/10/input";
    public static String INPUT_FILE = "inputs/input010.txt";

    class Point {
        private int x;
        private int y;
        private final int vx;
        private final int vy;

        public Point(String raw) {
            // position=< 52484, -20780> velocity=<-5, 2>
            // position=<-52068, 31483> velocity=< 5, -3>
            String parts[] = raw.split("<");
            String posParts[] = parts[1].substring(0, parts[1].indexOf('>')).split(",");
            x = Integer.parseInt(posParts[0].trim());
            y = Integer.parseInt(posParts[1].trim());
            String velParts[] = parts[2].substring(0, parts[2].length() - 1).split(",");
            vx = Integer.parseInt(velParts[0].trim());
            vy = Integer.parseInt(velParts[1].trim());
        }

        public void tick() {
            x += vx;
            y += vy;
        }

        public void reverseTick() {
            x -= vx;
            y -= vy;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public boolean atCoords(int x, int y) {
            return this.x == x && this.y == y;
        }
    }

    private void main() throws IOException {
        List<String> input = Files.readAllLines(Paths.get(INPUT_FILE));

        if (input == null || input.isEmpty()) {
            Log.logErrorMessage("Input data set is empty!");
            return;
        }

        List<Point> points = new ArrayList<>();
        for (String line : input) {
            points.add(new Point(line));
        }

        int minX = Integer.MIN_VALUE, maxX = Integer.MAX_VALUE, minY = Integer.MIN_VALUE, maxY = Integer.MAX_VALUE;
        int xDiff = Integer.MAX_VALUE, yDiff = Integer.MAX_VALUE;
        int seconds = 0;
        boolean first = true;

        // To have the lights spell out something, points have to be close together. We warp time while both dimensions keep decreasing and stop when we
        // detect an increase on either X or Y coordinate.
        do {
            if (first) {
                first = false;
            } else {
                xDiff = maxX - minX;
                yDiff = maxY - minY;
            }

            minX = Integer.MAX_VALUE;
            maxX = Integer.MIN_VALUE;
            minY = Integer.MAX_VALUE;
            maxY = Integer.MIN_VALUE;

            for (Point point : points) {
                point.tick();

                if (point.getX() < minX) {
                    minX = point.getX();
                }
                if (point.getX() > maxX) {
                    maxX = point.getX();
                }

                if (point.getY() < minY) {
                    minY = point.getY();
                }
                if (point.getY() > maxY) {
                    maxY = point.getY();
                }
            }
            seconds++;
        } while ((maxX - minX) < xDiff && (maxY - minY) < yDiff);

        // Since we detected an increase on either X or Y axis, we're 1 step too far. So we back off 1 step to get the message.
        minX = Integer.MAX_VALUE;
        maxX = Integer.MIN_VALUE;
        minY = Integer.MAX_VALUE;
        maxY = Integer.MIN_VALUE;
        for (Point point : points) {
            if (point.getX() < minX) {
                minX = point.getX();
            }
            if (point.getX() > maxX) {
                maxX = point.getX();
            }

            if (point.getY() < minY) {
                minY = point.getY();
            }
            if (point.getY() > maxY) {
                maxY = point.getY();
            }

            point.reverseTick();
        }
        seconds--;

        // Display the message
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                boolean found = false;
                for (Point point : points) {
                    if (point.atCoords(x, y)) {
                        found = true;
                        break;
                    }
                }

                if (found) {
                    System.out.print("#");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println("");
        }

        // Part 2 -- display the seconds we warped
        System.out.println(String.format("Seconds: %d", seconds));
    }

    public static void main(String[] args) throws IOException {
        new Day010().main();
    }
}
