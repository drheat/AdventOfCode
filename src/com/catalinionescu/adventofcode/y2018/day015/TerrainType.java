package com.catalinionescu.adventofcode.y2018.day015;

public enum TerrainType {
    WALL('#'),
    OPEN('.');

    private final char ch;

    TerrainType(char ch) {
        this.ch = ch;
    }

    public char getCh() {
        return ch;
    }
}