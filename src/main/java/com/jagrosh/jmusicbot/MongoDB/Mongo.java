package com.jagrosh.jmusicbot.MongoDB;

import com.jagrosh.jmusicbot.JMusicBot;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;



public abstract class Mongo {

    protected MongoCollection<Document> collection;
    protected MongoClient mongoClient;
    protected MongoDatabase mongoDatabase;

    public Mongo() {
        connect();
    }

    // connect database
    private void connect() {

        // Heb je geen gegevens in de MainApplication staan slaat hij het maken van de verbinding over
        if (JMusicBot.getNosqlHost().equals(""))
            return;

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

    public void selectedCollection(String collection) {
        this.collection = mongoDatabase.getCollection(collection);
    }


}