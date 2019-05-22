package com.catalinionescu.adventofcode.y2018.day015;

public class Creature implements Comparable<Creature> {
    private final int uuid;
    private CreatureType type;
    private int health;
    private int attackPower;
    private int x;
    private int y;

    private static int uuidGen = 0;

    public Creature(CreatureType type, int initialHealth, int attackPower) {
        this.type = type;
        health = initialHealth;
        this.attackPower = attackPower;
        uuid = uuidGen++;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public CreatureType getType() {
        return type;
    }

    public int getHealth() {
        return health;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public void attack(Creature foe) {
        foe.health -= attackPower;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public int getUuid() {
        return uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Creature)) {
            return false;
        }

        return uuid == ((Creature) o).uuid;
    }

    @Override
    public int hashCode() {
        return uuid;
    }

    @Override
    public int compareTo(Creature o) {
        if (y < o.y) {
            return -1;
        } else if (y > o.y) {
            return 1;
        }

        if (x < o.x) {
            return -1;
        } else if (x > o.x) {
            return 1;
        }

        return 0;
    }
}