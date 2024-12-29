package com.kallie.breakcalculator;

import java.util.*;

public class BPSimulator {

    static int teams;
    static int openBreak;
    static int totalRounds = 5;
    static final int simulationRuns = 100000;
	
	static int[][] permutations = {
		{0, 1, 2, 3}, {0, 1, 3, 2}, {0, 2, 1, 3}, {0, 2, 3, 1}, {0, 3, 1, 2}, {0, 3, 2, 1}, //0-5
		{1, 0, 2, 3}, {1, 0, 3, 2}, {1, 2, 0, 3}, {1, 2, 3, 0}, {1, 3, 0, 2}, {1, 3, 2, 0}, //6-11
		{2, 0, 1, 3}, {2, 0, 3, 1}, {2, 1, 0, 3}, {2, 1, 3, 0}, {2, 3, 0, 1}, {2, 3, 1, 0}, //12-17
		{3, 0, 1, 2}, {3, 0, 2, 1}, {3, 1, 0, 2}, {3, 1, 2, 0}, {3, 2, 0, 1}, {3, 2, 1, 0}  //18-23
	};

	public static void beginSim(int teams, int openBreak) {
        BPSimulator.teams = teams;
        BPSimulator.openBreak = openBreak;

        while (teams % 4 != 0) teams++; // assume that they will add swing teams

        int min = Integer.MAX_VALUE;
        int[] minFrac = new int[2];
        int[] minArr = new int[teams];
        
        int max = 0;
        int[] maxFrac = new int[2];
        int[] maxArr = new int[teams];

        for (int i = 0; i < simulationRuns; i++) {
            int[] sim = simTourney(new int[teams]);
            int x = sim[teams - openBreak];
            int[] frac = findFraction(x, sim);
            
            double ratio = (double) frac[0] / frac[1];
            double minRatio = (double) minFrac[0] / minFrac[1];
            double maxRatio = (double) maxFrac[0] / maxFrac[1];

            if (x < min || x == min && ratio > minRatio) {
                min = x;
                minFrac = frac;
                minArr = sim;
            }
            if (x > max || x == max && ratio < maxRatio) {
                max = x;
                maxFrac = frac;
                maxArr = sim;
            }
        }

        // could also output as fraction!
        String minStr = "" + (Math.round(((double) minFrac[0]/minFrac[1] * 100) * 100.0) / 100.0);
        String maxStr = "" + (Math.round(((double) maxFrac[0]/maxFrac[1] * 100) * 100.0) / 100.0);

        System.out.println("Best case: " + minStr + "% of teams on " + min + " break.");
        //printArr(minArr);
        System.out.println("Worst case: " + maxStr + "% of teams on " + max + " break.");
        //printArr(maxArr);
    }

    public static void printArr(int[] arr) {
        for (int i = teams - 1; i >= 0; i--) {
            System.out.print(arr[i] + " ");
            if (i % 4 == 0) System.out.println();
        }
        System.out.println();
    }

    public static int[] simTourney(int[] sim) {
        for (int round = 0; round < totalRounds; round++) {
            for (int room = 0; room < teams; room += 4) {
                int rand = (int) (Math.random() * 24);

                for (int i = 0; i < 4; i++) {
                    sim[room + i] += permutations[rand][i];
                }
            }
            Arrays.sort(sim);
        }

        return sim;
    }

    public static int[] findFraction(int x, int[] sim) {
        int total = 0; // total occurences of x
        int broke = 0; // occurences of x among teams that broke
        for (int i = teams - openBreak; i < teams; i++) {
            if (sim[i] == x) {
                broke++;
                total++;
            }
            if (sim[i] > x) break;
        }

        for (int i = teams - openBreak - 1; i >= 0; i--) {
            if (sim[i] == x) total++;
            if (sim[i] < x) break;
        }

        return new int[] {broke, total};
    }
}