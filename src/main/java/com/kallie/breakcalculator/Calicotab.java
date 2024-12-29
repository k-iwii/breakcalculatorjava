package com.kallie.breakcalculator;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Calicotab {
    private String serverUrl;
    private String tournamentSlug;

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
        String urlString = serverUrl + "api/v1/tournaments/" + tournamentSlug + "/teams";
        List<Team> teams = new ArrayList<>();

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {
                Scanner scanner = new Scanner(url.openStream());
                StringBuilder inline = new StringBuilder();
                while (scanner.hasNext()) {
                    inline.append(scanner.nextLine());
                }
                scanner.close();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                Team[] teamArray = mapper.readValue(inline.toString(), Team[].class);
                for (Team team : teamArray) {
                    teams.add(team);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return teams;
    }
}