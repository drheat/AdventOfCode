package com.catalinionescu.adventofcode.y2018.day015;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.catalinionescu.adventofcode.datastructures.MoveDirection;

public class Path implements Comparable<Path> {
    private List<MoveDirection> path = new LinkedList<>();

    public int getCost() {
        return path.size();
    }

    public static Path buildPath(TerrainMap map, int sourceX, int sourceY, int targetX, int targetY) {
        return new Path();
    }

    @Override
    public int compareTo(Path o) {
        if (path.size() != o.path.size()) {
            return path.size() - o.path.size();
        }

        Iterator<MoveDirection> i1 = path.iterator();
        Iterator<MoveDirection> i2 = o.path.iterator();
        int result = -1;
        while (i1.hasNext() && i2.hasNext() && result == 0) {
            MoveDirection d1 = i1.next();
            MoveDirection d2 = i2.next();
            result = d1.compareTo(d2);
        }

        return result;
    }
}