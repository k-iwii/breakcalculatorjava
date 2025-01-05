package com.kallie.breakcalculator;

import java.util.*;

public class BPSimulator {
    int teams, jrTeams;
    int openBreak, jrBreak;
    int roundsLeft;
    int simulationRuns;
	
	static int[][] permutations = {
		{0, 1, 2, 3}, {0, 1, 3, 2}, {0, 2, 1, 3}, {0, 2, 3, 1}, {0, 3, 1, 2}, {0, 3, 2, 1}, //0-5
		{1, 0, 2, 3}, {1, 0, 3, 2}, {1, 2, 0, 3}, {1, 2, 3, 0}, {1, 3, 0, 2}, {1, 3, 2, 0}, //6-11
		{2, 0, 1, 3}, {2, 0, 3, 1}, {2, 1, 0, 3}, {2, 1, 3, 0}, {2, 3, 0, 1}, {2, 3, 1, 0}, //12-17
		{3, 0, 1, 2}, {3, 0, 2, 1}, {3, 1, 0, 2}, {3, 1, 2, 0}, {3, 2, 0, 1}, {3, 2, 1, 0}  //18-23
	};

    public BPSimulator(int teams, int jrTeams, int openBreak, int jrBreak, int roundsLeft, int simulationRuns) {
        this.teams = teams;
        this.jrTeams = jrTeams;
        this.openBreak = openBreak;
        this.jrBreak = jrBreak;
        this.roundsLeft = roundsLeft;
        this.simulationRuns = simulationRuns;
    }

	public void beginSim(int[][] startingPoints) {
        sortDescending(startingPoints);

        while (teams % 4 != 0) teams++; // add swings

        int minOpen = Integer.MAX_VALUE, minJr = Integer.MAX_VALUE;
        int[] minOpenFrac = new int[2], minJrFrac = new int[2];

        int maxOpen = 0, maxJr = 0;
        int[] maxOpenFrac = new int[2], maxJrFrac = new int[2];

        int[][] minArr = new int[teams][2];
        int[][] maxArr = new int[teams][2];

        for (int i = 0; i < simulationRuns; i++) {
            // simulate using duplicate array of startingPoints
            int[][] sim = simTourney(Arrays.stream(startingPoints).map(a -> Arrays.copyOf(a, a.length)).toArray(int[][]::new));
            
            // FIND BREAKING 'THRESHOLD' (lowest # points needed to break)
            int x = sim[openBreak - 1][0];
            int[] xJr = findXJr(sim);
            if (xJr[0] == -1) continue;

            //UPDATE OPEN
            int[] frac = findFraction(x, sim);

            double ratio = (double) frac[0] / frac[1];
            double minRatio = (double) minOpenFrac[0] / minOpenFrac[1];
            double maxRatio = (double) maxOpenFrac[0] / maxOpenFrac[1];

            if (x < minOpen || x == minOpen && ratio > minRatio) {
                minOpen = x; minOpenFrac = frac; //minArr = sim;
            }
            if (x > maxOpen || x == maxOpen && ratio < maxRatio) {
                maxOpen = x; maxOpenFrac = frac; //maxArr = sim;
            }

            // UPDATE JR
            double minJrRatio = (double) minJrFrac[0] / minJrFrac[1];
            double maxJrRatio = (double) maxJrFrac[0] / maxJrFrac[1];
            
            if (roundsLeft == 5) {
                int[] jrFracBest = findJrFraction(xJr[0], bestCaseAllocation(sim), true);
                int[] jrFracWorst = findJrFraction(xJr[1], worstCaseAllocation(sim), false);

                double jrRatioBest = (double) jrFracBest[0] / jrFracBest[1];
                double jrRatioWorst = (double) jrFracWorst[0] / jrFracWorst[1];

                if (xJr[0] < minJr || xJr[0] == minJr && jrRatioBest > minJrRatio) {
                    minJr = xJr[0]; minJrFrac = jrFracBest; minArr = sim;
                }
                if (xJr[1] > maxJr || xJr[1] == maxJr && jrRatioWorst < maxJrRatio) {
                    maxJr = xJr[1]; maxJrFrac = jrFracWorst; maxArr = sim;
                }
            } else {
                int[] jrFrac = findJrFraction(xJr[0], sim, false);
                double jrRatio = (double) jrFrac[0] / jrFrac[1];

                if (xJr[0] < minJr || xJr[0] == minJr && jrRatio > minJrRatio) {
                    minJr = xJr[0]; minJrFrac = jrFrac; minArr = sim;
                }
                if (xJr[0] > maxJr || xJr[0] == maxJr && jrRatio < maxJrRatio) {
                    maxJr = xJr[0]; maxJrFrac = jrFrac; maxArr = sim;
                }
            }
        }

        System.out.println(" --- OPEN BREAK ---");
        System.out.println("best case: " + minOpenFrac[0] + "/" + minOpenFrac[1] + " teams on " + minOpen + " break.");
        //printArr(minArr);
        System.out.println("worst case: " + maxOpenFrac[0] + "/" + maxOpenFrac[1] + " teams on " + maxOpen + " break.");
        //printArr(maxArr);

        System.out.println("\n --- JUNIOR BREAK ---");
        System.out.println("best case: " + minJrFrac[0] + "/" + minJrFrac[1] + " junior teams on " + minJr + " break.");
        printArr(minArr);
        System.out.println("worst case: " + maxJrFrac[0] + "/" + maxJrFrac[1] + " junior teams on " + maxJr + " break.");
        printArr(maxArr);
    }

    public int[][] simTourney(int[][] sim) {
        for (int round = 0; round < roundsLeft; round++) {
            for (int room = 0; room < teams; room += 4) {
                int rand = (int) (Math.random() * 24);

                for (int i = 0; i < 4; i++)
                    sim[room + i][0] += permutations[rand][i];
            }
            
            sortDescending(sim);
        }

        return sim;
    }

    public int[] findFraction(int x, int[][] sim) {
        int total = 0; // total occurences of x
        int broke = 0; // occurences of x among teams that broke

        // count the number of teams that broke on x
        for (int i = openBreak - 1; i >= 0; i--) {
            if (sim[i][0] == x) {
                broke++;
                total++;
            }
            if (sim[i][0] > x) break;
        }

        // count the total number of teams on
        for (int i = openBreak; i < teams; i++) {
            if (sim[i][0] == x) total++;
            if (sim[i][0] < x) break;
        }

        return new int[] {broke, total};
    }

    public int[] findXJr(int[][] sim) {
        int[] ans = {-1, -1};

        if (roundsLeft == 5) {
            // ans[0] = best case; ans[1] = worst case
            ans[0] = sim[teams - Math.max(jrTeams - jrBreak - openBreak, 0) - 1][0];
            ans[1] = sim[openBreak + jrBreak - 1][0];
        } else {
            // only 1 case (no best/worst); ans in format {break, -1}
            for (int i = openBreak, j = 0; i < teams; i++) {
                if (sim[i][1] == 1) j++;

                if (j == jrBreak) {
                    ans[0] = sim[i][0];
                    break;
                }
            }

            // if ans[0] is -1, then this simulation is invalid
        }

        return ans;
    }

    public int[] findJrFraction(int x, int[][] sim, boolean isBestCase) {
        int total = 0; // total occurences of x
        int broke = 0; // occurences of x among teams that broke
        
        // count the number of jrbreaking teams that broke on x
        int i = 0;
        if (isBestCase) {
            int openBreakingJrs = 0; // # junior teams that broke open
            for (int j = 0; j < openBreak; j++) {
                if (sim[j][1] == 1) openBreakingJrs++;
                else break;
            }

            for (i = teams - jrTeams + openBreakingJrs; i < teams - jrTeams + openBreakingJrs + jrBreak; i++) {
                if (sim[i][0] == x && sim[i][1] == 1) {
                    broke++;
                    total++;
                }
            }
        } else {
            i = openBreak;
            for (int j = 0; i < teams && j < jrBreak; i++) {
                if (sim[i][0] == x && sim[i][1] == 1) {
                    broke++;
                    total++;
                    j++;
                }
                if (sim[i][0] < x) break;
            }
        }

        // count the total number of jr teams on x
        for (; i < teams; i++) {
            if (sim[i][0] == x && sim[i][1] == 1) total++;
            if (sim[i][0] < x) break;
        }

        return new int[] {broke, total};
    }

    public int[][] bestCaseAllocation(int[][] sim) {
        int j = jrTeams;
        // first, fill up the last jrBreak teams
        for (int i = teams - 1; i >= teams - jrBreak && j > 0; i--, j--)
            sim[i][1] = 1;

        // then, fill up the first openBreak teams
        for (int i = 0; i < openBreak && j > 0; i++, j--)
            sim[i][1] = 1;

        // last, if u have any more teams, fill them from the bottom up
        for (int i = teams - jrBreak - 1; i > 0 && j > 0; i--, j--)
            sim[i][1] = 1;
        
        return sim;
    }

    public int[][] worstCaseAllocation(int[][] sim) {
        // fill the first jrBreak teams after openBreak
        for (int i = openBreak; i < openBreak + jrBreak; i++)
            sim[i][1] = 1;
        
        return sim;
    }

    public void sortDescending(int[][] arr) {
        Arrays.sort(arr, new Comparator<int[]>() {
            @Override
            public int compare(int[] a, int[] b) {
                return Integer.compare(b[0], a[0]); // compare based on 1st column in descending order
            }
        });
    }

    public void printArr(int[][] arr) {
        for (int i = 0; i < teams; i++) {
            System.out.print(arr[i][0] + " ");
            if (i % 4 == 3) System.out.println();
        }
        System.out.println();
    }
}