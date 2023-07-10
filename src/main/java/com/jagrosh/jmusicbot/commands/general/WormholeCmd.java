package com.jagrosh.jmusicbot.commands.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jmusicbot.Bot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class WormholeCmd extends Command {

    private final Bot bot;

    public WormholeCmd(Bot bot) {
        this.name = "connect";
        this.help = "connects many servers. Only works if FabBot is included in the servers you are trying to reach, of course";
        this.aliases = bot.getConfig().getAliases(this.name);
        this.bot = bot;
    }

    @Override
    protected void execute(CommandEvent event) {

        String[] argumentValues = event.getArgs().split(",");

        if (argumentValues.length < 2) {
            event.replyError("Invalid usage! Please provide both the guild name and the message.");
            return;
        }

        String targetGuildName = argumentValues[0];
        String messageContent = argumentValues[1];

        Guild targetGuild = bot.getJDA().getGuildsByName(targetGuildName, true).stream()
                .findFirst()
                .orElse(null);

        if (targetGuild == null) {
            event.replyError("Guild not found.");
            return;
        }

        TextChannel communicationChannel = targetGuild.getTextChannels().stream()
                .filter(channel -> channel.getName().equalsIgnoreCase("wormhole"))
                .findFirst()
                .orElse(null);

        if (communicationChannel == null) {
            event.replyError("Communication channel 'wormhole' not found in the target guild.");
            return;
        }

        String senderGuildName = event.getGuild().getName();
        String senderUsername = event.getAuthor().getName();

        String message = "------------------------------------------------------------------------------------\n"
                + "This is a message from the user: " + senderUsername + ". From the server: " + senderGuildName + "\n\n"
                + messageContent + "\n"
                + "------------------------------------------------------------------------------------";

        communicationChannel.sendMessage(message).queue(
                success -> event.reply("Message sent successfully."),
                error -> event.replyError("Failed to send message.")
        );
    }
}
