package com.as;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;

public class Wczytaj {
    private static final Type PLAYERS_TYPE = new TypeToken<ArrayList<Person>>() {}.getType();
    private static final Type REZULT_TYPE = new TypeToken<ArrayList<Rezult>>() {}.getType();
    private static final Faker faker=new Faker();
    private static final Gson gson = new Gson();
    private static final int HMS=10;
    private static final double dopuszczalna_jakosc=60;
    private static final double money=50000;
    private static final double srednia=money/5;
    private static final int NI=100000;
    public static void main(String[] args) throws IOException {

        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader("players.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert reader != null;
        ArrayList<Person> lista = gson.fromJson(reader, PLAYERS_TYPE);
        //ArrayList<Rezult> HM=makeHM(lista);

        ArrayList<Rezult> HM=readHM();
        for(int i=0;i<NI;i++){
            int randomNr=faker.number().numberBetween(1,100);
            boolean choice=false;
            boolean choice2=false;
            if(randomNr>70){
                choice=true;
            }else if (randomNr>50){
                choice2=true;
            }
            los(HM,lista,choice,choice2);
        }
        writeHM(HM);
    }
    private static ArrayList<Rezult> makeHM(ArrayList<Person> lista) throws IOException {
        ArrayList<Rezult> HM= new ArrayList<>();
        los_HM(lista,HM);
        for (Rezult rezults:HM) {
            rezults.getTeam().sort(Comparator.comparing(Person::getPosition));
        }
        writeHM(HM);
        return HM;
    }
    private static void writeHM(ArrayList<Rezult> HM) throws IOException {
        Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
        Writer writer = new FileWriter("playersChosen.json");
        prettyGson.toJson(HM, writer);
        writer.close();
    }
    private static ArrayList<Rezult> readHM(){
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader("playersChosen.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert reader != null;
        ArrayList<Rezult> HM=gson.fromJson(reader, REZULT_TYPE);
        return HM;
    }
    private static void los_HM(ArrayList<Person> lista,
                               ArrayList<Rezult> HM){
        for(int i=0;i<HMS;){
            String teamRoles="";
            ArrayList<Person> team= new ArrayList<>();
            double jakosc_teamu=0;
            double pensja_teamu=0;
            for(int j=0;j<5;){
                int random=faker.number().numberBetween(0,100000-1);
                Person wylosowany=lista.get(random);
                if(!teamRoles.contains(wylosowany.getPosition())) {
                    double jakosc=getJakosc(wylosowany);
                    j++;
                    teamRoles+=wylosowany.getPosition();
                    jakosc_teamu+=jakosc;
                    pensja_teamu+=wylosowany.getPensja();
                    team.add(wylosowany);
                    if (jakosc_teamu >= dopuszczalna_jakosc && j==5) {
                        if(pensja_teamu<=money){
                            System.out.println(pensja_teamu);
                            HM.add(new Rezult(team,jakosc_teamu));
                            i++;
                        }
                        else {
                            j=0;
                            team.clear();
                            teamRoles="";
                            pensja_teamu=0;
                            jakosc_teamu=0;
                        }
                    }
                    else if(j==5) {
                        j=0;
                        team.clear();
                        teamRoles="";
                        pensja_teamu=0;
                        jakosc_teamu=0;//
                    }
                }
            }
        }
    }
    private static void los(ArrayList<Rezult> arraychoices, ArrayList<Person> arrayAll, boolean type, boolean type2){
        ArrayList<Person> team=new ArrayList<>();
        double jakosc_teamu=0;
        double pensja_teamu=0;
        String teamRoles="";
        for(int j=0;j<5;) {
            int random;
            Person wylosowany;
            if (type) {
                random = faker.number().numberBetween(0, 100000 - 1);
                wylosowany = arrayAll.get(random);
            } else {
                if(type2){
                    int ranFromAll=faker.number().numberBetween(0,4);
                    if(ranFromAll<1){
                        random = faker.number().numberBetween(0, 100000 - 1);
                        wylosowany = arrayAll.get(random);
                    }
                    else {
                        random = faker.number().numberBetween(0, arraychoices.size()-1);
                        wylosowany = arraychoices.get(random).getTeam().get(j);
                    }
                }
                else {
                    random = faker.number().numberBetween(0, arraychoices.size()-1);
                    wylosowany = arraychoices.get(random).getTeam().get(j);
                }
            }
            if(!teamRoles.contains(wylosowany.getPosition())) {
                double jakosc = getJakosc(wylosowany);
                teamRoles+=wylosowany.getPosition();
                j++;
                jakosc_teamu += jakosc;
                pensja_teamu += wylosowany.getPensja();
                team.add(wylosowany);
                if (jakosc_teamu >= dopuszczalna_jakosc && j == 5) {
                    ArrayList<Double> jakosci = new ArrayList<>();
                    for (Rezult rezult : arraychoices) {
                        jakosci.add(rezult.getQuality());
                    }
                    double min_jakos = jakosci.stream().min(Double::compare).orElse(-1.0);
                    //System.out.println(min_jakos);
                    if (pensja_teamu <= money && jakosc_teamu > min_jakos) {
                        int teamid = 0;
                        for (Rezult test : arraychoices) {
                            if (test.getQuality() == min_jakos) {
                                break;
                            }
                            teamid++;
                        }

                        Rezult newBeast=new Rezult(team, jakosc_teamu);
                        newBeast.getTeam().sort(Comparator.comparing(Person::getPosition));
                        int sameTeams=0;
                        for (Rezult rezult:arraychoices) {
                            sameTeams=0;
                            for (Person person:rezult.getTeam()) {
                                //System.out.println(person.getNick()+" VS "+newBeast.getTeam().get(sameTeams).getNick());
                                if (person.getNick().equals(newBeast.getTeam().get(sameTeams).getNick())) sameTeams++;
                                else {
                                    break;
                                }
                            }
                            //System.out.println(sameTeams);
                            if(sameTeams==5){
                                //System.out.println("Takie same drużyny: "+sameTeams);
                                break;
                            }
                        }
                        if(sameTeams!=5)
                        {
                            System.out.println("New team with quality: "+jakosc_teamu);
                            arraychoices.remove(teamid);
                            arraychoices.add(new Rezult(team, jakosc_teamu));
                        }
                    }
                }
            }
        }
    }
    private static double getJakosc(Person wylosowany){
        double jakosc = switch (wylosowany.getDivision()) {
            case "Platinum" -> 2;
            case "Diamond" -> 3;
            case "Master" -> 5;
            case "Grandmaster" -> 6;
            case "Challenger" -> 8;
            default -> 0;
        };
        double kda = wylosowany.getKDA();
        if (kda > 1) kda *= 2.5;
        if (kda < 1) kda *= -5.5;
        jakosc += kda;
        double pensjaDod = Math.round(((srednia - wylosowany.getPensja()) / 1000) * 10) / 10;
        if (pensjaDod > 5) pensjaDod = 5;
        if (pensjaDod < -5) pensjaDod = -5;
        jakosc -= pensjaDod;
        return Math.round(jakosc*1000.0)/1000.0;
    }
}
