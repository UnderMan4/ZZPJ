package p.lodz.pl.bot.commands;

import lombok.Getter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import p.lodz.pl.logic.exceptions.CannotJoinGame;
import p.lodz.pl.logic.model.Deck;
import p.lodz.pl.logic.model.Hand;
import p.lodz.pl.logic.model.Player;
import p.lodz.pl.logic.model.Table;

public class Commands extends ListenerAdapter {

    @Getter
    private final Deck deck;

    @Getter
    private final Table table;

    public Commands() {
        this.table = new Table();
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


        if (event.getMessage().getContentDisplay().equals("!deck")) {
            event.getChannel().sendMessage(deck.toString()).queue();
            event.getAuthor().openPrivateChannel()
                    .flatMap(privateChannel -> privateChannel.sendMessage(deck.toString()))
                    .queue();
        }

        if (event.getMessage().getContentDisplay().equals("!init")) {
            event.getChannel().sendMessage("Rozpoczynamy gre! Uzyj polecenia !join aby dolaczyc do gry.").queue();
            table.initGame();
        }

        if (event.getMessage().getContentDisplay().equals("!join")) {
            Player player = new Player(event.getAuthor().getName(), 1000, 0, new Hand());
            try {
//                check if player already joined
                table.joinGame(player);
                event.getChannel().sendMessage("Udalo sie dolaczyc do gry.").queue();
                System.out.println(table);
            } catch (CannotJoinGame e) {
                event.getChannel().sendMessage("Nie udalo sie dolaczyc do gry.").queue();
                e.printStackTrace();
            }
        }

        if (event.getMessage().getContentDisplay().equals("!start")) {
            event.getChannel().sendMessage("Rozpoczynamy gre!").queue();
            table.startGame();
        }
    }


}
