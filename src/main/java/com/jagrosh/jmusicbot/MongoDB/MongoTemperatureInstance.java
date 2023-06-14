package com.jagrosh.jmusicbot.MongoDB;

import com.jagrosh.jmusicbot.JMusicBot;
import com.jagrosh.jmusicbot.utils.TemperatureInstance;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MongoTemperatureInstance extends Mongo {

    private final List<TemperatureInstance> temperatureInstrances;


    public MongoTemperatureInstance() {
        temperatureInstrances = new ArrayList<TemperatureInstance>();

        connect();
        load();
    }

    // connect database
    private void connect() {

        // Heb je geen gegevens in de MainApplication staan slaat hij het maken van de verbinding over
        if (JMusicBot.getNosqlHost().equals("")) {

            System.out.println("The connection string is null!!!!");

            return;
        }

        // Verbind alleen als er nog geen actieve verbinding is.
        if (this.mongoClient == null) {
            try {
                // Open pijpleiding
                this.mongoClient = MongoClients.create(JMusicBot.getNosqlHost());
                // Selecteer de juiste database
                this.mongoDatabase = mongoClient.getDatabase(JMusicBot.getNosqlDatabase());
            } catch (MongoException e) {
                e.printStackTrace();
            }
        }
    }

    public void load() {

        // Als je geen NoSQL server hebt opgegeven gaat de methode niet verder anders zou je een nullpointer krijgen
        if (JMusicBot.getNosqlHost().equals(""))
            return;

        // Selecteer de juiste collecton in de NoSQL server
        this.selectedCollection("weatherInstance");

        // Haal alles op uit deze collection en loop er 1 voor 1 doorheen
        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            // Zolang er data is
            while (cursor.hasNext()) {
                // warning Java is case sensitive
                // Haal alle velden per record
                Document tempReiziger = cursor.next();
                String location = (String) tempReiziger.get("location");
                String time = (String) tempReiziger.get("time");
                double temperature = Double.valueOf(tempReiziger.get("temperature").toString());
                String winddirection = (String) tempReiziger.get("winddirection");
                String alarmtext = (String) tempReiziger.get("alarmtext");
                String expectedWeather = (String) tempReiziger.get("alarmtext");
                String kindOfWeather = (String) tempReiziger.get("kindOfWeather");



                // Maak een nieuw object en voeg deze toe aan de arraylist
                temperatureInstrances.add(new TemperatureInstance(location,time,temperature,winddirection,alarmtext,expectedWeather, kindOfWeather));
            }
        } finally {
            // Sluit de stream
            cursor.close();
        }
    }



    public void add(Object object) {

        try {
            if (object instanceof TemperatureInstance){
                Document document = new Document("location", ((TemperatureInstance) object).getLocation()).append("time", ((TemperatureInstance) object).getTime())
                        .append("temperature", ((TemperatureInstance) object).getTemperature()).append("winddirection", ((TemperatureInstance) object).getWinddirection()).append("alarmtext", ((TemperatureInstance) object).getAlarmtext())
                        .append("expectedWeather", ((TemperatureInstance) object).getExpectedWeather()).append("kindOfWeather", ((TemperatureInstance) object).getKindOfWeather());

                collection.insertOne(document);
            }
        }
        catch (Exception e){
            System.out.println("Shit, that conversion didn't go as planned. " + e);
        }

    }
}
