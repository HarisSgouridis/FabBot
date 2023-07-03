package com.jagrosh.jmusicbot.commands.music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jmusicbot.Bot;

import java.io.File;

public class HugCmd extends Command {

    public HugCmd(Bot bot) {
        this.name = "hug";
        this.help = "Haris and FabBot loves you <3";
        this.aliases = bot.getConfig().getAliases(this.name);
    }

    @Override
    protected void execute(CommandEvent event) {

        File file;

        if (event.getAuthor().getName() == "grasparkieten"){
            file = new File("pictures/HarisHug.gif");
        }
        else {
            file = new File("pictures/HarisHug.gif");
        }


        String currentChannel = event.getChannel().getName();

        event.getGuild().getTextChannelsByName(currentChannel, true).get(0).sendMessage( event.getAuthor().getName() +  " hugs " + event.getArgs() + " <33").addFile(file).queue();

    }
}
