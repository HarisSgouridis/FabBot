package com.jagrosh.jmusicbot.commands.music;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jmusicbot.Bot;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherCmd extends Command {

    public String location;
    public String time;
    public String temperature;
    public String winddirection;
    public String alarmtext;
    public String expectedWeather;
    public String kindOfWeather;


    public WeatherCmd(Bot bot) {
        this.name = "getWeather";
        this.help = "weather?";
        this.aliases = bot.getConfig().getAliases(this.name);
    }


    private String getWebsite(String urlString) throws IOException {
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


            this.temperature = (rootNode.path("temp").asText());
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


    @Override
    protected void execute(CommandEvent event) {
        try {
            // channelToTest.sendMessage(this.getWebsite(event.getArgs()));

            String rawJson = this.getWebsite("https://weerlive.nl/api/json-data-10min.php?key=078bf26a41&locatie=" + event.getArgs());

            String trimmedJson = rawJson.substring(15);
            trimmedJson = trimmedJson.substring(0, trimmedJson.length() - 2);


            this.parseJson(trimmedJson);


            if (event.getArgs().equals("Hell")) {
                this.temperature = "(999999999999999999999999999999999)^∞";
            }

            if (alarmtext.equals("")){
                event.reply("Hey " + event.getAuthor().getName() + "!\n\n" + "Location: " + this.location + "\n" + "temperature: " + this.temperature + "°C\n" + "Current wind direction: " + this.winddirection + "\nKind of weather: " + this.kindOfWeather + "\n\nExpected weather: " + this.expectedWeather + "\n\nTimestamp: " +
                        this.time);
            }
            else {
                event.reply("Hey " + event.getAuthor().getName() + "!\n\n" + "Location: " + this.location + "\n" + "temperature: " + this.temperature + "°C\n" + "Current wind direction: " + this.winddirection + "\nKind of weather: " + this.kindOfWeather + "\n\nImportant weather notice!: \n" + this.alarmtext + "\n\nExpected weather: " + this.expectedWeather + "\n\nTimestamp: " +
                        this.time);
            }

        } catch (IOException e) {
            event.reply("I just shat myself :(");
            e.printStackTrace();
        }
    }
}
