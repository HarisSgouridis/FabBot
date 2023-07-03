package com.jagrosh.jmusicbot.commands.music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jmusicbot.Bot;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GoonCmd extends Command {

    private List<String> funnyImages = new ArrayList<>();



    public GoonCmd(Bot bot) {
        this.name = "goon";
        this.help = "krill yourself";
        this.aliases = bot.getConfig().getAliases(this.name);

        funnyImages.add("pictures/BasedCeres.gif");
        funnyImages.add("pictures/kanker-kanker-kat.gif");
        funnyImages.add("pictures/FunnyCatGig.gif");
        funnyImages.add("pictures/KevinsGif.gif");
        funnyImages.add("pictures/lol.gif");
        funnyImages.add("pictures/FunnyTaser.gif");
        funnyImages.add("pictures/Pranked.gif");
    }


    @Override
    protected void execute(CommandEvent event) {

        System.out.println(funnyImages.size());

        int randomIndex = (int) Math.round(Math.random() * funnyImages.size() - 1);

        System.out.println(randomIndex);

        File file = new File(funnyImages.get(randomIndex));


        String currentChannel = event.getChannel().getName();

        event.getGuild().getTextChannelsByName(currentChannel, true).get(0).sendMessage("pranked").addFile(file).queue();
    }
}
