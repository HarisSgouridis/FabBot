package com.jagrosh.jmusicbot.MongoDB;

import com.jagrosh.jmusicbot.JMusicBot;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

public class MongoKiss extends Mongo{



    public MongoKiss() {

        connect();
        load();
    }


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
        this.selectedCollection("kissManager");

        // Haal alles op uit deze collection en loop er 1 voor 1 doorheen
        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            // Zolang er data is
//            while (cursor.hasNext()) {
//                // warning Java is case sensitive
//                // Haal alle velden per record
//                Document tempReiziger = cursor.next();
//                String location = (String) tempReiziger.get("location");
//                String time = (String) tempReiziger.get("time");
//                double temperature = Double.valueOf(tempReiziger.get("temperature").toString());
//                String winddirection = (String) tempReiziger.get("winddirection");
//                String alarmtext = (String) tempReiziger.get("alarmtext");
//                String expectedWeather = (String) tempReiziger.get("alarmtext");
//                String kindOfWeather = (String) tempReiziger.get("kindOfWeather");
//
//
//
//            }
        } finally {
            // Sluit de stream
            cursor.close();
        }
    }


    public void addOne(String userId, String targetId){
        Document query1 = new Document("userId", userId);

        FindIterable<Document> result = collection.find(query1);
        boolean exists = result.iterator().hasNext();

        if (!exists){
            Document document = new Document("userId", userId).append(targetId, 1);
            collection.insertOne(document);
        }
        else {

            Document query2 = new Document("userId", userId)
                    .append(targetId, new Document("$exists", true));
            Document document = collection.find(query2).first();


            if (document != null){
                Document update = new Document("$inc", new Document(targetId, 1));
                collection.updateOne(query1, update);
            }
            else {
                Document document1 = collection.find(new Document("userId", userId)).first();

                if (document1 != null && document1.containsKey(targetId)) {

                    Document query = new Document("userId", userId)
                            .append(targetId, new Document("$exists", false));

                    // Create an update operation to set the userId field to 1 if it doesn't exist
                    Document update = new Document("$setOnInsert", new Document(targetId, 1));

                    // Execute the update operation if the userId field doesn't exist for the document
                    collection.updateOne(query, update);


                } else {
                    Document update = new Document("$inc", new Document(targetId, 1));
                    collection.updateOne(query1, update);
                    System.out.println("Column does not exist in the document.");
                }
            }
        }
    }


    public int getKissesTotal(String userId, String targetId){

        Document document = collection.find(new Document("userId", userId)).first();

        if (document != null && document.containsKey(targetId)) {
            int targetIdValue = document.getInteger(targetId);

            return targetIdValue;
        } else {
            System.out.println("userId column does not exist in the document.");
            return 69;
        }
    }




}
