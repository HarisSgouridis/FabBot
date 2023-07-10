package com.jagrosh.jmusicbot.commands.music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jmusicbot.Bot;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.List;

public class PenisCmd extends Command {

    public PenisCmd(Bot bot) {
        this.name = "penis";
        this.help = "penis?!?!?!?!?!?!";
        this.aliases = bot.getConfig().getAliases(this.name);
    }

    public static int MAX_MESSAGES = 2;

    @Override
    protected void execute(CommandEvent event) {

        List<User> blacklistedUsers = new ArrayList();

        blacklistedUsers.add(event.getGuild().getMembersByName("JelskiGaming", true).get(0).getUser());
        blacklistedUsers.add(event.getGuild().getMembersByName("HonHonKandelaar", true).get(0).getUser());
//        blacklistedUsers.add(event.getGuild().getMembersByName("joannesmh", true).get(0).getUser());

//        blacklistedUsers.add(event.getGuild().getMembersByName("Kevinović", true).get(0).getUser());
        blacklistedUsers.add(event.getGuild().getMembersByName("Martin Van der Kolk", true).get(0).getUser());
//        blacklistedUsers.add(event.getGuild().getMembersByName("StarCarcass", true).get(0).getUser());
//        blacklistedUsers.add(event.getGuild().getMembersByName("taro_bean", true).get(0).getUser());
//        blacklistedUsers.add(event.getGuild().getMembersByName("bearbennie", true).get(0).getUser());
//        blacklistedUsers.add(event.getGuild().getMembersByName("Lavenderforlove", true).get(0).getUser());
        blacklistedUsers.add(event.getGuild().getMembersByName(".mediolanum", true).get(0).getUser());

        String message = event.getArgs();

        if (event.getArgs() == null) {
            message = "(noArgException)";
        }

        String message2 = "https://www.meme-arsenal.com/memes/18934dc45129ade6279df13fe733e412.jpg";

        for (int i = 0; i < event.getGuild().getMemberCount(); i++) {

            ArrayList<String> messages = splitMessage(message);
            for (int j = 0; j < MAX_MESSAGES && j < messages.size(); j++) {

                if (event.getGuild().getMembers().get(i).getUser() == event.getSelfUser() || blacklistedUsers.contains(event.getGuild().getMembers().get(i).getUser())) {
                    System.out.println("Hey, that's me!!! (or someone from the blacklist)");
                    System.out.println(event.getGuild().getMembers().get(i).getUser() + " blacklisted user or self");
                } else {
                    String finalMessage = message;
                    event.getGuild().getMembers().get(i).getUser().openPrivateChannel().queue(pc -> pc.sendMessage("This is a public announcement from the user: " + event.getAuthor().getName() + ". From the server: " + event.getGuild().getName() + "\n\n" + finalMessage).queue());
                    //    event.getGuild().getMembers().get(i).getUser().openPrivateChannel().queue(pc -> pc.sendMessage(message2).queue());

                    System.out.println(event.getGuild().getMembers().get(i).getUser());
                }
            }
        }
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
