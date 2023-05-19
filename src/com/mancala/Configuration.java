package com.mancala;

import java.util.Arrays;

public abstract class Configuration {
    /**
     * Number of pits/holes on each side. Defaults to 6
     */
    public int houses = 6;

    /**
     * @return array of number of seeds in each hole for one side
     */
    public abstract int[] piles();


    public class holeGenerator {
        public Hole MIN_STORE() {
            return new Hole(houses);
        }

        public Hole MAX_STORE() {
            return new Hole(houses * 2 + 1);
        }
    }

    public static class Static extends Configuration {
        /**
         * Number of seeds in each hole
         */
        public int seeds = 4;

        /**
         * @param houses Number of holes on each side. Defaults to 6
         * @param seeds  Number of seeds in each hole
         */
        public Static(int houses, int seeds) {
            this.houses = houses;
            this.seeds = seeds;
        }

        @Override
        public int[] piles() {
            int[] piles = new int[houses];
            Arrays.fill(piles, seeds);
            return piles;
        }
    }

    public static Configuration.Static DEFAULT = new Configuration.Static(6, 4);
}
