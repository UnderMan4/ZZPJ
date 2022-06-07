package p.lodz.pl.bot.commands;

import lombok.extern.java.Log;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import p.lodz.pl.logic.exceptions.CannotJoinGame;
import p.lodz.pl.logic.exceptions.NoCardsInDeck;
import p.lodz.pl.logic.exceptions.NotEnoughPlayersException;
import p.lodz.pl.logic.model.*;
import p.lodz.pl.logic.utils.ImageGenerator;

import java.io.File;
import java.util.List;

import static p.lodz.pl.logic.model.DEFS.ROUNDS_COUNT;

@Log
public class Commands extends ListenerAdapter {

    private final Deck deck;
    private final Table table;
    private final ImageGenerator generator;

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
    private boolean isGameFinished;
    private boolean wasRaised;


    public Commands() {
        this.table = new Table();
        this.deck = new Deck();
        this.generator = new ImageGenerator();
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        if (event.getMessage().getContentDisplay().equals("!help")) {

            EmbedBuilder helpEmbed = new EmbedBuilder();
            helpEmbed.setTitle("PokerBOT | Help Menu:");
            helpEmbed.setDescription("`!init` - utworz stol do gry w pokera\n" +
                    "`!join` - dolacz do utworzonego stolu\n" +
                    "`!start` - zacznij rozgrywke\n" +
                    "`!bet` - wyswietl aktualna stawke\n" +
                    "`!rank` - wyslij w wiadomosci prywatnej aktualna kompozycje\n" +
                    "`!fold` - spasuj\n" +
                    "`!call` - wyrownaj do stawki\n" +
                    "`!raise` <value> - podbij stawke o dana wartosc\n" +
                    "`!check` - sprawdz\n" +
                    "`!rules` - wyswietla zasady gry\n" +
                    "`!hands` - wyswietla  hierachie kombinacji pokerowych");
            event.getChannel().sendMessageEmbeds(helpEmbed.build()).queue();
        }

        //-----------------------------------------------COMMANDS USED BEFORE GAME-----------------------------------

        if (event.getMessage().getContentDisplay().equals("!init") && !isGameInitiated) {

            EmbedBuilder initEmbed = new EmbedBuilder();
            initEmbed.setDescription("Gra jest juz rozpoczeta!");

            if (isGameStarted) {
                event.getChannel().sendMessageEmbeds(initEmbed.build()).queue();
                return;
            }

            initEmbed.setDescription("Rozpoczynamy gre! Uzyj polecenia !join aby dolaczyc do gry.");
            event.getChannel().sendMessageEmbeds(initEmbed.build()).queue();
            table.initGame();
            isGameInitiated = true;
            return;
        }

        if (event.getMessage().getContentDisplay().equals("!join") && isGameInitiated) {
            EmbedBuilder joinEmbed = new EmbedBuilder();

            List<String> nicknames = table.getPlayersList().stream().map(Player::getName).toList();
            if (nicknames.contains(event.getAuthor().getName())) {
                joinEmbed.setDescription("Juz jestes w grze!");
                event.getChannel().sendMessageEmbeds(joinEmbed.build()).queue();
                return;
            }
            if (isGameStarted) {
                joinEmbed.setDescription("Gra jest juz rozpoczeta!");
                event.getChannel().sendMessageEmbeds(joinEmbed.build()).queue();
                return;
            }

            Player player = new Player(event.getAuthor().getName(), 1000, 0, new Hand());
            try {
                table.joinGame(player);
                joinEmbed.setTitle("Gracz " + event.getAuthor().getName() + " dolaczyl do gry");
                joinEmbed.setDescription("Aktualna ilosc graczy: " + table.getPlayersList().size());
                event.getChannel().sendMessageEmbeds(joinEmbed.build()).queue();
            } catch (CannotJoinGame e) {
                joinEmbed.setDescription("Nie udalo sie dolaczyc do gry.");
                event.getChannel().sendMessageEmbeds(joinEmbed.build()).queue();
                e.printStackTrace();
            }
            return;
        }

        if (event.getMessage().getContentDisplay().equals("!start") && isGameInitiated) {
            EmbedBuilder startEmbed = new EmbedBuilder();

            if (isGameStarted) {
                startEmbed.setDescription("Gra jest juz rozpoczeta!");
                event.getChannel().sendMessageEmbeds(startEmbed.build()).queue();
                return;
            }
            try {
                table.startGame();
            } catch (NotEnoughPlayersException e) {
                startEmbed.setDescription("Zbyt mala ilosc graczy: " + table.getPlayersList().size());
                event.getChannel().sendMessageEmbeds(startEmbed.build()).queue();
                return;
            }



            isGameStarted = true;
            isGameFinished = false;
            wasRaised = true;

            event.getGuild().getMembers().stream().forEach(member -> {
                System.out.println(member.getUser().getName());
                EmbedBuilder privateMessageEmbed = new EmbedBuilder();
                for (Player player : table.getPlayersList()) {
                    privateMessageEmbed.setTitle("Twoje karty:");
                    File file = new File(String.valueOf(prepareUserCards(player.getName())));
                    privateMessageEmbed.setImage("attachment://table.png");
                    if (player.getName().equals(member.getUser().getName())) {
                        member.getUser()
                                .openPrivateChannel()
                                .flatMap(privateChannel -> privateChannel.sendMessageEmbeds(privateMessageEmbed.build()).addFile(file, "table.png"))
                                .queue();
                    }

                }
            });

            dealerIndex = table.getDealerIndex();
            smallBlindIndex = table.getSmallBlindIndex();
            bigBlindIndex = table.getBigBlindIndex();
            currentPlayerIndex = table.getCurrentPlayerIndex();
            playerToActIndex = currentPlayerIndex;


            startEmbed.setTitle("Rozpoczynamy gre!");
//            event.getChannel().sendMessageEmbeds(startEmbed.build()).queue();
            startEmbed.setDescription("Kolej gracza: " + table.getPlayersList().get(currentPlayerIndex).getName());
            event.getChannel().sendMessageEmbeds(startEmbed.build()).queue();
            return;
        }

        //-----------------------------------------------COMMANDS USED DURING GAME-----------------------------------

        if (!table.getPlayersNames().contains(event.getAuthor().getName())) {
            return;
        }

        if (event.getMessage().getContentDisplay().equals("!makao") && isGameStarted) {

            EmbedBuilder makaoEmbed = new EmbedBuilder();
            makaoEmbed.setTitle("Makao!");
            makaoEmbed.setDescription("https://youtu.be/BZkKgq7EyP8");
            File file = new File(String.valueOf(prepareUserCards(event.getAuthor().getName())));
            makaoEmbed.setImage("attachment://table.png");
            event.getChannel().sendMessageEmbeds(makaoEmbed.build()).addFile(file, "table.png").queue();

            isGameFinished = true;
        }

        if (event.getMessage().getContentDisplay().equals("!bet") && isGameStarted) {
            event.getChannel().sendMessage("Aktualna kwota zakladu: " + table.getCurrentBet()).queue();
        }


        if (event.getMessage().getContentDisplay().equals("!rank") && isGameStarted) {
            Player player = table.getPlayersList()
                    .stream()
                    .filter(p -> p.getName().equals(event.getAuthor().getName()))
                    .findFirst()
                    .get();
            event.getAuthor()
                    .openPrivateChannel()
                    .flatMap(privateChannel -> privateChannel.sendMessage(player.getPlayerHand().checkRank())).queue();
            return;
        }

        if (event.getMessage().getContentDisplay().equals("!fold") && isGameStarted) {
            EmbedBuilder foldEmbed = new EmbedBuilder();

            if (!checkPlayer(event)) return;

            table.getPlayersList().get(currentPlayerIndex).setFold(true);

            foldEmbed.setDescription("Gracz " + table.getPlayersList().get(currentPlayerIndex).getName() + " oddal karty");
            event.getChannel().sendMessageEmbeds(foldEmbed.build()).queue();

            previousPlayerIndex = currentPlayerIndex;
            currentPlayerIndex = currentPlayerIndex != table.getPlayersList().size() - 1 ? currentPlayerIndex + 1 : 0;

            if (table.getPlayersList().stream().filter(player -> !player.isFold()).count() == 1) {
                Player winner = table.getPlayersList().stream().filter(player -> !player.isFold()).findFirst().get();
                foldEmbed.setTitle("Koniec gry!");
//                TODO te dwa przypadki
                foldEmbed.setDescription("Wszyscy gracze z wyjatkiem " + winner.getName() + " oddali karty\nWygrywa gracz: \" + winner.getName()");
                event.getChannel().sendMessageEmbeds(foldEmbed.build()).queue();

                isGameStarted = false;
                isGameInitiated = false;
                return;
            }

            checkIsTheRoundFinished(event);
            if (!isGameFinished) {
                checkIsFold(event);
                foldEmbed.setDescription("Kolej gracza: " + table.getPlayersList().get(currentPlayerIndex).getName());
                event.getChannel().sendMessageEmbeds(foldEmbed.build()).queue();
            }
            return;
        }

        if (event.getMessage().getContentDisplay().equals("!call") && isGameStarted) {
            EmbedBuilder callEmbed = new EmbedBuilder();

            if (!checkPlayer(event)) return;

            if (!wasRaised) {
                callEmbed.setDescription("Nie mozesz wyrownac zakladu, jesli nikt go wczesniej nie podbil");
                event.getChannel().sendMessageEmbeds(callEmbed.build()).queue();
                return;
            }

            callEmbed.setDescription("Gracz " + table.getPlayersList().get(currentPlayerIndex).getName() + " wyrownal zaklad. Stawka wynosi: " + table.getCurrentBet());
            event.getChannel().sendMessageEmbeds(callEmbed.build()).queue();
            table.getPlayersList().get(currentPlayerIndex).setBet(table.getCurrentBet());
            table.getPlayersList().get(currentPlayerIndex).call();

            previousPlayerIndex = currentPlayerIndex;
            currentPlayerIndex = currentPlayerIndex != table.getPlayersList().size() - 1 ? currentPlayerIndex + 1 : 0;

            checkIsTheRoundFinished(event);
            if (!isGameFinished) {
                checkIsFold(event);
                callEmbed.setDescription("Kolej gracza: " + table.getPlayersList().get(currentPlayerIndex).getName());
                event.getChannel().sendMessageEmbeds(callEmbed.build()).queue();
            }
            return;
        }

        if (event.getMessage().getContentDisplay().contains("!raise") && isGameStarted) {
            EmbedBuilder raiseEmbed = new EmbedBuilder();

            if (!checkPlayer(event)) return;

            int amount;

            try {
                String value = event.getMessage().getContentDisplay().split(" ")[1];
                amount = Integer.parseInt(value);


                if (amount > table.getPlayersList().get(currentPlayerIndex).getPlayerChips()) {
                    raiseEmbed.setDescription("Nie masz wystarczajacej liczby pieniedzy do obstawienia!");
                    event.getChannel().sendMessageEmbeds(raiseEmbed.build()).queue();
                    return;
                }

                if (amount <= table.getCurrentBet()) {
                    raiseEmbed.setDescription("Nie mozesz obstawic mniejszej stawki!");
                    event.getChannel().sendMessageEmbeds(raiseEmbed.build()).queue();
                    return;
                }

            } catch (Exception e) {
                raiseEmbed.setDescription("Podano niepoprawna kwote");
                event.getChannel().sendMessageEmbeds(raiseEmbed.build()).queue();
                return;
            }

            table.setCurrentBet(table.getCurrentBet() + amount);
            wasRaised = true;
            raiseEmbed.setDescription("Gracz " + table.getPlayersList().get(currentPlayerIndex).getName()
                    + " podniosl stawke o " + amount + ". Stawka wynosi: " + table.getCurrentBet());
            event.getChannel().sendMessageEmbeds(raiseEmbed.build()).queue();
            playerToActIndex = currentPlayerIndex;

            previousPlayerIndex = currentPlayerIndex;
            currentPlayerIndex = currentPlayerIndex != table.getPlayersList().size() - 1 ? currentPlayerIndex + 1 : 0;

            checkIsFold(event);

            raiseEmbed.setDescription("Kolej gracza: " + table.getPlayersList().get(currentPlayerIndex).getName());
            event.getChannel().sendMessageEmbeds(raiseEmbed.build()).queue();

            return;

        }

        if (event.getMessage().getContentDisplay().equals("!check") && isGameStarted) {
            EmbedBuilder checkEmbed = new EmbedBuilder();

            if (!checkPlayer(event)) return;

            if (wasRaised) { // TODO
                checkEmbed.setDescription("Nie mozna czekac, gdyz gracz " + table.getPlayersList().get(previousPlayerIndex).getName() + " podniosl stawke");
                event.getChannel().sendMessageEmbeds(checkEmbed.build()).queue();
                return;
            }

            checkEmbed.setDescription("Gracz " + table.getPlayersList().get(currentPlayerIndex).getName() + " czeka.");
            event.getChannel().sendMessageEmbeds(checkEmbed.build()).queue();

            previousPlayerIndex = currentPlayerIndex;
            currentPlayerIndex = currentPlayerIndex != table.getPlayersList().size() - 1 ? currentPlayerIndex + 1 : 0;


            checkIsTheRoundFinished(event);
            if (!isGameFinished) {
                checkIsFold(event);
                checkEmbed.setDescription("Kolej gracza: " + table.getPlayersList().get(currentPlayerIndex).getName());
                event.getChannel().sendMessageEmbeds(checkEmbed.build()).queue();
            }
        }
    }

    //-----------------------------------------------PRIVATE METHODS-----------------------------------

    // Send cards to user
    private File prepareUserCards(String name) {
        Player player = table.getPlayersList()
                .stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .get();
        try {
            File file = generator.generateTable(player.getPlayerCards(), player.getPlayerCards().size());
            return file;
        } catch (Exception e) {
            log.severe("Error while drawing cards");
            e.printStackTrace();
            return null;
        }
    }

    EmbedBuilder infoEmbed = new EmbedBuilder();

    // Checks whether the round is finished
    private void checkIsTheRoundFinished(@NotNull MessageReceivedEvent event) {
        if (currentPlayerIndex == playerToActIndex) {
            if (roundNumber == ROUNDS_COUNT) {
                infoEmbed.setDescription("Koniec gry!");
                event.getChannel().sendMessageEmbeds(infoEmbed.build()).queue();
                isGameStarted = false;
                isGameInitiated = false;
                isGameFinished = true;
                return;
            }

            infoEmbed.setDescription("Koniec rundy!");
            event.getChannel().sendMessageEmbeds(infoEmbed.build()).queue();
            wasRaised = false;
            previousPlayerIndex = currentPlayerIndex;
            currentPlayerIndex = dealerIndex != table.getPlayersList().size() - 1 ? dealerIndex + 1 : 0;
            playerToActIndex = currentPlayerIndex;
            handleCommunityCards(event);

            roundNumber++;
        }
    }

    EmbedBuilder generalEmbed = new EmbedBuilder();
    // Handle community cards after the round is finished
    private void handleCommunityCards(@NotNull MessageReceivedEvent event) {
        try {
            if (roundNumber == 0) {
                for (int i = 0; i < 3; i++) {
                    table.getCommunityCards().add(table.getDeck().draw());
                }
            } else {
                table.getCommunityCards().add(table.getDeck().draw());
            }
            table.getPlayersList()
                    .stream()
                    .forEach(player -> player.setPlayerHand(new Hand(
                            table.getCommunityCards().toArray(new Card[table.getCommunityCards().size()]),
                            player.getPlayerCards().toArray(new Card[2]))));
            try {
                File file = generator.generateTable(table.getCommunityCards(), table.getCommunityCards().size());
                generalEmbed.setTitle("Karty na stole: ");
                generalEmbed.setImage("attachment://table.png");
                event.getChannel().sendMessageEmbeds(generalEmbed.build()).addFile(file, "table.png").queue();
            } catch (Exception e) {
                log.severe("Error while drawing cards");
                e.printStackTrace();
            }

        } catch (NoCardsInDeck e) {
            event.getChannel().sendMessage(e.getMessage()).queue();
        }
    }

    // Checks whether next Player has folded
    private void checkIsFold(@NotNull MessageReceivedEvent event) {
        Player player = table.getPlayersList().get(currentPlayerIndex);
        if (player.isFold()) {
            infoEmbed.setDescription("Gracz " + player.getName() + " oddal karty - pomijanie gracza");
            event.getChannel().sendMessageEmbeds(infoEmbed.build()).queue();
            previousPlayerIndex = currentPlayerIndex;
            currentPlayerIndex = currentPlayerIndex != table.getPlayersList().size() - 1 ? currentPlayerIndex + 1 : 0;
        }
    }

    // Checks whether calling Player is the same as current Player
    private boolean checkPlayer(@NotNull MessageReceivedEvent event) {
        System.out.println(currentPlayerIndex);
        Player player = table.getPlayersList().get(currentPlayerIndex);
        if (!player.getName().equals(event.getAuthor().getName())) {
            infoEmbed.setDescription("Poczekaj chwile <@" + event.getAuthor().getId() + ">. Teraz kolej na gracza " + player.getName());
            event.getChannel().sendMessageEmbeds(infoEmbed.build()).queue();
//            event.getChannel().sendMessage("Poczekaj chwile <@" + event.getAuthor().getId() + ">. Teraz kolej na gracza " + player.getName()).queue();
            return false;
        }
        return true;
    }

}