package com.jagrosh.jmusicbot.commands.music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jmusicbot.Bot;
import com.jagrosh.jmusicbot.MongoDB.MongoKiss;

public class UpdateColourCmd extends Command {

    MongoKiss mongoKiss = new MongoKiss();


    public UpdateColourCmd(Bot bot) {
        this.name = "savecolour";
        this.help = "saves your colour";
        this.aliases = bot.getConfig().getAliases(this.name);
    }

    @Override
    protected void execute(CommandEvent event) {

        String[] colorValues = event.getArgs().split(",");


        int red = Integer.valueOf(colorValues[0]);
        int green = Integer.valueOf(colorValues[1]);
        int blue = Integer.valueOf(colorValues[2]);


        Integer[] rgbColours = {red, green, blue};


        try {
            mongoKiss.changeColour(rgbColours, event.getAuthor().getId());
            event.reply("Your colour values have been succesfully added to my database <3");
        } catch (Exception e) {
            event.reply("Welll.......... Something went wrong here");
            e.printStackTrace();
        }
    }
    }

