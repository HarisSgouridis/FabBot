package com.jagrosh.jmusicbot.commands.music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jmusicbot.Bot;
import com.jagrosh.jmusicbot.MongoDB.MongoKiss;

public class UpdateColourCmd extends Command {

    MongoKiss mongoKiss = new MongoKiss();


    public UpdateColourCmd(Bot bot) {
        this.name = "savecolour";
        this.help = "saves your gif";
        this.aliases = bot.getConfig().getAliases(this.name);
    }

    @Override
    protected void execute(CommandEvent event) {

        String[] colorValues = event.getArgs().split(",");



            mongoKiss.changeColour(colorValues, event.getAuthor().getId());
        }
    }

