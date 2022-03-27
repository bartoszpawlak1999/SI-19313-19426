package com.as;

public class Person {
    private String nick;
    private String position;
    private String division;
    private Double KDA;
    private int pensja;

    public Person(String nick, String position, String division, Double KDA, int pensja) {
        this.nick = nick;
        this.position = position;
        this.division = division;
        this.KDA = KDA;
        this.pensja = pensja;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public Double getKDA() {
        return KDA;
    }

    public void setKDA(Double KDA) {
        this.KDA = KDA;
    }

    public int getPensja() {
        return pensja;
    }

    public void setPensja(int pensja) {
        this.pensja = pensja;
    }
}
