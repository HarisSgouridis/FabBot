package com.jagrosh.jmusicbot.commands.music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jmusicbot.Bot;
import com.jagrosh.jmusicbot.MongoDB.MongoKiss;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.io.File;

public class KissCmd extends Command {

    MongoKiss mongoKiss = new MongoKiss();

    public KissCmd(Bot bot) {
        this.name = "kiss";
        this.help = "Haris and FabBot loves you <3";
        this.aliases = bot.getConfig().getAliases(this.name);
    }


    @Override
    protected void execute(CommandEvent event) {

        String currentChannel = event.getChannel().getName();

        File file;


      File file1 = new File("pictures/"+event.getAuthor().getId()+ ".gif");

        if (!file1.exists()){
            file = new File("pictures/Pranked.gif");
        }
        else {
            file = file1;
        }

        try {

            Color colour = new Color(255, 255, 255);




            EmbedBuilder embed2 = new EmbedBuilder();

            embed2.setTitle("Special delivery from " + event.getAuthor().getName(), null);
            embed2.addField(event.getAuthor().getName() + " kisses " + event.getArgs() + " <33", "", false).setImage("attachment://" + file.getName());
            embed2.setColor(colour);
            embed2.setFooter("(ps: I am in your walls)", event.getGuild().getMembersByName("grasparkieten", true).get(0).getUser().getAvatarUrl());

            if (!file1.exists()){
                embed2.setDescription("You do not have a .gif set yet! (Use \"@FabBot savegif\" and add a .gif as an attachment to save your gif <333)");
            }

            if (event.getGuild().getMembersByName(event.getArgs(), true).size() > 0) {
                User user = event.getGuild().getMembersByName(event.getArgs(), true).get(0).getUser();

                if (user.equals(event.getAuthor())) {
                    event.getGuild().getTextChannelsByName(currentChannel, true).get(0).sendMessage("Stop trying to kiss yourself you lonely shit, that's not what I made this command for. Love, -Haris <3333").queue();
                    return;
                }

                mongoKiss.addOne(event.getAuthor().getId(), event.getGuild().getMembersByName(event.getArgs(), true).get(0).getUser().getId());

                EmbedBuilder embed = new EmbedBuilder();

                embed.setTitle("Special delivery from " + event.getAuthor().getName(), null);
                embed.setDescription("You've kissed this person: " + mongoKiss.getKissesTotal(event.getAuthor().getId(), event.getGuild().getMembersByName(event.getArgs(), true).get(0).getUser().getId()) + " times");
                embed.addField(event.getAuthor().getName() + " kisses " + event.getArgs() + " <33", event.getGuild().getMembersByName(event.getArgs(), true).get(0).getAsMention(), false).setImage("attachment://" + file.getName());
                embed.setColor(colour);
                embed.setFooter("(ps: I am in your walls)", event.getGuild().getMembersByName("grasparkieten", true).get(0).getUser().getAvatarUrl());

                if (!file1.exists()){
                    embed.setDescription("You do not have a .gif set yet! (Use \"@FabBot savegif\" and add a .gif as an attachment to save your gif <333)");
                }

                event.getChannel().sendMessage(embed.build()).addFile(file).queue();
                return;
            } else {
                System.out.println("attachment://" + file.getName());

                event.getChannel().sendMessage(embed2.build()).addFile(file).queue();
            }


        } catch (Exception e) {
            event.getGuild().getTextChannelsByName(currentChannel, true).get(0).sendMessage("Ehmmm.... Daar ging iets mis").queue();
            e.printStackTrace();
        }
    }
}
