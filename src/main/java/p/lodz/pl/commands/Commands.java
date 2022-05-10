package p.lodz.pl.commands;

import lombok.Getter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import p.lodz.pl.model.Deck;

public class Commands extends ListenerAdapter {

    @Getter
    private final Deck deck;

    public Commands() {
        this.deck = new Deck();
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(event.getAuthor().isBot()){
            return;
        }

        StringBuilder builder = new StringBuilder();
        builder.append(event.getAuthor().getName()).append(": ")
                .append(event.getMessage().getContentDisplay());
        System.out.println(builder);

        if (event.getMessage().getContentDisplay().equals("!deck")) {
            event.getChannel().sendMessage(deck.toString()).queue();
            event.getAuthor().openPrivateChannel()
                    .flatMap(privateChannel -> privateChannel.sendMessage(deck.toString()))
                    .queue();
        }
    }
}
