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
import p.lodz.pl.logic.utils.ImageGenerator;

import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.stream.Collectors;

public class Commands extends ListenerAdapter {

    @Getter
    private final Deck deck;

    @Getter
    private final Table table;

        ImageGenerator generator = new ImageGenerator();

    public Commands() {
        this.table = new Table();
        this.deck = new Deck();
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }


        StringBuilder builder = new StringBuilder();
        builder.append(event.getAuthor().getName()).append(": ")
                .append(event.getMessage().getContentDisplay());


        if (event.getMessage().getContentDisplay().equals("!deck")) {
            event.getChannel().sendMessage(deck.toString()).queue();
            File file = null;
            try {
                file = generator.generateTable(deck.getCardDeck(), 10);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            File finalFile = file;
            
            event.getAuthor().openPrivateChannel()
                    .flatMap(privateChannel -> privateChannel
                            .sendMessage(deck.toString())
                            .addFile(finalFile))
                    .queue();
        }

        if (event.getMessage().getContentDisplay().equals("!init")) {
            event.getChannel().sendMessage("Rozpoczynamy gre! Uzyj polecenia !join aby dolaczyc do gry.").queue();
            table.initGame();
        }

        if (event.getMessage().getContentDisplay().equals("!join")) {
            List<String> nicknames = table.getPlayersList().stream().map(Player::getName).toList();
            if(nicknames.contains(event.getAuthor().getName())) {
                event.getChannel().sendMessage("Juz jestes w grze!").queue();
                return;
            }
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
            event.getGuild().getMembers().stream().forEach(member -> {
                for(Player player : table.getPlayersList()) {
                    if(player.getName().equals(member.getUser().getName())) {
                        member.getUser()
                                .openPrivateChannel()
                                .flatMap(privateChannel -> privateChannel.sendMessage(player.getPlayerCards().toString()))
                                .queue();
                    }
                }
            });
        }

    }

//
//    private void preFlopRound() {
//        currentPlayerIndex = bigBlindIndex != playersList.size() - 1 ? bigBlindIndex + 1 : 0;
//        int playerToActIndex = currentPlayerIndex;
//
//        log.info("**********Pre-flop round**********");
//
//        do {
//            log.info("Current bet: " + currentBet);
//            log.info("Round pot: " + roundPot);
//            log.info("Player to act: " + playersList.get(playerToActIndex).getName());
//
//            if (playersList.get(currentPlayerIndex).isFold()) {
//                System.out.println(playersList.get(currentPlayerIndex).getName() + " is folded");
//                currentPlayerIndex = currentPlayerIndex != playersList.size() - 1 ? currentPlayerIndex + 1 : 0;
//                continue;
//            }
//
//            if(getAllFoldedPlayers() == playersList.size() - 1) {
//                log.info("Player" + currentPlayerIndex + "wins the game!!");
//                return;
//            }
//
//            log.info("It's " + playersList.get(currentPlayerIndex).getName() + "'s turn");
//            String option = scanner.nextLine();
//
//            if (option.equals("fold")) {
//                log.info(playersList.get(currentPlayerIndex).getName() + " folded");
//                playersList.get(currentPlayerIndex).fold();
//            }
//            if (option.equals("call")) {
//                log.info(playersList.get(currentPlayerIndex).getName() + " called");
//                playersList.get(currentPlayerIndex).setBet(currentBet);
//                playersList.get(currentPlayerIndex).call();
//                roundPot += currentBet;
//            }
//            if (option.equals("raise")) {
//                log.info(playersList.get(currentPlayerIndex).getName() + " raised");
//                currentBet += 20;
//                playersList.get(currentPlayerIndex).raise(20);
//                roundPot += currentBet;
//                playerToActIndex = currentPlayerIndex;
//            }
//            currentPlayerIndex = currentPlayerIndex != playersList.size() - 1 ? currentPlayerIndex + 1 : 0;
//
//
//        } while (playerToActIndex != currentPlayerIndex);
//
//        log.info("Pre flop round finished");
//        totalPot += roundPot;
//        roundPot = 0;
//    }

}