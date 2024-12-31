package com.kallie.breakcalculator;

import org.junit.jupiter.api.Test;
// import static org.junit.jupiter.api.Assertions.*;

public class bpSimulatorTest {
    @Test
    public void testBeginSim() {
        int teams = 8;
        int openBreak = 4;
        int roundsLeft = 3;
        int simulationRuns = 100000;
        int[] startingPoints = {10, 8, 6, 4, 2, 1, 0, 0};

        // Call the beginSim method
        BPSimulator.beginSim(teams, openBreak, roundsLeft, simulationRuns, startingPoints);

        // Add assertions to verify the expected behavior
        // For example, you can check if the teams variable is correctly set
        // assertEquals(teams, BPSimulator.teams);
        // assertEquals(openBreak, BPSimulator.openBreak);
        // assertEquals(roundsLeft, BPSimulator.roundsLeft);
        // assertEquals(simulationRuns, BPSimulator.simulationRuns);
    }
}

/*
FORMAT: teams, jrTeams, openBreak, jrBreak, roundsLeft

OPEN & JR, <5-ROUND TEST CASES:
16 9 4 4 4
0 3 2 1 0 2 3 0 1 1 0 2 3 3 2 1
1 1 0 0 1 1 0 0 1 1 0 1 1 0 0 1

OPEN & JR, 5-ROUND TEST CASES:
16 9 4 4 5
1 1 0 0 1 1 0 0 1 1 0 1 1 0 0 1

OPEN-ONLY, <5-ROUND TEST CASES: PASS
16 0 4 0 4
0 3 2 1 0 2 3 0 1 1 0 2 3 3 2 1
16 0 8 0 4
2 3 1 0 3 2 1 3 0 2 0 3 1 2 0 1

OPEN-ONLY, 5-ROUND TEST CASES: PASS
16 0 4 0 5
16 0 8 0 5
*/