package com.example.shubhraj.assetsdata.model;

/**
 * Created by Shubhraj on 23-10-2017.
 */

public class Pokemon
{
    private String name,type;

    public Pokemon(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }
}
