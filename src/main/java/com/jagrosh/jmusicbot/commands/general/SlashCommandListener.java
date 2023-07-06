package com.jagrosh.jmusicbot.commands.general;

import com.jagrosh.jmusicbot.commands.music.UpdateKissCmd;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class SlashCommandListener extends ListenerAdapter {

   // UpdateKissCmd updateKissCmd = new UpdateKissCmd();

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (event.getName().equals("savegif")) {


            String parameterValue = event.getOption("file").getAsString();
            event.reply("Parameter value: " + parameterValue).queue();

            event.getOption("file").getAsMessageChannel().retrieveMessageById(event.getInteraction().getId()).queue(message -> {
                List<Message.Attachment> attachments = message.getAttachments();

                System.out.println(attachments.get(0));

            });
        }
    }
}

