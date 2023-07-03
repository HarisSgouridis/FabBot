package com.jagrosh.jmusicbot.commands.general;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jagrosh.jmusicbot.Bot;
import com.jagrosh.jmusicbot.MongoDB.MongoTemperatureInstance;
import com.jagrosh.jmusicbot.utils.TemperatureInstance;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class WeatherInformer {

    MongoTemperatureInstance mongoTemperatureInstance;

    private String location;
    private String time;
    private double temperature;
    private String winddirection;
    private String alarmtext;
    private String expectedWeather;
    private String kindOfWeather;

    private double median = 0.123456789;
    private double mean = 0.123456789;
    private double minimumTemp = 0.123456789;
    private double maximumTemp = 0.123456789;
    private double biggestDayDiffrence;

    File harisImage = new File("pictures/20230702_105241.jpg");
    File bugi = new File("pictures/budgie-tennis-ball.gif");


    private static int counter = 0;

    public WeatherInformer(Bot bot) {
        mongoTemperatureInstance = new MongoTemperatureInstance();
        this.bot = bot;
    }

    private Bot bot;

    public String getWebsite(String urlString) throws IOException {
        URL url = new URL(urlString);
        StringBuilder stringBuilder = new StringBuilder();

        // open a connection to the URL
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // read the response from the connection
        InputStream inputStream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }

        // close the connection
        connection.disconnect();
        return stringBuilder.toString();
    }

    public void parseJson(String jsonString) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonString);


            this.temperature = (rootNode.path("temp").asDouble());
            this.time = (rootNode.path("time").asText());
            this.location = (rootNode.path("plaats").asText());
            this.winddirection = (rootNode.path("windr").asText());
            this.alarmtext = (rootNode.path("alarmtxt").asText());
            this.expectedWeather = (rootNode.path("verw").asText());
            this.kindOfWeather = (rootNode.path("samenv").asText());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void execute(MessageReceivedEvent event, JDA jda) {


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {

                int minute = LocalTime.now().getMinute();
                int hour = LocalTime.now().getHour();
                int second = LocalTime.now().getSecond();

                if (hour == 0 && minute == 0) {
                    minimumTemp = temperature;
                    maximumTemp = temperature;

                    System.out.println("Reset the values succesfully!");
                }

                if ((minute == 14 || minute == 24 || minute == 34 || minute == 44 || minute == 54 || minute == 4) && second <= 5) {

                    System.out.println("Time is right!");
                    System.out.println(minute);
                    System.out.println(second);

                    biggestDayDiffrence = maximumTemp - minimumTemp;

                    String rawJson = null;
                    try {
                        rawJson = getWebsite("https://weerlive.nl/api/json-data-10min.php?key=demo&locatie=Amsterdam");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String trimmedJson = rawJson.substring(15);
                    trimmedJson = trimmedJson.substring(0, trimmedJson.length() - 2);


                    parseJson(trimmedJson);

                    if (temperature < minimumTemp || minimumTemp == 0.123456789) {
                        minimumTemp = temperature;
                    }

                    if (temperature > maximumTemp || maximumTemp == 0.123456789) {
                        maximumTemp = temperature;
                    }

                    for (int i = 1; i < jda.getGuilds().size(); i++) {

                        try {


                            MessageHistory history = MessageHistory.getHistoryFromBeginning(jda.getGuilds().get(i).getTextChannelsByName("Weather-channel", true).get(0)).complete();
                            List<Message> mess = history.getRetrievedHistory();
                            for (Message m : mess) {
                                m.delete().queue();
                            }


                            if (alarmtext.equals("")) {
                                jda.getGuilds().get(i).getTextChannelsByName("weather-channel", true).get(0).sendMessage("Hey" + "!\n\n" + "Location: " + location + "\n" + "temperature: " + temperature + "°C\n" + "Current wind direction: " + winddirection + "\nKind of weather: " + kindOfWeather + ": " + "\n\nExpected weather: " + expectedWeather + "\n\nTimestamp: " +
                                        time).queue();
                            } else {
                                jda.getGuilds().get(i).getTextChannelsByName("weather-channel", true).get(0).sendMessage("Hey" + "!\n\n" + "Location: " + location + "\n" + "temperature: " + temperature + "°C\n" + "Current wind direction: " + winddirection + "\nKind of weather: " + kindOfWeather + "\n\nImportant weather notice!: \n" + alarmtext + "\n\nExpected weather: " + expectedWeather + "\n\nTimestamp: " +
                                        time).queue();
                            }

                            jda.getGuilds().get(i).getTextChannelsByName("weather-channel", true).get(0).sendMessage("Minimum temperature of the day: " + minimumTemp + "\n Maximum temperature of the day: " + maximumTemp + "\n temperature spread of the day: " + biggestDayDiffrence).queue();


                            TemperatureInstance temperatureInstance = new TemperatureInstance(location, time, temperature, winddirection, alarmtext, expectedWeather, kindOfWeather);


                            mongoTemperatureInstance.add(temperatureInstance);
                            reloadMongoList();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        }, 0, 5000); // Run every 5 seconds (5000 milliseconds)


    }

    private void reloadMongoList() {
        this.mongoTemperatureInstance.load();
    }
}

