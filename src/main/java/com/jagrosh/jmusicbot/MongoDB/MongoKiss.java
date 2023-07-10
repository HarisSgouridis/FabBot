package com.jagrosh.jmusicbot.MongoDB;

import com.jagrosh.jmusicbot.JMusicBot;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClients;
import com.mongodb.client.model.Filters;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.bson.types.Binary;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

public class MongoKiss extends Mongo {


    public MongoKiss() {

        connect();
        load();
        registerCodecs();
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

    }


    public void addOne(String userId, String targetId) {

        Document query1 = new Document("userId", userId);

        FindIterable<Document> result = collection.find(query1);
        boolean exists = result.iterator().hasNext();

        if (!exists) {
            Document document = new Document("userId", userId).append(targetId, 1);
            collection.insertOne(document);
        } else {

            Document query2 = new Document("userId", userId)
                    .append(targetId, new Document("$exists", true));
            Document document = collection.find(query2).first();


            if (document != null) {
                Document update = new Document("$inc", new Document(targetId, 1));
                collection.updateOne(query1, update);
            } else {
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


    public int getKissesTotal(String userId, String targetId) {

        Document document = collection.find(new Document("userId", userId)).first();

        if (document != null && document.containsKey(targetId)) {
            int targetIdValue = document.getInteger(targetId);

            return targetIdValue;
        } else {
            System.out.println("userId column does not exist in the document.");
            return 69;
        }
    }

    public byte[] readBytesFromFile(File file) {
        try {

            System.out.println(Files.readAllBytes(file.toPath()));


            // Create a document to store the GIF data

            return Files.readAllBytes(file.toPath());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Binary getGifData(String targetId) {
        // Create a filter for the user ID and the "gifData" field
        Bson filter = and(eq("userId", targetId), exists("gifData"));

        // Find the document matching the filter
        Document document = collection.find(filter).first();

        if (document != null) {
            Binary gifData = document.get("gifData", Binary.class);

            if (gifData != null) {
                byte[] gifBytes = gifData.getData();

                // Perform further operations with the gifData, such as converting it to a GIF file

                System.out.println("gifData retrieved successfully.");

                fromBinaryToGif(gifBytes, targetId);


                return gifData;
            } else {
                System.out.println("gifData is null.");
            }
        } else {
            System.out.println("User not found.");
        }
        return null;
    }

    public void addOrUpdateGif(String targetId, byte[] bytes) {

        // Create a filter for the user ID and the "gifData" field
        Bson filter = and(eq("userId", targetId), exists("gifData"));

        // Find the document matching the filter
        Document document = collection.find(filter).first();


        if (document != null) {
            Document userFilter = new Document("userId", targetId);
            Document update = new Document("$set", new Document("gifData", bytes));

            collection.updateOne(userFilter, update);

            fromBinaryToGif(bytes, targetId);

        } else {

            Document userFilter = new Document("userId", targetId);
            Document update = new Document("$set", new Document("gifData", bytes));

            collection.updateOne(userFilter, update);

            fromBinaryToGif(bytes, targetId);

            System.out.println("The 'gifData' field does not exist in the document for user ID: " + targetId);
        }

    }

    public void fromBinaryToGif(byte[] gifdata, String userId) {

        String filePath = "C:\\Users\\theoh\\IdeaProjects\\MusicBot\\pictures\\" + userId + ".gif";

        try {
            // Write the binary data to the file
            FileOutputStream outputStream = new FileOutputStream(filePath);
            outputStream.write(gifdata);
            outputStream.close();

            System.out.println("GIF file created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeColour(Integer[] colour, String targetId) {
        // Create a filter for the user ID and the "colour" field
        Bson filter = and(eq("userId", targetId), exists("colour"));

        // Find the document matching the filter
        Document document = collection.find(filter).first();

        if (document != null) {
            Document userFilter = new Document("userId", targetId);
            Document update = new Document("$set", new Document("colour", colour));

            collection.updateOne(userFilter, update);
        } else {
            Document userFilter = new Document("userId", targetId);

            Document update = new Document("$set", new Document("colour", colour));

            collection.updateOne(userFilter, update);

            System.out.println("The 'colour' field does not exist in the document for user ID: " + targetId);
        }
    }

    public Integer[] getColour(String userId) {
        Document userDocument = collection.find(Filters.eq("userId", userId)).first();

        // Retrieve the color value from the document
        List<Integer> colourList = userDocument.get("colour", List.class);
        if (colourList != null) {
            Integer[] colour = colourList.toArray(new Integer[0]);
            return colour;
        }
        else{
            return new Integer[]{255, 255,255};
        }
    }

    private void registerCodecs() {
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
                CodecRegistries.fromProviders(new RGBArrayCodecProvider()),
                MongoClient.getDefaultCodecRegistry()
        );
        collection = collection.withCodecRegistry(codecRegistry);
    }

    private static class RGBArrayCodec implements Codec<Integer[]> {

        @Override
        public Integer[] decode(BsonReader reader, DecoderContext decoderContext) {
            reader.readStartArray();
            Integer[] rgbArray = new Integer[3];
            for (int i = 0; i < 3; i++) {
                rgbArray[i] = reader.readInt32();
            }
            reader.readEndArray();
            return rgbArray;
        }


        @Override
        public void encode(BsonWriter writer, Integer[] rgbArray, EncoderContext encoderContext) {
            writer.writeStartArray();
            for (int value : rgbArray) {
                writer.writeInt32(value);
            }
            writer.writeEndArray();
        }

        @Override
        public Class<Integer[]> getEncoderClass() {
            return Integer[].class;
        }
    }

    private static class RGBArrayCodecProvider implements CodecProvider {
        @Override
        public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
            if (clazz.isArray() && clazz.getComponentType().equals(Integer.class)) {
                return (Codec<T>) new RGBArrayCodec();
            }
            return null;
        }
    }
}

