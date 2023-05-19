package com.mancala;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;


public class GameStateTree {
    private final GameStateTree.Node root;
    private int depth = 0;

    public GameStateTree(GameState rootState) {
        root = new Node(rootState, 0, null);
    }

    public int getDepth() {
        return depth;
    }

    public Node getRoot() {
        return root;
    }

    public int estimateValue(final int maxDepth) {
//        root.populateTo(maxDepth);
        return root.calculateValue();
    }

    static class Node {
        private final GameState state;
        private Map<Action, Node> children;
        public final int depth;
        private boolean childrenPopulated = false;
        public final Node parent;

        public void populateChildren() {
            children = new HashMap<>();
            Action[] actions = state.getActions();

            for (Action act : actions)
                children.put(act, new Node(state.performAction(act), depth + 1, this));

            childrenPopulated = true;
        }

        public Node(GameState state, int depth, Node parent) {
            this.parent = parent;
            this.depth = depth;
            this.state = state;
        }

        public GameState getState() {
            return state;
        }

        public Node getChild(Action act) {
            return children.get(act);
        }

        public int getChildCount() {
            return children.size();
        }

        public boolean isRoot() {
            return parent == null;
        }

        public boolean isTerminal() {
            return state.terminal();
        }

        public boolean isPopulated() {
            return childrenPopulated;
        }

        public Collection<Node> getChildrenList() {
            return children.values();
        }

        public Map<Action, Node> getChildrenMap() {
            return children;
        }

        /**
         * @return whether all descendants are populated
         */
        public boolean isFullyPopulated() {
            return isPopulated() && (isTerminal() || children.values().stream().allMatch(Node::isFullyPopulated));
        }

        public int calculateValue() {
            if (isPopulated())
                if (!isTerminal()) {
                    IntStream stream = children.values().stream().mapToInt(Node::calculateValue);
                    if (state.turn.isMin())
                        return stream.min().getAsInt();
                    else return stream.max().getAsInt();
                }
            return getEstimatedValue();
        }
//        public int calculateValueTo(int maxDepth) {
//            if (maxDepth <= depth) return getEstimatedValue();
//
//
//        }

        public int getEstimatedValue() {
            return Evaluation.evaluateState(state, new Action[]{}).value;
        }

        public void populateTo(final int maxDepth) {
            if (maxDepth <= depth) return;
            if (this.isFullyPopulated()) return;
            if (!this.isPopulated()) this.populateChildren();

            this.getChildrenList().forEach(n -> n.populateTo(maxDepth));
        }

    }
}
