package p.lodz.pl.bot.commands;

import lombok.Getter;
import lombok.extern.java.Log;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import p.lodz.pl.logic.exceptions.CannotJoinGame;
import p.lodz.pl.logic.exceptions.NoCardsInDeck;
import p.lodz.pl.logic.exceptions.NotEnoughPlayersException;
import p.lodz.pl.logic.model.Deck;
import p.lodz.pl.logic.model.Hand;
import p.lodz.pl.logic.model.Player;
import p.lodz.pl.logic.model.Table;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import static p.lodz.pl.logic.model.DEFS.ROUNDS_COUNT;

@Log
public class Commands extends ListenerAdapter {

    @Getter
    private final Deck deck;

    @Getter
    private final Table table;

    private int dealerIndex;
    private int smallBlindIndex;
    private int bigBlindIndex;

    private int previousPlayerIndex;
    private int currentPlayerIndex;
    private int playerToActIndex;

    private int roundPot;
    private int currentBet;
    private int totalPot;

    private int roundNumber;
    private boolean isGameStarted;
    private boolean isGameInitiated;
    private boolean isPreFlopRound;
    private boolean wasRaised;



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
            event.getAuthor().openPrivateChannel()
                    .flatMap(privateChannel -> privateChannel.sendMessage(deck.toString()))
                    .queue();
        }

        if (event.getMessage().getContentDisplay().equals("!init") && !isGameInitiated) {

            if (isGameStarted) {
                event.getChannel().sendMessage("Gra jest juz rozpoczeta!").queue();
                return;
            }
            event.getChannel().sendMessage("Rozpoczynamy gre! Uzyj polecenia !join aby dolaczyc do gry.").queue();
            table.initGame();
            isGameInitiated = true;
        }

        if (event.getMessage().getContentDisplay().equals("!join") && isGameInitiated) {
            List<String> nicknames = table.getPlayersList().stream().map(Player::getName).toList();
            if (nicknames.contains(event.getAuthor().getName())) {
                event.getChannel().sendMessage("Juz jestes w grze!").queue();
                return;
            }
            if (isGameStarted) {
                event.getChannel().sendMessage("Gra jest juz rozpoczeta!").queue();
                return;
            }

            Player player = new Player(event.getAuthor().getName(), 1000, 0, new Hand());
            try {
//                check if player already joined
                table.joinGame(player);
                event.getChannel().sendMessage("Gracz " + event.getAuthor().getName() + " dolaczyl do gry").queue();
                event.getChannel().sendMessage("Aktualna ilosc graczy: " + table.getPlayersList().size()).queue();
            } catch (CannotJoinGame e) {
                event.getChannel().sendMessage("Nie udalo sie dolaczyc do gry.").queue();
                e.printStackTrace();
            }
        }

        if (event.getMessage().getContentDisplay().equals("!start") && isGameInitiated) {
            if (isGameStarted) {
                event.getChannel().sendMessage("Gra jest juz rozpoczeta!").queue();
                return;
            }
            try {
                table.startGame();
            } catch (NotEnoughPlayersException e) {
                event.getChannel().sendMessage("Zbyt mala ilosc graczy: " + table.getPlayersList().size()).queue();
                return;
            }
            event.getChannel().sendMessage("Rozpoczynamy gre!").queue();
            isGameStarted = true;
            isPreFlopRound = true;
            wasRaised = true;
            event.getGuild().getMembers().stream().forEach(member -> {
                for (Player player : table.getPlayersList()) {
                    if (player.getName().equals(member.getUser().getName())) {
                        member.getUser()
                                .openPrivateChannel()
                                .flatMap(privateChannel -> privateChannel.sendMessage(player.getPlayerCards().toString()))
                                .queue();
                    }
                }
            });
            dealerIndex = table.getDealerIndex();
            smallBlindIndex = table.getSmallBlindIndex();
            bigBlindIndex = table.getBigBlindIndex();
            currentPlayerIndex = table.getCurrentPlayerIndex();
            playerToActIndex = currentPlayerIndex;

            event.getChannel().sendMessage("Kolej gracza: " + table.getPlayersList().get(currentPlayerIndex).getName()).queue();
        }

//        if (event.getMessage().getContentDisplay().equals("!status")) {
//            event.getChannel().sendMessage().queue();
//        }


        if (event.getMessage().getContentDisplay().equals("!fold") && isGameStarted) {
            if (!checkPlayer(event)) return;

            table.getPlayersList().get(currentPlayerIndex).setFold(true);
            event.getChannel().sendMessage("Gracz " + table.getPlayersList().get(currentPlayerIndex).getName() + " oddal karty").queue();

            previousPlayerIndex = currentPlayerIndex;
            currentPlayerIndex = currentPlayerIndex != table.getPlayersList().size() - 1 ? currentPlayerIndex + 1 : 0;

            if (table.getPlayersList().stream().filter(player -> !player.isFold()).count() == 1) {
                Player winner = table.getPlayersList().stream().filter(player -> !player.isFold()).findFirst().get();
                event.getChannel().sendMessage("Koniec gry!").queue();
                event.getChannel().sendMessage("Wszyscy gracze z wyjatkiem " + winner.getName() + " oddali karty").queue(); // TODO
                event.getChannel().sendMessage("Wygrywa gracz: " + winner.getName()).queue(); // TODO
                isGameStarted = false;
                isGameInitiated = false;
                return;
            }

            checkIsTheRoundFinished(event);
            checkIsFold(event);
            event.getChannel().sendMessage("Kolej gracza: " + table.getPlayersList().get(currentPlayerIndex).getName()).queue();
        }

        if (event.getMessage().getContentDisplay().equals("!call") && isGameStarted) {
            if (!checkPlayer(event)) return;

            event.getChannel().sendMessage("Gracz " + table.getPlayersList().get(currentPlayerIndex).getName() + " sprawdzil. Stawka wynosi: " + table.getCurrentBet()).queue();
            table.getPlayersList().get(currentPlayerIndex).setBet(table.getCurrentBet());
            table.getPlayersList().get(currentPlayerIndex).call();

            previousPlayerIndex = currentPlayerIndex;
            currentPlayerIndex = currentPlayerIndex != table.getPlayersList().size() - 1 ? currentPlayerIndex + 1 : 0;

            checkIsTheRoundFinished(event);
            checkIsFold(event);
            event.getChannel().sendMessage("Kolej gracza: " + table.getPlayersList().get(currentPlayerIndex).getName()).queue();
        }

        if (event.getMessage().getContentDisplay().contains("!raise") && isGameStarted) {
            if (!checkPlayer(event)) return;

            int amount;

            try {
                String value = event.getMessage().getContentDisplay().split(" ")[1];
                amount = Integer.parseInt(value);


                if (amount > table.getPlayersList().get(currentPlayerIndex).getPlayerChips()) {
                    event.getChannel().sendMessage("Nie masz wystarczajacej liczby pieniedzy do obstawienia!").queue();
                    return;
                }

                if (amount <= table.getCurrentBet()) {
                    event.getChannel().sendMessage("Nie mozesz obstawic mniejszej stawki!").queue();
                    return;
                }

            } catch (Exception e) {
                event.getChannel().sendMessage("Podano niepoprawna kwote").queue();
                return;
            }

            table.setCurrentBet(table.getCurrentBet() + amount);
            wasRaised = true;
            event.getChannel()
                    .sendMessage("Gracz " + table.getPlayersList().get(currentPlayerIndex).getName()
                            + " podniosl stawke o " + amount + ". Stawka wynosi: " + table.getCurrentBet()).queue();
            playerToActIndex = currentPlayerIndex;

            previousPlayerIndex = currentPlayerIndex;
            currentPlayerIndex = currentPlayerIndex != table.getPlayersList().size() - 1 ? currentPlayerIndex + 1 : 0;

            checkIsFold(event);
            event.getChannel().sendMessage("Kolej gracza: " + table.getPlayersList().get(currentPlayerIndex).getName()).queue();
        }

        if (event.getMessage().getContentDisplay().equals("!check") && isGameStarted) {
            if (!checkPlayer(event)) return;

            if (wasRaised) { // TODO
                event.getChannel().sendMessage("Nie mozna czekac, gdyz gracz " + table.getPlayersList().get(previousPlayerIndex).getName() + " podniosl stawke").queue();
                return;
            }

            event.getChannel().sendMessage("Gracz " + table.getPlayersList().get(currentPlayerIndex).getName() + " czeka.").queue();
            previousPlayerIndex = currentPlayerIndex;
            currentPlayerIndex = currentPlayerIndex != table.getPlayersList().size() - 1 ? currentPlayerIndex + 1 : 0;


            checkIsTheRoundFinished(event);
            checkIsFold(event);
            event.getChannel().sendMessage("Kolej gracza: " + table.getPlayersList().get(currentPlayerIndex).getName()).queue();
        }

//    }
//
//
//    private void preFlopRound(@NotNull MessageReceivedEvent event) {
//        currentPlayerIndex = bigBlindIndex != table.getPlayersList().size() - 1 ? bigBlindIndex + 1 : 0;
//        int playerToActIndex = currentPlayerIndex;
//
//        log.info("**********Pre-flop round**********");
//
//        do {
//            log.info("Current bet: " + currentBet);
//            log.info("Round pot: " + roundPot);
//            log.info("Player to act: " + table.getPlayersList().get(playerToActIndex).getName());
//
//            if (table.getPlayersList().get(currentPlayerIndex).isFold()) {
//                System.out.println(table.getPlayersList().get(currentPlayerIndex).getName() + " is folded");
//                currentPlayerIndex = currentPlayerIndex != table.getPlayersList().size() - 1 ? currentPlayerIndex + 1 : 0;
//                continue;
//            }
//
//            if(table.getAllFoldedPlayers() == table.getPlayersList().size() - 1) {
//                log.info("Player" + currentPlayerIndex + "wins the game!!");
//                return;
//            }
//
//            log.info("It's " + table.getPlayersList().get(currentPlayerIndex).getName() + "'s turn");
//
//            String option = scanner.nextLine();
//
//            if (option.equals("fold")) {
//                log.info(table.getPlayersList().get(currentPlayerIndex).getName() + " folded");
//                table.getPlayersList().get(currentPlayerIndex).fold();
//            }
//            if (option.equals("call")) {
//                log.info(table.getPlayersList().get(currentPlayerIndex).getName() + " called");
//                table.getPlayersList().get(currentPlayerIndex).setBet(currentBet);
//                table.getPlayersList().get(currentPlayerIndex).call();
//                roundPot += currentBet;
//            }
//            if (option.equals("raise")) {
//                log.info(table.getPlayersList().get(currentPlayerIndex).getName() + " raised");
//                currentBet += 20;
//                table.getPlayersList().get(currentPlayerIndex).raise(20);
//                roundPot += currentBet;
//                playerToActIndex = currentPlayerIndex;
//            }
//            currentPlayerIndex = currentPlayerIndex != table.getPlayersList().size() - 1 ? currentPlayerIndex + 1 : 0;
//
//
//        } while (playerToActIndex != currentPlayerIndex);
//
//        log.info("Pre flop round finished");
//        totalPot += roundPot;
//        roundPot = 0;
//    }

    }

    // Checks whetter the round is finished
    private void checkIsTheRoundFinished(@NotNull MessageReceivedEvent event) {
        if (currentPlayerIndex == playerToActIndex) {
            if (roundNumber == ROUNDS_COUNT) {
                event.getChannel().sendMessage("Koniec gry!").queue();
                isGameStarted = false;
                isGameInitiated = false;
                return;
            }
            event.getChannel().sendMessage("Koniec rundy!").queue();
            wasRaised = false;
            previousPlayerIndex = currentPlayerIndex;
            currentPlayerIndex = dealerIndex != table.getPlayersList().size() - 1 ? dealerIndex + 1 : 0;
            playerToActIndex = currentPlayerIndex;
            handleCards(event);

            roundNumber++;
        }
    }

    private void handleCards(@NotNull MessageReceivedEvent event) {
        try {
            if (roundNumber == 0) {
                for(int i = 0; i < 3; i++) {
                    table.getCommunityCards().add(table.getDeck().draw());
                }
                event.getChannel().sendMessage("Karty na stole:\n" + table.getCommunityCards().toString()).queue();

                return;
            }
            table.getCommunityCards().add(table.getDeck().draw());
            event.getChannel().sendMessage("Karty na stole:\n" + table.getCommunityCards().toString()).queue();
        } catch (NoCardsInDeck e) {
            event.getChannel().sendMessage(e.getMessage()).queue();
        }
    }

    // Checks whether next Player has folded
    private void checkIsFold(@NotNull MessageReceivedEvent event) {
        Player player = table.getPlayersList().get(currentPlayerIndex);
        if (player.isFold()) {
            event.getChannel().sendMessage("Gracz " + player.getName() + " oddal karty - pomijanie gracza").queue();
            previousPlayerIndex = currentPlayerIndex;
            currentPlayerIndex = currentPlayerIndex != table.getPlayersList().size() - 1 ? currentPlayerIndex + 1 : 0;
        }
    }

    // Checks whether calling Player is the same as current Player
    private boolean checkPlayer(@NotNull MessageReceivedEvent event) {
        System.out.println(currentPlayerIndex);
        Player player = table.getPlayersList().get(currentPlayerIndex);
        if (!player.getName().equals(event.getAuthor().getName())) {
            event.getChannel().sendMessage("Poczekaj chwile " + event.getAuthor().getName() + ". Teraz kolej na gracza " + player.getName()).queue();
            return false;
        }
        return true;
    }

}