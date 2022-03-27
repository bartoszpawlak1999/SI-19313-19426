package com.as;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException {
        ArrayList<Person> lista=new ArrayList<>();
        Faker faker=new Faker();
        ArrayList<String> pozycje=new ArrayList<>();
        pozycje.add("top");
        pozycje.add("jungle");
        pozycje.add("mid");
        pozycje.add("adc");
        pozycje.add("supp");
        ArrayList<String> dywizje=new ArrayList<>();
        dywizje.add("Platinum");
        dywizje.add("Diamond");
        dywizje.add("Master");
        dywizje.add("Grandmaster");
        dywizje.add("Challenger");
        for(int i=0;i<100000;i++){
            Person obecny=new Person(faker.name().fullName(),
                    pozycje.get(faker.number().numberBetween(0, pozycje.size())),
                    dywizje.get(faker.number().numberBetween(0,dywizje.size())),
                    faker.number().randomDouble(2,0,5),
                    faker.number().numberBetween(3000,25000));
            lista.add(obecny);
        }
        //GsonBuilder gsonBuilder = new GsonBuilder();
        //Gson gson = gsonBuilder.create();
        //String JSONObject = gson.toJson(lista);
        //System.out.println("\nConverted JSONObject ==> " + JSONObject);

        Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
        Writer writer = new FileWriter("players.json");
        prettyGson.toJson(lista, writer);
        writer.close();

    }
}
