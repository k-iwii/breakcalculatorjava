package com.kallie.breakcalculator;

import java.util.*;

public class WSDCSimulator {

    static int teams, jrTeams;
    static int openBreak, jrBreak;
    static int roundsLeft;
    static int simulationRuns; // 100,000
	
	static int[][] permutations = { {0, 1}, {1, 0}};

	public static void beginSim(int teams, int openBreak, int roundsLeft, int simulationRuns, int[] startingPoints) {
        WSDCSimulator.teams = teams;
        WSDCSimulator.openBreak = openBreak;
        WSDCSimulator.roundsLeft = roundsLeft;
        WSDCSimulator.simulationRuns = simulationRuns;

        if (teams % 2 == 1) teams++; // assume that they will add swing teams

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
        for (int i = teams - 1; i >= 0; i -= 2)
            System.out.print(arr[i] + " " + arr[i + 1] + " ");
        System.out.println();
    }

    public static int[] simTourney(int[] sim) {
        for (int round = 0; round < roundsLeft; round++) {
            for (int room = 0; room < teams; room += 2) {
                int rand = (int) (Math.random() * 2);

                sim[room] += permutations[rand][0];
                sim[room + 1] += permutations[rand][1];
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