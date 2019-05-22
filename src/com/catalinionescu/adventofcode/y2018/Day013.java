package com.catalinionescu.adventofcode.y2018;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.catalinionescu.adventofcode.common.Log;
import com.catalinionescu.adventofcode.datastructures.MoveDirection;

public class Day013 {
    public static String INPUT_URL = "https://adventofcode.com/2018/day/13/input";
    public static String INPUT_FILE = "inputs/input013.txt";

    enum TrackType {
        VERT('|'),
        HORIZ('-'),
        JUNCTION('+'),
        CURVE_LEFT('\\'),
        CURVE_RIGHT('/');

        private final char ch;

        TrackType(char ch) {
            this.ch = ch;
        }

        public char getCh() {
            return ch;
        }
    }

    private static Map<Character, TrackType> trackTypes = new HashMap<>();
    static {
        for (TrackType type : TrackType.values()) {
            trackTypes.put(type.getCh(), type);
        }
    }

    /**
     * Converts input character into it's associated track type.
     * 
     * @param ch Input character
     * @return Track type
     */
    public static TrackType charToTrackType(char ch) {
        return trackTypes.get(ch);
    }

    /**
     * Returns true if input character is a track.
     * 
     * @param ch Input character
     * @return
     */
    public static boolean isTrackType(char ch) {
        return trackTypes.containsKey(ch);
    }

    private static Map<Character, MoveDirection> moveDirections = new HashMap<>();
    static {
        for (MoveDirection dir : MoveDirection.values()) {
            moveDirections.put(dir.getCh(), dir);
        }
    }

    /**
     * Converts a cart character to the cart initial move direction.
     * 
     * @param ch Cart character
     * @return Initial move direction
     */
    public static MoveDirection charToMoveDirection(char ch) {
        return moveDirections.get(ch);
    }

    /**
     * Returns true if given character is a cart.
     * 
     * @param ch Character
     * @return
     */
    public static boolean isCart(char ch) {
        return moveDirections.containsKey(ch);
    }

    /**
     * Returns the track type under given input cart.
     * 
     * @param ch Input cart
     * @return Track type
     */
    public static TrackType cartToTrackType(char ch) {
        if (ch == '>' || ch == '<') {
            return TrackType.HORIZ;
        }

        return TrackType.VERT;
    }

    enum TurnOptions {
        LEFT,
        STRAIGHT,
        RIGHT;
    }

    TurnOptions cartTurnOptions[] = { TurnOptions.LEFT, TurnOptions.STRAIGHT, TurnOptions.RIGHT };

    static class UniqueCartId {
        private static int uuid = 0;

        public static int getUUID() {
            return uuid++;
        }
    }

    class Cart {
        private int uuid;
        private int turnOptionIdx = 0;
        private MoveDirection direction;

        public Cart(MoveDirection direction) {
            this.direction = direction;
            uuid = UniqueCartId.getUUID();
        }

        /**
         * Returns current cart move direction.
         * 
         * @return
         */
        public MoveDirection getMoveDirection() {
            return direction;
        }

        /**
         * Updates cart move direction based off the old direction and the track type under then cart.
         * 
         * @param track Track type under the cart
         */
        public void updateMoveDirection(TrackType track) {
            switch (track) {
                case CURVE_LEFT:
                    // \
                    switch (direction) {
                        case DOWN:
                            direction = MoveDirection.RIGHT;
                            break;
                        case LEFT:
                            direction = MoveDirection.UP;
                            break;
                        case RIGHT:
                            direction = MoveDirection.DOWN;
                            break;
                        case UP:
                            direction = MoveDirection.LEFT;
                            break;
                    }
                    break;
                case CURVE_RIGHT:
                    // /
                    switch (direction) {
                        case DOWN:
                            direction = MoveDirection.LEFT;
                            break;
                        case LEFT:
                            direction = MoveDirection.DOWN;
                            break;
                        case RIGHT:
                            direction = MoveDirection.UP;
                            break;
                        case UP:
                            direction = MoveDirection.RIGHT;
                            break;
                    }
                    break;
                case JUNCTION:
                    turn();
                    break;
                case HORIZ:
                case VERT:
                    // keep going in same direction
                    break;
            }
        }

        /**
         * Turns the cart at a junction.
         * 
         * @return New direction cart is moving
         */
        public MoveDirection turn() {
            switch (cartTurnOptions[turnOptionIdx]) {
                case LEFT:
                    switch (direction) {
                        case DOWN:
                            direction = MoveDirection.RIGHT;
                            break;
                        case LEFT:
                            direction = MoveDirection.DOWN;
                            break;
                        case RIGHT:
                            direction = MoveDirection.UP;
                            break;
                        case UP:
                            direction = MoveDirection.LEFT;
                            break;
                    }
                    break;
                case RIGHT:
                    switch (direction) {
                        case DOWN:
                            direction = MoveDirection.LEFT;
                            break;
                        case LEFT:
                            direction = MoveDirection.UP;
                            break;
                        case RIGHT:
                            direction = MoveDirection.DOWN;
                            break;
                        case UP:
                            direction = MoveDirection.RIGHT;
                            break;
                    }
                    break;
                case STRAIGHT:
                    // continues on same direction
                    break;
            }
            turnOptionIdx = (turnOptionIdx + 1) % cartTurnOptions.length;

            return direction;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Cart)) {
                return false;
            }

            return ((Cart) o).uuid == uuid;
        }

        @Override
        public int hashCode() {
            return uuid;
        }
    }

    class Track {
        private final TrackType type;
        private Cart cart = null;
        private boolean hasCrash = false;

        public Track(TrackType type) {
            this.type = type;
        }

        public TrackType getType() {
            return type;
        }

        public boolean hasCart() {
            return cart != null;
        }

        public void setCart(Cart cart) {
            this.cart = cart;
        }

        public Cart getCart() {
            return cart;
        }

        public void clearCart() {
            cart = null;
        }

        public boolean hasCrash() {
            return hasCrash;
        }

        public void setCrash() {
            hasCrash = true;
        }

        public void clearCrash() {
            hasCrash = false;
        }
    }

    public void printTrackState(Track tracks[][], int maxX, int maxY) {
        for (int y = 0; y < maxY; y++) {
            StringBuffer sb = new StringBuffer();
            for (int x = 0; x < maxX; x++) {
                if (tracks[x][y] == null) {
                    sb.append(' ');
                } else if (tracks[x][y].hasCart()) {
                    sb.append(tracks[x][y].getCart().getMoveDirection().getCh());
                } else {
                    sb.append(tracks[x][y].getType().getCh());
                }
            }
            Log.logInfoMessage("%s", sb.toString());
        }
    }

    private void main() throws IOException {
        List<String> input = Files.readAllLines(Paths.get(INPUT_FILE));

        if (input == null || input.isEmpty()) {
            Log.logErrorMessage("Input data set is empty!");
            return;
        }

        int maxX = 0;
        int maxY = 0;
        int length;
        for (String line : input) {
            length = line.trim().length();
            if (length > maxX) {
                maxX = length;
            }
            if (length > 0) {
                maxY++;
            }
        }

        Track tracks[][] = new Track[maxX][maxY];
        Log.logInfoMessage("Track dimensions: x - %d, y - %d", maxX, maxY);
        int x = 0;
        int y = 0;
        for (String line : input) {
            char ch[] = line.toCharArray();
            for (x = 0; x < ch.length; x++) {
                if (isCart(ch[x])) {
                    tracks[x][y] = new Track(cartToTrackType(ch[x]));
                    tracks[x][y].setCart(new Cart(charToMoveDirection(ch[x])));
                } else if (isTrackType(ch[x])) {
                    tracks[x][y] = new Track(charToTrackType(ch[x]));
                } else {
                    tracks[x][y] = null;
                }
            }
            y++;
        }

        boolean firstCrashFound = false;
        int crashX = 0;
        int crashY = 0;
        Cart cart;
        int newX, newY;
        int lastCartX = 0;
        int lastCartY = 0;
        Set<Cart> tickedCarts = new HashSet<>();
        while (tickedCarts.size() != 1) {
            // printTrackState(tracks, maxX, maxY);
            tickedCarts.clear();
            for (y = 0; y < maxY; y++) {
                for (x = 0; x < maxX; x++) {
                    if (tracks[x][y] == null || !tracks[x][y].hasCart()) {
                        continue;
                    }

                    cart = tracks[x][y].getCart();
                    if (tickedCarts.contains(cart)) {
                        continue;
                    }

                    // Clear cart from source position
                    tracks[x][y].clearCart();
                    cart.updateMoveDirection(tracks[x][y].getType());
                    newX = x;
                    newY = y;
                    switch (cart.getMoveDirection()) {
                        case DOWN:
                            newY++;
                            break;
                        case LEFT:
                            newX--;
                            break;
                        case RIGHT:
                            newX++;
                            break;
                        case UP:
                            newY--;
                            break;
                    }

                    if (tracks[newX][newY].hasCart()) {
                        // Part 1 answer found
                        if (!firstCrashFound) {
                            crashX = newX;
                            crashY = newY;
                        }
                        firstCrashFound = true;

                        // Clear the cart our current cart has collided with.
                        tracks[newX][newY].clearCart();
                    } else {
                        // No crash. Update the cart target position at the end of this tick.
                        tracks[newX][newY].setCart(cart);
                        lastCartX = newX;
                        lastCartY = newY;
                        tickedCarts.add(cart);
                    }
                }
            }
        }

        Log.logInfoMessage("First crash coordinates: %d,%d", crashX, crashY);
        Log.logInfoMessage("Last cart coordinates: %d,%d", lastCartX, lastCartY);
    }

    public static void main(String[] args) throws IOException {
        new Day013().main();
    }
}
