package com.kallie.breakcalculator;

import java.io.IOException;

import org.junit.jupiter.api.Test;


public class wsdcSimulatorTest {
    @Test
    public void testBeginSim() throws IOException {
        int simulationRuns = 100000;
        int teams = 16, jrTeams = 5, openBreak = 4, jrBreak = 2;
        int roundsLeft = 5;

        int[][] startingPoints = new int[teams][2];
        // Call the beginSim method
        WSDCSimulator sim = new WSDCSimulator(teams, jrTeams, openBreak, jrBreak, roundsLeft, simulationRuns);
        sim.beginSim(startingPoints);
    }
}
