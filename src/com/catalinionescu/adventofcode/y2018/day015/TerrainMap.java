package com.catalinionescu.adventofcode.y2018.day015;

public class TerrainMap {
    private final TerrainCell terrain[][];
    private final int width;
    private final int height;

    public TerrainMap(int width, int height) {
        this.width = width;
        this.height = height;
        terrain = new TerrainCell[width][height];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public TerrainCell get(int x, int y) {
        return terrain[x][y];
    }

    public void set(int x, int y, TerrainCell value) {
        terrain[x][y] = value;
    }
}