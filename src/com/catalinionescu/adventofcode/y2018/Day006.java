package com.catalinionescu.adventofcode.y2018;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.catalinionescu.adventofcode.common.Log;

public class Day006 {
    public static String INPUT_URL = "https://adventofcode.com/2018/day/6/input";
    public static String INPUT_FILE = "inputs/input006.txt";

    private static int MAX_DIST = 10000;

    class Coordinate {
        private final int x;
        private final int y;
        private boolean isInfinite = false;
        private int areaSize = 0;

        public Coordinate(String rawData) {
            String[] split = rawData.split(", ");
            x = Integer.parseInt(split[0]);
            y = Integer.parseInt(split[1]);
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public void setInfinite() {
            isInfinite = true;
        }

        public boolean isInfinite() {
            return isInfinite;
        }

        public void incAreaSize() {
            areaSize++;
        }

        public int getAreaSize() {
            return areaSize;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Coordinate)) {
                return false;
            }

            Coordinate other = (Coordinate) o;
            return other.x == x && other.y == y;
        }

        @Override
        public int hashCode() {
            return (31 + x) * 31 + y;
        }

        /**
         * Calculates the Manhattan distance between this and another coordinate.
         * 
         * @param o Other coordinate
         * @return Manhattan distance between the coordinates
         */
        public int dist(Coordinate o) {
            return dist(o.x, o.y);
        }

        /**
         * Calculates the Manhattan distance between this and another coordinate.
         * 
         * @param x component of the other coordinate
         * @param y component of the other coordinate
         * @return Manhattan distance between the coordinates
         */
        public int dist(int x, int y) {
            return Math.abs(x - this.x) + Math.abs(y - this.y);
        }
    }

    private void main() throws IOException {
        List<String> input = Files.readAllLines(Paths.get(INPUT_FILE));

        if (input == null || input.isEmpty()) {
            Log.logErrorMessage("Input data set is empty!");
            return;
        }

        Set<Coordinate> coords = new HashSet<>();
        for (String line : input) {
            coords.add(new Coordinate(line));
        }

        // Calculate a rectangle that encloses the "used" part of the infinite grid, that is the smallest rectangle that can hold all given coordinates.
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
        for (Coordinate c : coords) {
            if (c.getX() < minX) {
                minX = c.getX();
            }
            if (c.getY() < minY) {
                minY = c.getY();
            }
            if (c.getX() > maxX) {
                maxX = c.getX();
            }
            if (c.getY() > maxY) {
                maxY = c.getY();
            }
        }

        // Expand the rectangle by 1 in all directions, so that no coordinate is on the rectangle border. This will be used later to detect infinite areas.
        minX -= 1;
        minY -= 1;
        maxX += 1;
        maxY += 1;

        // ********* Part 1
        int minMDist, dist;
        Set<Coordinate> closestSet = new HashSet<>();
        Coordinate closest;
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                // Minimal Manhattan distance from (x, y) to whatever coordinate. This determines if (x, y) belongs to a single area or more.
                minMDist = Integer.MAX_VALUE;
                closestSet.clear();
                for (Coordinate c : coords) {
                    dist = c.dist(x, y);
                    if (dist < minMDist) {
                        minMDist = dist;
                        closestSet.clear();
                        closestSet.add(c);
                    } else if (dist == minMDist) {
                        closestSet.add(c);
                    }
                }

                // (x, y) is closest to a single coordinate, means it belongs to that area.
                if (closestSet.size() == 1) {
                    closest = closestSet.iterator().next();

                    // If (x, y) is on the border of our all enclosing rectangle means that coordinate area is infinite.
                    if (x == minX || x == maxX || y == minY || y == maxY) {
                        closest.setInfinite();
                    }
                    closest.incAreaSize();
                }
            }
        }

        int maxAreaSize = 0;
        for (Coordinate c : coords) {
            if (!c.isInfinite() && c.getAreaSize() > maxAreaSize) {
                maxAreaSize = c.getAreaSize();
            }
        }

        Log.logInfoMessage("Size of the largest area that isn't infinite: %d", maxAreaSize);

        // ********* Part 2
        int distSum;
        boolean isGood;
        int regionSize = 0;
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                distSum = 0;
                isGood = true;
                for (Coordinate c : coords) {
                    dist = c.dist(x, y);

                    // Quick exit if a single distance is over our threshold
                    if (dist >= MAX_DIST) {
                        isGood = false;
                        break;
                    }
                    distSum += dist;
                }

                if (isGood && distSum < MAX_DIST) {
                    regionSize++;
                }
            }
        }

        Log.logInfoMessage("Size of the region with total distance less than %d is: %d", MAX_DIST, regionSize);
    }

    public static void main(String[] args) throws IOException {
        new Day006().main();
    }
}
