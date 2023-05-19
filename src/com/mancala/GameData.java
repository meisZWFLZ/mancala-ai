package com.mancala;

public class GameData {
    public int[] minPiles;
    public int[] maxPiles;
    public int   minStore;
    public int   maxStore;

    public Player turn;

    GameData(int[] minMarbles, int[] maxMarbles, int minStore, int maxStore, Player turn) {
        this.minPiles = minMarbles;
        this.maxPiles = maxMarbles;
        this.minStore = minStore;
        this.maxStore = maxStore;
        this.turn = turn;
    }

    /**
     * gets current player's piles
     */
    public int[] getCurrentPiles() {
        return this.getPiles(turn);
    }

    /**
     * gets the ith pile of the current player's piles
     */
    public int getCurrentPile(int i) {
        return getPile(turn, i);
    }

    public int[] getPiles(Player player) {
        return player.isMin() ? minPiles : maxPiles;
    }


    public int getPile(Player player, int i) {
        return getPiles(player)[i];
    }

    public int setPile(Player player, int i, int pile) {
        return (player.isMin() ? minPiles : maxPiles)[i] = pile;
    }

    /**
     * gets current player's store
     */
    public int getCurrentStore() {
        return getStore(turn);
    }

    public int getStore(Player player) {
        return player.isMin() ? minStore : maxStore;
    }

    public int getHole(Hole hole) {
        if (hole.index == Hole.MIN_STORE.index) return this.minStore;
        if (hole.index == Hole.MAX_STORE.index) return this.maxStore;

        if (Hole.isOwnedBy(hole, Player.MIN)) return getPile(Player.MIN, hole.index);

        return getPile(Player.MAX, hole.index - 7);
    }

    public int setHole(Hole hole, final int pile) {
        if (hole.index == Hole.MIN_STORE.index) return this.minStore = pile;
        if (hole.index == Hole.MAX_STORE.index) return this.maxStore = pile;

        if (Hole.isOwnedBy(hole, Player.MIN)) return setPile(Player.MIN, hole.index, pile);
        return setPile(Player.MAX, hole.index - 7, pile);
    }

    public void incrementHole(Hole hole) {
        setHole(hole, getHole(hole) + 1);
    }

    public int sideTotal(Player player) {
        int total = 0;
        for (int pile : this.getPiles(player)) {
            total += pile;
        }
        return total;
    }

    static GameData fromData(GameData data) {
        return new GameData(data.minPiles.clone(), data.maxPiles.clone(), data.minStore, data.maxStore, data.turn);
    }
}
