package com.mancala;

import java.util.ArrayList;
import java.util.Arrays;

public class GameState extends GameData {
    final Configuration config;

    public GameState(int[] minMarbles, int[] maxMarbles, int minStore, int maxStore, Player turn) {
        super(minMarbles, maxMarbles, minStore, maxStore, turn);
        config = new Configuration.Static(minMarbles.length, -1);
    }

    public GameState(Configuration config) {
        super(config.piles(), config.piles(), 0, 0, Player.MIN);
        this.config = config;
    }

    public Action[] getActions() {
        ArrayList<Action> actions = new ArrayList<Action>();
        for (int i = 0; i < getCurrentPiles().length; i++)
            if (getCurrentPile(i) > 0) actions.add(new Action(i));
        return actions.toArray(new Action[]{});
    }

    public GameState performAction(Action act) {
        int      holding = getCurrentPile(act.pile);
        GameData data    = GameData.fromData(this);

        Hole hole = Hole.fromPile(act.pile, this.turn);
        data.setHole(hole, 0);
        for (; holding > 0; holding--) {
            hole.increment(this.turn);
            data.incrementHole(hole);
        }
        if (!Hole.isStore(hole)) {
            if (Hole.isOwnedBy(hole, turn) && data.getHole(hole) == 1 && data.getHole(Hole.opposite(hole)) > 0) {
                data.setHole(
                   Hole.store(data.turn),
                   data.getHole(Hole.store(data.turn)) + data.getHole(Hole.opposite(hole)) + 1
                );
                data.setHole(Hole.opposite(hole), 0);
                data.setHole(hole, 0);
            }
            data.turn = Player.opponent(data.turn);
        }
        if (data.sideTotal(Player.MIN) == 0) {
            data.maxStore += data.sideTotal(Player.MAX);
            Arrays.fill(data.maxPiles, 0);
        }
        else if (data.sideTotal(Player.MAX) == 0) {
            data.minStore += data.sideTotal(Player.MIN);
            Arrays.fill(data.minPiles, 0);
        }
        return GameState.fromData(data);
    }

    /**
     * evaluates whether game state is terminal (has ended)
     */
    public boolean terminal() {
        return sideTotal(Player.MAX) == 0 && sideTotal(Player.MIN) == 0;
    }

    public int value() {
        return maxStore - minStore;
    }

    static GameState fromData(GameData data) {
        return new GameState(data.minPiles.clone(), data.maxPiles.clone(), data.minStore, data.maxStore, data.turn);
    }

    public static final GameState START_STATE = new GameState(Configuration.DEFAULT);

    public static GameState nSeedStartState(final int n) {
        return new GameState(new Configuration.Static(6, n));
    }
}
