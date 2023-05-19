package com.mancala;

public class Hole {
    int index;
    final Configuration config;

    Hole(int index) {
        this.index = index;
        this.config = Configuration.DEFAULT;
    }

    Hole(int index, Configuration config) {
        this.index = index;
        this.config = config;
    }

    public static final Hole MIN_STORE = new Hole(6);
    public static final Hole MAX_STORE = new Hole(13);

    public static Hole fromPile(int pileIndex, Player player) {
        return new Hole((player.isMin() ? 0 : 7) + pileIndex);
    }

    public static Hole store(Player player) {
        return player.isMin() ? MIN_STORE : MAX_STORE;
    }

    public void increment(Player turn) {
        this.index = (this.index + 1) % 14;
        if (Hole.isStore(this) && !Hole.isOwnedBy(this, turn)) this.increment(turn);
    }

    public static boolean isOwnedBy(Hole hole, Player player) {
        if (player.isMin()) return hole.index <= MIN_STORE.index && hole.index >= 0;
        else return hole.index > MIN_STORE.index && hole.index <= MAX_STORE.index;
    }

    public static Player owner(Hole hole) {
        return isOwnedBy(hole, Player.MIN) ? Player.MIN : Player.MAX;
    }

    public static boolean isStore(Hole hole) {
        return hole.index == Hole.MAX_STORE.index || hole.index == Hole.MIN_STORE.index;
    }

    public static Hole opposite(Hole hole) {
        return new Hole(
           !Hole.isStore(hole) ? 12 - hole.index : (hole == Hole.MIN_STORE ? Hole.MAX_STORE : Hole.MIN_STORE).index);
    }

    public static class HoleIsStoreException extends Exception {
    }

    /**
     * @return the pile index represented by the hole
     * @throws HoleIsStoreException if hole is a store
     */
    public static int getPileIndex(Hole hole) throws HoleIsStoreException {
        if (Hole.isStore(hole)) throw new HoleIsStoreException();
        return Hole.owner(hole)
                   .isMin() ? hole.index : hole.index - 7;
    }
}
