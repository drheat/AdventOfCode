package com.catalinionescu.adventofcode.y2018.day015;

import java.util.HashMap;
import java.util.Map;

public class ConverterHelpers {
    private static Map<Character, TerrainType> terrainTypes = new HashMap<>();
    static {
        for (TerrainType type : TerrainType.values()) {
            terrainTypes.put(type.getCh(), type);
        }
    }

    /**
     * Converts input character into it's associated terrain type.
     * 
     * @param ch Input character
     * @return Terrain type
     */
    public static TerrainType charToTerrainType(char ch) {
        return terrainTypes.get(ch);
    }

    /**
     * Returns true if input character is terrain.
     * 
     * @param ch Input character
     * @return
     */
    public static boolean isTerrain(char ch) {
        return terrainTypes.containsKey(ch);
    }

    private static Map<Character, CreatureType> creatureTypes = new HashMap<>();
    static {
        for (CreatureType type : CreatureType.values()) {
            creatureTypes.put(type.getCh(), type);
        }
    }

    /**
     * Converts input character into it's associated creature type.
     * 
     * @param ch Input character
     * @return Creature type
     */
    public static CreatureType charToCreatureType(char ch) {
        return creatureTypes.get(ch);
    }

    /**
     * Returns true if input character is a creature.
     * 
     * @param ch Input character
     * @return
     */
    public static boolean isCreature(char ch) {
        return creatureTypes.containsKey(ch);
    }
}
