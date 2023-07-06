package com.jagrosh.jmusicbot.commands.music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jmusicbot.Bot;
import com.jagrosh.jmusicbot.MongoDB.MongoKiss;
import java.util.concurrent.ExecutionException;

public class UpdateKissCmd extends Command {

    MongoKiss mongoKiss = new MongoKiss();

    public UpdateKissCmd(Bot bot) {
        this.name = "savegif";
        this.help = "saves your gif";
        this.aliases = bot.getConfig().getAliases(this.name);
    }


    @Override
    protected void execute(CommandEvent event) {

        System.out.println(event.getMessage().getAttachments().get(0).isImage());

        try {

            if (event.getMessage().getAttachments().get(0).isImage()){

                mongoKiss.readBytesFromFile(event.getMessage().getAttachments().get(0).downloadToFile().get());

                mongoKiss.addOrUpdateGif(event.getAuthor().getId(), mongoKiss.readBytesFromFile(event.getMessage().getAttachments().get(0).downloadToFile().get()));

                event.reply("I have saved your gif in my database <3");
            }
            else {
                event.reply(event.getAuthor().getName() + "KRIJG DE HOERENTERING IK ZEI TOCH ALLEEN .GIF FILES??!!1!1!!");

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}
