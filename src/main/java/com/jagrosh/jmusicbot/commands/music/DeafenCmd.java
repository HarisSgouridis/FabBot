package com.jagrosh.jmusicbot.commands.music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jmusicbot.Bot;
import net.dv8tion.jda.api.entities.Member;

import java.util.Arrays;

public class DeafenCmd extends Command {


    public DeafenCmd(Bot bot){
        this.name = "deafen";
        this.help = "deafen lol";
        this.aliases = bot.getConfig().getAliases(this.name);
    }

    @Override
    protected void execute(CommandEvent event) {
   //     Member userToBeDeafened = event.getGuild().getMembersByName(event.getArgs(), true).get(0);

        for (int i = 0; i < this.botPermissions.length; i++) {
            System.out.println(this.botPermissions[i].getName());
        }

     //   userToBeDeafened.deafen(true);
    }
}
