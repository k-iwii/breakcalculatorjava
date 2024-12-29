package com.kallie.breakcalculator;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Team {
    private int id;
    @JsonProperty("reference")
    private String name;
    private String url;
    private boolean isJunior;
    @JsonProperty("break_categories")
    private String[] breakCategories;
    private Speaker[] speakers;

    public Team() { }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isJunior() {
        return isJunior;
    }   

    public String[] getBreakCategories() {
        return breakCategories;
    }

    public void setBreakCategories(String[] breakCategories) {
        this.breakCategories = breakCategories;
        this.isJunior = breakCategories.length > 1;
    }

    public Speaker[] getSpeakers() {
        return speakers;
    }

    public void setSpeakers(Speaker[] speakers) {
        this.speakers = speakers;
    }
}
