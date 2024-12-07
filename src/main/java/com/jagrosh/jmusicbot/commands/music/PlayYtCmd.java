// package com.jagrosh.jmusicbot.commands.music;

// import com.jagrosh.jdautilities.command.CommandEvent;
// import com.jagrosh.jmusicbot.Bot;
// import com.jagrosh.jmusicbot.audio.AudioHandler;
// import com.jagrosh.jmusicbot.audio.QueuedTrack;
// import com.jagrosh.jmusicbot.commands.MusicCommand;
// import com.jagrosh.jmusicbot.utils.FormatUtil;
// import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
// import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
// import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
// import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

// import net.dv8tion.jda.api.entities.Message;

// public class PlayYtCmd extends MusicCommand {
    
//     private final static String LOAD = "\uD83D\uDCE5"; // 📥
//     private final static String CANCEL = "\uD83D\uDEAB"; // 🚫

//     private final String loadingEmoji;

//     public PlayYtCmd(Bot bot) {
//         super(bot); // Call the constructor of MusicCommand
//         this.loadingEmoji = bot.getConfig().getLoading();
//         this.name = "playyt";
//         this.arguments = "<song title>";
//         this.help = "plays a YouTube video based on the provided song title";
//         this.aliases = new String[]{"playyoutube", "ytplay"};
//         this.bePlaying = false;
//     }

//     @Override
//     public void doCommand(CommandEvent event) {
//         if (event.getArgs().isEmpty()) {
//             event.replyError("Please provide a song title to search on YouTube.");
//             return;
//         }

//         String searchQuery = "ytsearch:" + event.getArgs();
//         event.reply(loadingEmoji + " Searching YouTube for `[" + event.getArgs() + "]`...", m -> {
//             bot.getPlayerManager().loadItemOrdered(event.getGuild(), searchQuery, new ResultHandler(m, event));
//         });
//     }

//     private class ResultHandler implements AudioLoadResultHandler {
//         private final Message m;
//         private final CommandEvent event;

//         private ResultHandler(Message m, CommandEvent event) {
//             this.m = m;
//             this.event = event;
//         }

//         @Override
//         public void trackLoaded(AudioTrack track) {
//             loadSingleTrack(track);
//         }

//         @Override
//         public void playlistLoaded(AudioPlaylist playlist) {
//             if (playlist.getTracks().size() == 1 || playlist.isSearchResult()) {
//                 AudioTrack singleTrack = playlist.getSelectedTrack() != null ? playlist.getSelectedTrack() : playlist.getTracks().get(0);
//                 loadSingleTrack(singleTrack);
//             } else {
//                 int count = loadPlaylist(playlist);
//                 if (playlist.getTracks().isEmpty()) {
//                     m.editMessage(FormatUtil.filter(event.getClient().getWarning() + " No tracks were found for `"
//                             + event.getArgs() + "`")).queue();
//                 } else {
//                     m.editMessage(FormatUtil.filter(event.getClient().getSuccess() + " Found a playlist with `"
//                             + playlist.getTracks().size() + "` tracks. Added to the queue!")).queue();
//                 }
//             }
//         }

//         @Override
//         public void noMatches() {
//             m.editMessage(FormatUtil.filter(event.getClient().getWarning() + " No results found for `"
//                     + event.getArgs() + "`.")).queue();
//         }

//         @Override
//         public void loadFailed(FriendlyException exception) {
//             m.editMessage(FormatUtil.filter(event.getClient().getError() + " Error loading track: "
//                     + exception.getMessage())).queue();
//         }

//         private void loadSingleTrack(AudioTrack track) {
//             AudioHandler handler = (AudioHandler) event.getGuild().getAudioManager().getSendingHandler();
//             int pos = handler.addTrack(new QueuedTrack(track, event.getAuthor())) + 1;
//             String message = FormatUtil.filter(event.getClient().getSuccess() + " Added **"
//                     + track.getInfo().title + "** (`" + FormatUtil.formatTime(track.getDuration()) + "`)"
//                     + (pos == 0 ? " to begin playing" : " to the queue at position " + pos));
//             m.editMessage(message).queue();
//         }

//         private int loadPlaylist(AudioPlaylist playlist) {
//             AudioHandler handler = (AudioHandler) event.getGuild().getAudioManager().getSendingHandler();
//             int count = 0;
//             for (AudioTrack track : playlist.getTracks()) {
//                 handler.addTrack(new QueuedTrack(track, event.getAuthor()));
//                 count++;
//             }
//             return count;
//         }
//     }
// }



package com.jagrosh.jmusicbot.commands.music;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jmusicbot.Bot;
import com.jagrosh.jmusicbot.audio.AudioHandler;
import com.jagrosh.jmusicbot.audio.QueuedTrack;
import com.jagrosh.jmusicbot.commands.MusicCommand;
import com.jagrosh.jmusicbot.utils.FormatUtil;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.entities.Message;

public class PlayYtCmd extends MusicCommand {
    
    private final static String LOAD = "\uD83D\uDCE5"; // 📥
    private final static String CANCEL = "\uD83D\uDEAB"; // 🚫

    private final String loadingEmoji;

    public PlayYtCmd(Bot bot) {
        super(bot); // Call the constructor of MusicCommand
        this.loadingEmoji = bot.getConfig().getLoading();
        this.name = "playyt";
        this.arguments = "<song title>";
        this.help = "plays a YouTube video based on the provided song title";
        this.aliases = new String[]{"playyoutube", "ytplay"};
        this.bePlaying = false;
    }

    @Override
    public void doCommand(CommandEvent event) {
        if (event.getArgs().isEmpty()) {
            event.replyError("Please provide a song title to search on YouTube.");
            return;
        }

        String searchQuery = "ytsearch:" + event.getArgs();
        event.reply(loadingEmoji + " Searching YouTube for `[" + event.getArgs() + "]`...", m -> {
            bot.getPlayerManager().loadItemOrdered(event.getGuild(), searchQuery, new ResultHandler(m, event));
        });
    }

    private class ResultHandler implements AudioLoadResultHandler {
        private final Message m;
        private final CommandEvent event;

        private ResultHandler(Message m, CommandEvent event) {
            this.m = m;
            this.event = event;
        }

        @Override
        public void trackLoaded(AudioTrack track) {
            loadSingleTrack(track);
        }

        @Override
        public void playlistLoaded(AudioPlaylist playlist) {
            if (playlist.getTracks().size() == 1 || playlist.isSearchResult()) {
                AudioTrack singleTrack = playlist.getSelectedTrack() != null ? playlist.getSelectedTrack() : playlist.getTracks().get(0);
                loadSingleTrack(singleTrack);
            } else {
                int count = loadPlaylist(playlist);
                if (playlist.getTracks().isEmpty()) {
                    m.editMessage(FormatUtil.filter(event.getClient().getWarning() + " No tracks were found for `"
                            + event.getArgs() + "`")).queue();
                } else {
                    m.editMessage(FormatUtil.filter(event.getClient().getSuccess() + " Found a playlist with `"
                            + playlist.getTracks().size() + "` tracks. Added to the queue!")).queue();
                }
            }
        }

        @Override
        public void noMatches() {
            m.editMessage(FormatUtil.filter(event.getClient().getWarning() + " No results found for `"
                    + event.getArgs() + "`.")).queue();
        }

        @Override
        public void loadFailed(FriendlyException exception) {
            if (exception.severity == FriendlyException.Severity.SUSPICIOUS) {
                m.editMessage(FormatUtil.filter(event.getClient().getWarning() + " Warning: " + exception.getMessage())).queue();
            } else {
                m.editMessage(FormatUtil.filter(event.getClient().getError() + " Error loading track: "
                        + exception.getMessage())).queue();
            }
        }

        private void loadSingleTrack(AudioTrack track) {
            AudioHandler handler = (AudioHandler) event.getGuild().getAudioManager().getSendingHandler();
            int pos = handler.addTrack(new QueuedTrack(track, event.getAuthor())) + 1;
            String message = FormatUtil.filter(event.getClient().getSuccess() + " Added **"
                    + track.getInfo().title + "** (`" + FormatUtil.formatTime(track.getDuration()) + "`)"
                    + (pos == 0 ? " to begin playing" : " to the queue at position " + pos));
            m.editMessage(message).queue();
        }

        private int loadPlaylist(AudioPlaylist playlist) {
            AudioHandler handler = (AudioHandler) event.getGuild().getAudioManager().getSendingHandler();
            int count = 0;
            for (AudioTrack track : playlist.getTracks()) {
                handler.addTrack(new QueuedTrack(track, event.getAuthor()));
                count++;
            }
            return count;
        }
    }
}
