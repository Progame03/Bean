package at.xirado.bean.event;

import at.xirado.bean.Bean;
import at.xirado.bean.data.GuildData;
import at.xirado.bean.data.GuildManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;


public class MessageCreateListener extends ListenerAdapter
{
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event)
    {
        if (GuildJoinListener.isGuildBanned(event.getGuild().getIdLong()))
            return;
        if (event.isWebhookMessage() || event.getAuthor().isBot()) return;
        Member member = event.getMember();
        if (member == null) return;
        String content = event.getMessage().getContentRaw();
        GuildData guildData = GuildManager.getGuildData(event.getGuild());
        String prefix = guildData.getPrefix();
        String[] args = content.split("\\s+");
        if (args.length == 1 && event.getMessage().getMentionedUsers().contains(event.getJDA().getSelfUser()) && event.getMessage().getReferencedMessage() == null)
        {
            event.getMessage().reply("<a:ping:818580038949273621>")
                    .mentionRepliedUser(false).queue(s -> {}, e -> {});
            return;
        }
        if (content.startsWith(prefix))
        {
            Bean.getInstance().getCommandHandler().handleCommandFromGuild(event);
        }
    }
}
