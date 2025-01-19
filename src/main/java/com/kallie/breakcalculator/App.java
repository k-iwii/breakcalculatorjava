package com.kallie.breakcalculator;

import java.util.Scanner;

public class App 
{
    static final int simulationRuns = 100000;

    public static void main( String[] args )
    {
        Scanner sc = new Scanner(System.in);
        String[] tournamentUrl = sc.next().split("/");
        int openBreak = sc.nextInt();
        int jrBreak = sc.nextInt();
        int totalRounds = sc.nextInt();
        sc.close();
        
        String serverUrl = tournamentUrl[0] + "//" + tournamentUrl[2] + "/";
        String tournamentSlug = tournamentUrl[3];

        Calicotab calicotab = new Calicotab(serverUrl, tournamentSlug);
        /*calicotab.getTeams().forEach((team) -> {
            System.out.println(team.getName());
        });*/

        // DO SOMETHING TO FIGURE OUT WHETHER THIS IS BP OR WSDC

        int teams = calicotab.getTeams().size();
        while (teams % 4 != 0) teams++; // add swings; if mid-tourney, swings will enter as 0-pt teams

        int jrTeams = calicotab.getJrTeams().size();
        int roundsLeft = calicotab.getRoundsLeft(totalRounds);

        BPSimulator sim = new BPSimulator(teams, jrTeams, openBreak, jrBreak, roundsLeft, simulationRuns);
        sim.beginSim(calicotab.getStandings(new int[teams][2]));
    }
}

/* TEST CASES:
https://hhwo24.calicotab.com/hhwo25/ 8 16 5

https://autumnloohst.calicotab.com/autumnloohst/ 16 8 5

https://qho2024.calicotab.com/qho2024/ 16 8 5

https://bonanza24.calicotab.com/bonanza2024/tab/team/ 8 4 5

https://hhhs2024.calicotab.com/hhhs2024/ 16 8 5

https://qduholiday2023.calicotab.com/qho2023/break/teams/open/ 16 8 5
*/

/*
necessary user input:
- url
- openbreak & jrbreak
- total num of rounds
 */