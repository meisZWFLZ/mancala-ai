package com.mancala;

/**
 * Should be enum
 */
public class Player {
    private final boolean id;

    private Player(final boolean id) {
        this.id = id;
    }

    public static Player MIN = new Player(false);
    public static Player MAX = new Player(true);

    public static Player opponent(Player player) {
        return player.isMax() ? Player.MIN : Player.MAX;
    }

    public boolean isMin() {
        return this.id == Player.MIN.id;
    }

    public boolean isMax() {
        return this.id == Player.MAX.id;
    }

    @Override
    public String toString() {
        return isMax() ? "MAX" : "MIN";
    }
}
