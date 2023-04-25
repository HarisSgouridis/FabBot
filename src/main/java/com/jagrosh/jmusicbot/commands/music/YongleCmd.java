package com.jagrosh.jmusicbot.commands.music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jmusicbot.Bot;
import java.util.ArrayList;

public class YongleCmd extends Command {

    public static int MAX_MESSAGES = 2;
    private int loopsize = 30;

    public YongleCmd(Bot bot) {
        this.name = "yongle";
        this.help = "yongle??????";
        this.aliases = bot.getConfig().getAliases(this.name);
    }


    @Override
    protected void execute(CommandEvent event) {

        String yongle = "yongle";

        System.out.println(event.getArgs());

        String message = event.getArgs();


        String splitted[] =message.split(",");
        String messageInstance = splitted[0];

        if (event.getArgs() == null){
            message = yongle;
        }


        for (int i = 0; i < loopsize; i++) {

            if (i == 0) {
                for (int j = 0; j < MAX_MESSAGES ; j++) {
                    event.getGuild().getTextChannelsByName("spam-requiem", true).get(0).sendMessage(message).queue(m -> {
                    });
                }
            }

            if (i < loopsize / 2) {
                message = message + ", " + messageInstance;
            } else {
                message = message.substring(messageInstance.length());
            }


            ArrayList<String> messages = splitMessage(message);
            for (int j = 0; j < MAX_MESSAGES && j < messages.size(); j++) {
                event.getGuild().getTextChannelsByName("spam-requiem", true).get(0).sendMessage(messages.get(j)).queue(m -> {
                });
            }
        }


        if (this.loopsize > 1) {
            this.loopsize = this.loopsize / 2;
            this.execute(event);
        }
        this.loopsize = 30;
    }


    public static ArrayList<String> splitMessage(String stringtoSend) {
        ArrayList<String> msgs = new ArrayList<>();
        if (stringtoSend != null) {
            stringtoSend = stringtoSend.replace("@everyone", "@\u0435veryone").replace("@here", "@h\u0435re").trim();
            while (stringtoSend.length() > 2000) {
                int leeway = 2000 - (stringtoSend.length() % 2000);
                int index = stringtoSend.lastIndexOf("\n", 2000);
                if (index < leeway)
                    index = stringtoSend.lastIndexOf(" ", 2000);
                if (index < leeway)
                    index = 2000;
                String temp = stringtoSend.substring(0, index).trim();
                if (!temp.equals(""))
                    msgs.add(temp);
                stringtoSend = stringtoSend.substring(index).trim();
            }
            if (!stringtoSend.equals(""))
                msgs.add(stringtoSend);
        }
        return msgs;
    }


}
