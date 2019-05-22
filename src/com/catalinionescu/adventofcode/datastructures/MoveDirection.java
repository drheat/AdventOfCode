package com.catalinionescu.adventofcode.datastructures;

/**
 * Encodes move direction. Note the order directions are defined is important to have compareTo() consistent with natural reading order (up, then left, then
 * right, and finally down).
 * 
 * @author Catalin Ionescu
 *
 */
public enum MoveDirection {
    UP('^'),
    LEFT('<'),
    RIGHT('>'),
    DOWN('v');

    private final char ch;

    MoveDirection(char ch) {
        this.ch = ch;
    }

    public char getCh() {
        return ch;
    }
}