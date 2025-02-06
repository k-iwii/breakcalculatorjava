package com.kallie.breakcalculator;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Speaker {
    private int id;
    private String name;
    private String url;
    private boolean isJunior; // 1 if junior, 0 if not
    @JsonProperty("categories")
    private String[] breakCategories;

    public Speaker() { }

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
}
