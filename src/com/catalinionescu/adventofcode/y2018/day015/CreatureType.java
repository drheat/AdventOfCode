package com.catalinionescu.adventofcode.y2018.day015;

public enum CreatureType {
    ELF('E'),
    GOBLIN('G');

    private final char ch;

    CreatureType(char ch) {
        this.ch = ch;
    }

    public char getCh() {
        return ch;
    }
}