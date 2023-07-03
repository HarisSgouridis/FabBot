package com.jagrosh.jmusicbot.commands.music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jmusicbot.Bot;
import com.jagrosh.jmusicbot.MongoDB.MongoKiss;
import net.dv8tion.jda.api.EmbedBuilder;

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


        if (event.getAuthor().getName().equals("grasparkieten")) {
            file = new File("pictures/ezgif-5-abff46f119.gif");
        } else if (event.getAuthor().getName().equals("kevinovicc")) {
            file = new File("pictures/KevinKiss.gif");
        } else if (event.getAuthor().getName().equals("bearbennie")) {
            file = new File("pictures/BennieKiss.gif");
        } else if (event.getAuthor().getName().equals("starcarcass")) {
            file = new File("pictures/CeresKiss.gif");
        } else {

            event.getGuild().getTextChannelsByName(currentChannel, true).get(0).sendMessage("I could't find you in my collection :(. So here is Haris to serve as placeholder.").queue();


            file = new File("pictures/ezgif-5-abff46f119.gif");
        }

        try {

            Color colour = new Color(255,255,255);

            EmbedBuilder embed2 = new EmbedBuilder();

            embed2.setTitle("Special delivery from " + event.getAuthor().getName(), null);
            embed2.setDescription("You've kissed this person:" + " " + "times");
            embed2.addField(event.getAuthor().getName() + " kisses " + event.getArgs() + " <33", "", false).setImage("attachment://" + file.getName());
            embed2.setColor(colour);
            embed2.setFooter("(ps: I am in your walls)", event.getGuild().getMembersByName("grasparkieten", true).get(0).getUser().getAvatarUrl());



            for (int i = 0; i < event.getGuild().getMembers().size(); i++) {
                if (event.getGuild().getMembers().get(i).getUser().getName().equals(event.getArgs())) {

                    mongoKiss.addOne(event.getAuthor().getId(), event.getGuild().getMembers().get(i).getUser().getId());


                    if (event.getAuthor().getName().equals("starcarcass")){
                      colour = new Color(255, 188, 217);
                    }
                    else if (event.getAuthor().getName().equals("grasparkieten")){
                        colour = new Color(255,183,197);
                    }


                    EmbedBuilder embed = new EmbedBuilder();

                    embed.setTitle("Special delivery from " + event.getAuthor().getName(), null);
                    embed.setDescription("You've kissed this person: " + mongoKiss.getKissesTotal(event.getAuthor().getId(), event.getGuild().getMembers().get(i).getUser().getId()) + " times");
                    embed.addField(event.getAuthor().getName() + " kisses " + event.getArgs() + " <33", event.getGuild().getMembersByName(event.getArgs(), true).get(0).getAsMention(), false).setImage("attachment://" + file.getName());
                    embed.setColor(colour);
                    embed.setFooter("(ps: I am in your walls)", event.getGuild().getMembersByName("grasparkieten", true).get(0).getUser().getAvatarUrl());

                    event.getChannel().sendMessage(embed.build()).addFile(file).queue();
                    return;
                }
            }

            System.out.println("attachment://" + file.getName());


            event.getChannel().sendMessage(embed2.build()).addFile(file).queue();

        } catch (Exception e) {
            event.getGuild().getTextChannelsByName(currentChannel, true).get(0).sendMessage("Ehmmm.... Daar ging iets mis").queue();
            e.printStackTrace();
        }
    }
}
