package com.catalinionescu.adventofcode.y2018.day015;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.catalinionescu.adventofcode.common.Log;

public class Day015 {
    public static String INPUT_URL = "https://adventofcode.com/2018/day/15/input";
    public static String INPUT_FILE = "inputs/input015.txt";

    private static int ATTACK_POWER = 3;
    private static int INITIAL_HEALTH = 200;

    private void main() throws IOException {
        List<String> input = Files.readAllLines(Paths.get(INPUT_FILE));

        if (input == null || input.isEmpty()) {
            Log.logErrorMessage("Input data set is empty!");
            return;
        }

        int maxX = input.get(0).trim().length();
        int maxY = input.size();

        Log.logInfoMessage("Terrain size: x - %d, y - %d", maxX, maxY);

        TerrainMap map = new TerrainMap(maxX, maxY);
        Set<Creature> creatures = new TreeSet<>();

        int x = 0;
        int y = 0;
        int elvesCount = 0;
        int goblinsCount = 0;
        // Parse the initial map
        for (String line : input) {
            char ch[] = line.toCharArray();
            for (x = 0; x < ch.length; x++) {
                if (ConverterHelpers.isCreature(ch[x])) {
                    map.set(x, y, new TerrainCell(TerrainType.OPEN));
                    Creature creature = new Creature(ConverterHelpers.charToCreatureType(ch[x]), INITIAL_HEALTH, ATTACK_POWER);
                    creature.setPosition(x, y);
                    creatures.add(creature);
                    if (creature.getType() == CreatureType.ELF) {
                        elvesCount++;
                    } else {
                        goblinsCount++;
                    }
                }
            }
        }

        Log.logInfoMessage("Elves: %d, Goblins: %d", elvesCount, goblinsCount);
        // Combat time!
        while (elvesCount > 0 && goblinsCount > 0) {
            for (Creature creature : creatures) {
                if (creature.isDead()) {
                    continue;
                }

                // Get any foes in combat range
                Creature foe = creature.inAttackRange(creatures);
                if (foe != null) {
                    creature.attack(foe);
                    continue;
                }

                // Move to closest foe
                creature.moveToClosestFoe(creatures);
            }

            // Cleanup dead creatures and count the living based off their type
            Iterator<Creature> iter = creatures.iterator();
            elvesCount = 0;
            goblinsCount = 0;
            while (iter.hasNext()) {
                Creature creature = iter.next();
                if (creature.isDead()) {
                    iter.remove();
                } else {
                    switch (creature.getType()) {
                        case ELF:
                            elvesCount++;
                            break;
                        case GOBLIN:
                            goblinsCount++;
                            break;
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Day015().main();
    }
}
