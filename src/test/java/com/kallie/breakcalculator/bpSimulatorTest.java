package com.kallie.breakcalculator;

import java.io.IOException;
import org.junit.jupiter.api.Test;
// import static org.junit.jupiter.api.Assertions.*;

public class bpSimulatorTest {
    @Test
    public void testBeginSim() throws IOException {
        int simulationRuns = 100000;
        int teams = readInt(), jrTeams = readInt(), openBreak = readInt(), jrBreak = readInt();
        int roundsLeft = readInt();

        int[][] startingPoints = new int[teams][2];

        if (roundsLeft != 5)
            for (int i = 0; i < teams; i++)
                startingPoints[i][0] = readInt();

        if (jrTeams != 0)
            for (int i = 0; i < jrTeams; i++)
                startingPoints[i][1] = readInt();

        // Call the beginSim method
        BPSimulator sim = new BPSimulator(teams, jrTeams, openBreak, jrBreak, roundsLeft, simulationRuns);
        sim.beginSim(startingPoints);

        // Add assertions to verify the expected behavior
        // For example, you can check if the teams variable is correctly set
        // assertEquals(teams, BPSimulator.teams);
        // assertEquals(openBreak, BPSimulator.openBreak);
        // assertEquals(roundsLeft, BPSimulator.roundsLeft);
        // assertEquals(simulationRuns, BPSimulator.simulationRuns);
    }

    static int readInt() throws IOException {
        int x = 0, c;
        while ((c = System.in.read()) != ' ' && c != '\n')
            x = x * 10 + (c - '0');
        return x;
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