package com.as;

import java.util.ArrayList;

public class Rezult {
    private ArrayList<Person> team;
    private double quality;

    public Rezult(ArrayList<Person> team, double quality) {
        this.team = team;
        this.quality = quality;
    }

    public Rezult() {
        quality=0;
        team=new ArrayList<Person>();
    }

    public ArrayList<Person> getTeam() {
        return team;
    }

    public void setTeam(ArrayList<Person> team) {
        this.team = team;
    }

    public double getQuality() {
        return quality;
    }

    public void setQuality(double quality) {
        this.quality = quality;
    }
}
