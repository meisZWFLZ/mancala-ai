package com.mancala;

import java.util.*;

public class Main {

    static <T> T[] reverse(T[] arr) {
        for (int i = 0; i < arr.length / 2; i++) {
            T temp = arr[i];
            arr[i] = arr[arr.length - i - 1];
            arr[arr.length - i - 1] = temp;
        }
        return arr;
    }

    static int[] reverseInt(int[] arr) {
        for (int i = 0; i < arr.length / 2; i++) {
            int temp = arr[i];
            arr[i] = arr[arr.length - i - 1];
            arr[arr.length - i - 1] = temp;
        }
        return arr;
    }

    static void printState(GameState state, Player userPlayer) {
        final Player opponent       = Player.opponent(userPlayer);
        final String       oppStore       = Integer.toString(state.getStore(opponent));
        final String       oppStoreOffset = String.join("", Collections.nCopies(oppStore.length() + 2, " "));

        final String userStore       = Integer.toString(state.getStore(userPlayer));
        final String userStoreOffset = String.join("", Collections.nCopies(userStore.length() + 2, " "));

        System.out.println("   " + (state.turn.isMin() ? "MIN" : "MAX"));
        System.out.println("   " + oppStore + "<-" + Arrays.toString(reverseInt(state.getPiles(opponent)
                                                                                     .clone())) + userStoreOffset +
                           " >:( " + opponent);
        System.out.println(
           "   " + oppStoreOffset + Arrays.toString(state.getPiles(userPlayer)) + "->" + state.getStore(userPlayer) +
           "  :) " + userPlayer);
        System.out.println("   Value: " + Evaluation.evaluateState(state, new Action[0]).value);
    }

    public static GameState performAct(GameState state, Action act) {
//        Action act = evaluation.actions[evaluation.actions.length - 1];
        System.out.println(" Action: " + act.pile);
        return state.performAction(act);
    }

    public static GameState performAct(GameState state, Evaluation evaluation) {
        Action act = evaluation.actions[evaluation.actions.length - 1];
        System.out.println(" Action: " + act.pile);
        System.out.println("  Value: " + evaluation.value);
        return state.performAction(act);
    }

    public static void main(String[] args) throws InterruptedException {
//        System.out.println(com.mancala.GameState.minimax(com.mancala.GameState.START_STATE));
//        System.out.println(new com.mancala.GameStateTree(com.mancala.GameState.START_STATE).estimateValue(10));
        Scanner input = new Scanner(System.in);

        System.out.println("Who would you like to play?");
        System.out.print("> ");

        final Player userPlayer = switch (input.nextLine()
                                               .toLowerCase()) {
            case "max" -> Player.MAX;
            case "min" -> Player.MIN;
            default -> new Random().nextBoolean() ? Player.MAX : Player.MIN;
        };
        System.out.println("You selected " + userPlayer + "!");
        Thread.sleep(250);

        System.out.println("\nHow many seeds in each house?");

        GameState state = null;
        while (state == null) try {
            System.out.print("> ");
            Thread.sleep(150);
            state = GameState.nSeedStartState(input.nextInt());
        } catch (InputMismatchException ignored) {
            input.next();
            System.out.println("try again");
        }
        do {
            printState(state, userPlayer);

            if (state.turn.isMax() != userPlayer.isMax()) {
                System.out.println("Computer's Turn: ");
                Evaluation eval = Evaluation.minimax(state, 12);
                state = performAct(state, eval);
            }
            else {
                System.out.println("Your turn: ");
                int userInput = -1;
                while (true) {
                    System.out.print("> ");
                    try {
                        userInput = input.nextInt();
                    } catch (InputMismatchException ignored) {
                        input.next();
                    }
                    if (userInput < 0 || userInput > 5 || state.getCurrentPile(userInput) <= 0) {
                        System.out.println("Please input an integer between 0 and 5");
                        continue;
                    }
                    break;
                }
                Action userAct = new Action(userInput);
                state = performAct(state, userAct);
            }
        } while (!state.terminal());
        printState(state, userPlayer);

//        System.out.println("");
//        for (com.mancala.Action act : reverse(eval.actions)) {
//            printState(state);
//
//            state = state.performAction(act);
//
//        }
//        printState(state);
    }
}