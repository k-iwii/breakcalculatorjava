package com.kallie.breakcalculator;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Calicotab {
    private String serverUrl;
    private String tournamentSlug;
    private List<Team> teams = new ArrayList<>();
    private List<Team> jrTeams = new ArrayList<>();

    public Calicotab(String serverUrl, String tournamentSlug) {
        this.serverUrl = serverUrl;
        this.tournamentSlug = tournamentSlug;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public String getTournamentSlug() {
        return tournamentSlug;
    }

    public List<Team> getTeams() {
        if (teams.isEmpty())
            findTeams();
        return teams;
    }

    public List<Team> getJrTeams() {
        if (jrTeams.isEmpty())
            findTeams();
        return jrTeams;
    }

    private String urlToString(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("HttpResponseCode: " + responseCode);
        }
        Scanner scanner = new Scanner(url.openStream());
        StringBuilder inline = new StringBuilder();
        while (scanner.hasNext()) {
            inline.append(scanner.next());
        }
        scanner.close();

        return inline.toString();
    }

    public void findTeams() {
        String urlString = serverUrl + "api/v1/tournaments/" + tournamentSlug + "/teams";

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            Team[] teamArray = mapper.readValue(urlToString(urlString), Team[].class);
            for (Team team : teamArray) {
                teams.add(team);
                if (team.isJunior())
                    jrTeams.add(team);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getRoundsLeft(int totalRounds) {
        List<Map<String, Object>> team = readStandings().get(0);
        return totalRounds - (team.size() - 2);
    }

    public int[][] getStandings(int[][] standings) {
        getJrTeams();
        HashSet<String> jrTeamNames = new HashSet<>();
        for (Team t : jrTeams)
            jrTeamNames.add(t.getName());

        List<List<Map<String, Object>>> teams = readStandings();
        int i = 0;
        for (List<Map<String, Object>> team : teams) {
            //System.out.println(team.get(0).get("text") + ": " + team.get(1).get("sort"));
            String teamName = team.get(0).get("text").toString();
            int teamPoints = Integer.parseInt(team.get(1).get("sort").toString());

            standings[i][0] = teamPoints;
            standings[i][1] = jrTeamNames.contains(teamName) ? 1 : 0;
            i++;
        }

        return standings;
    }

    public List<List<Map<String, Object>>> readStandings() {
        String urlString = serverUrl + tournamentSlug + "/tab/current-standings/";

        List<List<Map<String, Object>>> ans = new ArrayList<>();

        try {
            Document doc = Jsoup.parse(urlToString(urlString));
            Elements scriptElements = doc.select("script");

            for (Element script : scriptElements) {
                String scriptContent = script.html(); // Inline JavaScript
                int startIdx = scriptContent.indexOf("tablesData:[");
                int endIdx = scriptContent.lastIndexOf("]");
                if (startIdx == -1 || endIdx == -1)
                    continue;
                scriptContent = scriptContent.substring(0, endIdx);
                scriptContent = scriptContent.substring(startIdx + 12);

                ObjectMapper mapper = new ObjectMapper();
                @SuppressWarnings("unchecked")
                Map<String, Object> map = mapper.readValue(scriptContent, Map.class);

                @SuppressWarnings("unchecked")
                List<List<Map<String, Object>>> teams = (List<List<Map<String, Object>>>)map.get("data");
                ans = teams;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return ans;
    }
}