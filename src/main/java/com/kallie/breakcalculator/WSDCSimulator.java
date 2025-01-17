package com.kallie.breakcalculator;

import java.util.*;

public class WSDCSimulator {

    int teams, jrTeams;
    int openBreak, jrBreak;
    int roundsLeft;
    int simulationRuns; // 100,000
	
	static int[][] permutations = { {0, 1}, {1, 0}};

    public WSDCSimulator(int teams, int jrTeams, int openBreak, int jrBreak, int roundsLeft, int simulationRuns) {
        this.teams = teams;
        this.jrTeams = jrTeams;
        this.openBreak = openBreak;
        this.jrBreak = jrBreak;
        this.roundsLeft = roundsLeft;
        this.simulationRuns = simulationRuns;
    }

    int maxOpen = 0, maxJr = 0;
    int minOpen = Integer.MAX_VALUE, minJr = Integer.MAX_VALUE;
    int[] minOpenFrac = new int[2], minJrFrac = new int[2];
    int[] maxOpenFrac = new int[2], maxJrFrac = new int[2];
    int[][] minArr = new int[teams][2], maxArr = new int[teams][2];

	public void beginSim(int[][] startingPoints) {
        sortDescending(startingPoints);

        if (teams % 2 == 1) teams++; // assume that they will add swing teams

        for (int i = 0; i < simulationRuns; i++) {
            // simulate using duplicate array of startingPoints
            int[][] sim = simTourney(Arrays.stream(startingPoints).map(a -> Arrays.copyOf(a, a.length)).toArray(int[][]::new));

            //UPDATE JR
            if (roundsLeft == 5) {
                handleBestCase(sim);
                handleWorstCase(sim);
            } else {
                if (!handleJr(sim)) continue;
            }

            // UPDATE OPEN
            handleOpen(sim);
        }

        System.out.println(" --- OPEN BREAK ---");
        System.out.println("Best case: " + minOpenFrac[0] + "/" + minOpenFrac[1] + " open teams on " + minOpen + " break.");
        System.out.println("Worst case: " + maxOpenFrac[0] + "/" + maxOpenFrac[1] + " open teams on " + maxOpen + " break.");

        if (jrTeams > 0) {
            System.out.println("\n --- JUNIOR BREAK ---");
            System.out.println("Best case: " + minJrFrac[0] + "/" + minJrFrac[1] + " junior teams on " + minJr + " break.");
            System.out.println("Worst case: " + maxJrFrac[0] + "/" + maxJrFrac[1] + " junior teams on " + maxJr + " break.");
        }
    }

    public int[][] simTourney(int[][] sim) {
        for (int round = 0; round < roundsLeft; round++) {
            for (int room = 0; room < teams; room += 2) {
                int rand = (int) (Math.random() * 2);

                sim[room][0] += permutations[rand][0];
                sim[room + 1][0] += permutations[rand][1];
            }

            Arrays.sort(sim);
        }

        return sim;
    }

    public int[] findFraction(int x, int[][] sim) {
        int total = 0; // total occurences of x
        int broke = 0; // occurences of x among teams that broke
        for (int i = teams - openBreak; i < teams; i++) {
            if (sim[i][0] == x) {
                broke++;
                total++;
            }
            if (sim[i][0] > x) break;
        }

        for (int i = teams - openBreak - 1; i >= 0; i--) {
            if (sim[i][0] == x) total++;
            if (sim[i][0] < x) break;
        }

        return new int[] {broke, total};
    }

    public void sortDescending(int[][] arr) {
        Arrays.sort(arr, new Comparator<int[]>() {
            @Override
            public int compare(int[] a, int[] b) {
                return Integer.compare(b[0], a[0]); // compare based on 1st column in descending order
            }
        });
    }

    public void printArr(int[] arr) {
        for (int i = teams - 1; i >= 0; i -= 2)
            System.out.print(arr[i] + " " + arr[i + 1] + " ");
        System.out.println();
    }
}