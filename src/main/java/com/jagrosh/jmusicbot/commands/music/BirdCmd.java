package com.jagrosh.jmusicbot.commands.music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jmusicbot.Bot;
import net.dv8tion.jda.api.entities.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Arrays;

public class BirdCmd extends Command {

    File birbFile = new File("![](../../assets/concatiel.jpg)");

    private String message = "https://www.meme-arsenal.com/memes/18934dc45129ade6279df13fe733e412.jpg";

    public BirdCmd(Bot bot) {
        this.name = "getSite";
        this.help = "birbs?!?!?!?!?!?!";
        this.aliases = bot.getConfig().getAliases(this.name);
    }

private String getWebsite(String urlString) throws IOException {
    URL url = new URL(urlString);
StringBuilder  stringBuilder = new StringBuilder();

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

    @Override
    protected void execute(CommandEvent event) {

        TextChannel channelToTest = event.getGuild().getTextChannelsByName("spam-requiem", true).get(0);

        try {

           // channelToTest.sendMessage(this.getWebsite(event.getArgs()));

            event.reply(this.getWebsite(event.getArgs()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
