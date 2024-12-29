package com.kallie.breakcalculator;

import java.util.Scanner;

public class App 
{
    public static void main( String[] args )
    {
        Scanner sc = new Scanner(System.in);
        String[] tournamentUrl = sc.next().split("/");
        int openBreak = sc.nextInt();
        sc.close();
        
        String serverUrl = tournamentUrl[0] + "//" + tournamentUrl[2] + "/";
        String tournamentSlug = tournamentUrl[3];

        Calicotab calicotab = new Calicotab(serverUrl, tournamentSlug);
        /*calicotab.getTeams().forEach((team) -> {
            System.out.println(team.getName());
        });*/

        BPSimulator.beginSim(calicotab.getTeams().size(), openBreak);
    }
}

/*
TEST CASES:
https://autumnloohst.calicotab.com/autumnloohst/
16

https://qho2024.calicotab.com/qho2024/
16

https://bonanza24.calicotab.com/bonanza2024/tab/team/
8

https://hhhs2024.calicotab.com/hhhs2024/
16

https://qduholiday2023.calicotab.com/qho2023/break/teams/open/
16
 */

/*
necessary user input:
- url
- openbreak (probably could find it with admin token)
 */