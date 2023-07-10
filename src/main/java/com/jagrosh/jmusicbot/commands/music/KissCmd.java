package com.jagrosh.jmusicbot.commands.music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jmusicbot.Bot;
import com.jagrosh.jmusicbot.MongoDB.MongoKiss;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class KissCmd extends Command {

    String creepyHaris = "(Can I also give one as well?)";
    String lovingHaris = "(<33333)";
    String scaryHaris = "(ps: I am in your walls)";
    String meanHaris = "(Are you sure you want to give it to someone like this?)";
    String noRegrets = "(I have no regrets making this command lol)";
    String devHaris = "(Follow your favourite Haris on GitHub!: \'https://github.com/HarisSgouridis\')";
    String intent = "(ps: I am in your walls)";
    String noIntent = "(ps: I am in your walls)";
    String noWay = "(Holy shit you actually did it. This is the 1/1000 easter egg, Haris half stripping in drip. " +
            "We did the voting and this is what I had to do, but I could've never expected it to actually show here one day...." +
            "Help guess I have to explain myself now lol)";
    String love = "(I love all you guys <3)";
    String easterEgg = "(Fun fact: there is a secret easter egg that activates with a 1/1000 chance. I filmed myself doing what the most popular vote was. Wonder which day will be the day it will show and I'll hang myself lol)";


    MongoKiss mongoKiss = new MongoKiss();

    public KissCmd(Bot bot) {
        this.name = "kiss";
        this.help = "Haris and FabBot loves you <3";
        this.aliases = bot.getConfig().getAliases(this.name);
    }


    @Override
    protected void execute(CommandEvent event) {

        List<String> harisTextList = new ArrayList<>();

        harisTextList.add(creepyHaris);
        harisTextList.add(lovingHaris);
        harisTextList.add(scaryHaris);
        harisTextList.add(devHaris);
        harisTextList.add(intent);
        harisTextList.add(noIntent);
        harisTextList.add(noRegrets);
        harisTextList.add(meanHaris);
        harisTextList.add(easterEgg);
        harisTextList.add(love);


        String roll = harisTextList.get((int) Math.round(Math.random() * 9));

        int randomNumber = (int)Math.round(Math.random() * 1000);

        System.out.println(randomNumber);

        String currentChannel = event.getChannel().getName();

        File file;

        mongoKiss.getGifData(event.getAuthor().getId());

      File file1 = new File("pictures/"+event.getAuthor().getId()+ ".gif");

        if (!file1.exists()){
            file = new File("pictures/bugi.gif");
        }
        else {
            file = file1;
        }

        if (randomNumber == 374){
            roll = noWay;
        //    file = new File("pictures/susHaris.gif");
        }
        else if (randomNumber >= 324 && randomNumber <= 424){
            event.reply("You were <=50 off out of 1000 to unlocking it!");
        }


        try {
            if (event.getGuild().getMembersByName(event.getArgs(), true).size() > 0) {
                mongoKiss.addOne(event.getAuthor().getId(), event.getGuild().getMembersByName(event.getArgs(), true).get(0).getUser().getId());
            }

            Integer[] rgbValues = mongoKiss.getColour(event.getAuthor().getId());


            Color colour = new Color(rgbValues[0], rgbValues[1], rgbValues[2]);
       //     colour = null;

            EmbedBuilder embed2 = new EmbedBuilder();

            embed2.setTitle("Special delivery from " + event.getAuthor().getName(), null);
            embed2.addField(event.getAuthor().getName() + " kisses " + event.getArgs() + " <33", "", false).setImage("attachment://" + file.getName());
            embed2.setColor(colour);
            embed2.setFooter(roll, event.getGuild().getMembersByName("grasparkieten", true).get(0).getUser().getAvatarUrl());

            if (!file1.exists()){
                embed2.setDescription("You do not have a .gif set yet! (Use \"@FabBot savegif\" and add a .gif as an attachment to save your gif <333)");
            }

            if (event.getGuild().getMembersByName(event.getArgs(), true).size() > 0) {
                User user = event.getGuild().getMembersByName(event.getArgs(), true).get(0).getUser();

                if (user.equals(event.getAuthor())) {
                    event.getGuild().getTextChannelsByName(currentChannel, true).get(0).sendMessage("Stop trying to kiss yourself you lonely shit, that's not what I made this command for. Love, -Haris <3333").queue();


                    return;
                }

                EmbedBuilder embed = new EmbedBuilder();

                embed.setTitle("Special delivery from " + event.getAuthor().getName(), null);
                embed.setDescription("You've kissed " +event.getArgs()+ ": " + mongoKiss.getKissesTotal(event.getAuthor().getId(), event.getGuild().getMembersByName(event.getArgs(), true).get(0).getUser().getId()) + " times");
                embed.addField(event.getAuthor().getName() + " kisses " + event.getArgs() + " <33", event.getGuild().getMembersByName(event.getArgs(), true).get(0).getAsMention(), false).setImage("attachment://" + file.getName());
                embed.setColor(colour);
                embed.setFooter(roll, event.getGuild().getMembersByName("grasparkieten", true).get(0).getUser().getAvatarUrl());

                if (!file1.exists()){
                    embed.setDescription("You do not have a .gif set yet! (Use \"@FabBot savegif\" and add a .gif as an attachment to save your gif <333)");
                }

                event.getChannel().sendMessage(embed.build()).addFile(file).queue();
                return;
            } else {
                event.getChannel().sendMessage(embed2.build()).addFile(file).queue();
            }

            boolean deleted = file1.delete();

            if (deleted == false){
                event.reply("File didn't get deleted properly!!");
            }

        } catch (Exception e) {
            event.getGuild().getTextChannelsByName(currentChannel, true).get(0).sendMessage("Ehmmm.... Daar ging iets mis").queue();

            e.printStackTrace();
        }
        finally {





        }
    }
}
