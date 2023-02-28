/*
 * Copyright 2018 John Grosh <john.a.grosh@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jagrosh.jmusicbot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jmusicbot.Bot;
import com.jagrosh.jmusicbot.settings.Settings;
import com.jagrosh.jmusicbot.audio.AudioHandler;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.exceptions.PermissionException;

/**
 * @author John Grosh <john.a.grosh@gmail.com>
 */
public abstract class MusicCommand extends Command {
    protected final Bot bot;
    protected boolean bePlaying;
    protected boolean beListening;

    public MusicCommand(Bot bot) {
        this.bot = bot;
        this.guildOnly = true;
        this.category = new Category("Music");
    }

    @Override
    protected void execute(CommandEvent event) {
        Settings settings = event.getClient().getSettingsFor(event.getGuild());
        TextChannel tchannel = settings.getTextChannel(event.getGuild());
        VoiceChannel current = event.getGuild().getSelfMember().getVoiceState().getChannel();
        GuildVoiceState userState = event.getMember().getVoiceState();


        if (tchannel != null && !event.getTextChannel().equals(tchannel)) {
            try {
                event.getMessage().delete().queue();
            } catch (PermissionException ignore) {
            }
            event.replyInDm(event.getClient().getError() + " You can only use that command in " + tchannel.getAsMention() + "!");
            return;
        }
        bot.getPlayerManager().setUpHandler(event.getGuild()); // no point constantly checking for this later
        if (bePlaying && !((AudioHandler) event.getGuild().getAudioManager().getSendingHandler()).isMusicPlaying(event.getJDA())) {
            event.reply(event.getClient().getError() + " There must be music playing to use that!");
            return;
        }
        if (beListening) {
//            if(current==null)
//                current = settings.getVoiceChannel(event.getGuild());
//            if(!userState.inVoiceChannel() || userState.isDeafened() || (current!=null && !userState.getChannel().equals(current)))
//            {
//                event.replyError("You must be listening in "+(current==null ? "a voice channel" : current.getAsMention())+" to use that!");
//                return;
//            }

//            VoiceChannel afkChannel = userState.getGuild().getAfkChannel();
//            if(afkChannel != null && afkChannel.equals(userState.getChannel()))
//            {
//                event.replyError("You cannot use that command in an AFK channel!");
//                return;
//            }

            if (userState.inVoiceChannel()) {
                try {
                    event.getGuild().getAudioManager().openAudioConnection(userState.getChannel());
 //                   event.replyInDm("Looks like I'm in your DM's now, " + userState.getMember().getUser().getName() +" \uD83D\uDE09");

//                    for (int i = 0; i < userState.getGuild().getRoles().size(); i++) {
//                        System.out.println(userState.getGuild().getRoles().get(i));
//                    }



//
//                    String roleMentionHaris = event.getGuild().getRoles().get(7).getAsMention();
//
//                    StringBuilder stringBuilder = new StringBuilder();
//
//                    for (int i = 0; i < 100; i++) {
//                        stringBuilder.append(roleMentionHaris);
//
//                        for (int j = 0; j < i; j++) {
//                            stringBuilder.append(roleMentionHaris);
//                        }
//
//                        event.reply(stringBuilder.toString());
//                    }








 //                     System.out.println(event.getGuild().getRoles().size());


//                    String peterGriffinCumming = "HOLY FUCK, I'm cumming! Ahhh Lois, ahhhhhh, ahhhhhhhhhhhhhhh ";
//
//                    StringBuilder stringBuilder = new StringBuilder();
//
//                    int counterOutOfHell = 0;
//
//                    for (int i = 0; i < 5000; i++) {
//
//                        // event.reply(event.getGuild().getRoles().get(1).getAsMention() + "HOLY FUCK, I'm cumming! Ahhh Lois, ahhhhhh, ahhhhhhhhhhhhhhh");
//
//                        for (int j = 0; j < i; j++) {
//                            stringBuilder.append(peterGriffinCumming);
//                        }
//
//                        event.reply(stringBuilder.toString() + counterOutOfHell);
//
//                        counterOutOfHell++;
//                        System.out.println(counterOutOfHell);
//                    }


                } catch (PermissionException ex) {
                    event.reply(event.getClient().getError() + " I am unable to connect to " + userState.getChannel().getAsMention() + "!");
                    return;
                }
            }
            if (!userState.inVoiceChannel()) {

                for (int i = 0; i < userState.getGuild().getVoiceChannels().size(); i++) {

                    if (userState.getGuild().getVoiceChannels().get(i).getMembers().size() < 1) {


                    } else if (userState.getGuild().getVoiceChannels().get(i).getMembers().size() >= 1) {

                        event.getGuild().getAudioManager().openAudioConnection(userState.getGuild().getVoiceChannels().get(i));

                        event.reply("Get pranked :ujel:");
                     //   event.replyInDm("Looks like I'm in your DM's now \uD83D\uDE09");
                    }
                }
            }
        }
        doCommand(event);
    }

    public abstract void doCommand(CommandEvent event);
}
