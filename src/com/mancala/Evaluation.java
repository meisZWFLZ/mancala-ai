package com.mancala;

public class Evaluation {
    int value = 0;
    Action[] actions;

    public Evaluation(int value, Action[] actions) {
        this.value = value;
        this.actions = actions;
    }

    public static Evaluation minimax(GameState state, int depth) {
        return minimax(state, depth, state.turn.isMax() ? Integer.MAX_VALUE : Integer.MIN_VALUE, new Action[depth]);
    }

    /**
     * @param state
     * @param depth how many more branches to explore (decreases with every iteration)
     * @param goal  if value becomes more undesirable than goal, stop
     * @return best actions
     */
    private static Evaluation minimax(GameState state, int depth, int goal, Action[] earlierActions) {
        if (state.terminal() || 0 >= depth) return evaluateState(state, earlierActions);
        final boolean max = state.turn.isMax();
        Evaluation eval = new Evaluation(max ? Integer.MIN_VALUE : Integer.MAX_VALUE, new Action[]{});
        for (Action act : state.getActions()) {
            earlierActions[depth - 1] = act;
            final GameState newState = state.performAction(act);
            final Evaluation newEval = Evaluation.minimax(newState, depth - 1, newState.turn.isMax() != max ? eval.value : goal, earlierActions.clone());
            // if true, value will only grow larger and be more undesirable than goal
            if (max ? newEval.value > goal : newEval.value < goal) return newEval;
            if (max ? newEval.value > eval.value : newEval.value < eval.value) eval = newEval;
        }
        return eval;
    }
    /*
        We are min
        check move A
        -> 6
        check move B
            check max response move B.A
            -> 8
            Greater than Move A
            -> STOP checking move B

     */

    public static Evaluation evaluateState(GameState state, Action[] actions) {
        int adjustment = 0;
        if (!state.terminal()) {
            for (int i = 0; i < 6; i++)
                adjustment += Math.max(state.minPiles[i] - (6 - i), 0);
            for (int i = 0; i < 6; i++)
                adjustment -= Math.max(state.maxPiles[i] - (6 - i), 0);
        }
        return new Evaluation(state.value() + adjustment, actions);
    }

    public static Action[] sortActions(Action[] acts, GameState state) {
        return acts;
    }
}
