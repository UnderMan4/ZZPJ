package p.lodz.pl.bot.commands;

import lombok.extern.java.Log;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import p.lodz.pl.logic.exceptions.CannotJoinGame;
import p.lodz.pl.logic.exceptions.NoCardsInDeck;
import p.lodz.pl.logic.exceptions.NotEnoughPlayersException;
import p.lodz.pl.logic.model.*;
import p.lodz.pl.logic.utils.ImageGenerator;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static p.lodz.pl.logic.model.DEFS.ROUNDS_COUNT;

@Log
public class Game extends ListenerAdapter {

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


    public Game() {
        this.table = new Table();
        this.deck = new Deck();
        this.generator = new ImageGenerator();
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }
        //-----------------------------------------------COMMANDS USED ANYTIME-----------------------------------

        if (event.getMessage().getContentDisplay().equals("!rules")) {
            EmbedBuilder ruleEmbed = new EmbedBuilder();
            ruleEmbed.setTitle("Zasady gry");
            ruleEmbed.setDescription(
                    """
                                    1.  Jeden gracz jest rozdaj\u0105cym (dealer), a pozycja rozdaj\u0105cego zmienia si\u0119 zgodnie z ruchem wskaz\u00F3wek zegara co rozdanie. Dwaj gracze na lewo od rozdaj\u0105cego nazywaj\u0105 si\u0119 small blind oraz big blind.
                                    2.  Gracze z pozycji Small Blind oraz Big Blind musz\u0105 wnie\u015B\u0107 zak\u0142ad i przewa\u017Cnie s\u0105 to jedyni gracze, kt\u00F3rzy musz\u0105 w\u0142o\u017Cy\u0107 pieni\u0105dze do puli przed rozdaniem kart. Nast\u0119pnie, ka\u017Cdy z graczy otrzymuje dwie karty (karty w\u0142asne) zakryte.
                                    3.  Po rozdaniu kart w\u0142asnych, rozpoczyna si\u0119 pierwsza runda licytacji, kt\u00F3r\u0105 rozpoczyna gracz siedz\u0105cy na lewo od big blinda. Gracz ten mo\u017Ce spasowa\u0107, sprawdzi\u0107 lub podbi\u0107.
                                    4.  Nast\u0119pnie licytacja jest kontynuowana, a ka\u017Cdy z graczy mo\u017Ce spasowa\u0107, sprawdzi\u0107 lub przebi\u0107 najwy\u017Cszy zak\u0142ad postawiony przez gracza przed nim. W celu pozostania w grze i obejrzenia kolejnej karty, wszyscy gracze musz\u0105 zainwestowa\u0107 w pul\u0119 tak\u0105 sam\u0105 liczb\u0119 \u017Ceton\u00F3w.
                                    5.  Po zako\u0144czeniu pierwszej rundy licytacji wyk\u0142adany jest flop, na stole pojawiaj\u0105 si\u0119 trzy karty wsp\u00F3lne, kt\u00F3rych warto\u015B\u0107 jest widoczna dla ka\u017Cdego uczestnika gry.
                                    6.  Nast\u0119pnie mamy drug\u0105 rund\u0119 licytacji, a pozostali w rozdaniu gracze maj\u0105 opcj\u0119 check, bet, raise, call lub fold.
                                    7.  Kiedy druga runda licytacji dobiega ko\u0144ca, wyk\u0142adana jest czwarta karta wsp\u00F3lna, tzw. \u201Cturn\u201D.
                                    8.  Zaczyna si\u0119 trzecia runda licytacji, a jako pierwszy decyzje podejmuje gracz na lewo od pozycji rozdaj\u0105cego. Podobnie jak w poprzednich rundach, gra przebiega zgodnie z ruchem wskaz\u00F3wek zegara.
                                    9.  Kiedy trzecia runda licytacji dobiega ko\u0144ca, wyk\u0142adana jest kolejna karta wsp\u00F3lna, tzw. \u201Criver\u201D.
                                    10. Rozpoczyna si\u0119 czwarta runda licytacji, w kt\u00F3rej jako pierwszy decyzje podejmuje gracz po lewej od rozdaj\u0105cego. Licytacja trwa zgodnie z ruchem wskaz\u00F3wek zegara. Je\u017Celi dw\u00F3ch lub wi\u0119cej graczy jest aktywnych po czwartej rundzie licytacji, karty w\u0142asne tych graczy s\u0105 ods\u0142aniane i gracz z najlepszym uk\u0142adem zgarnia pul\u0119.
                                    
                                    Aby zobaczy\u0107 wszystkie uk\u0142ady kart wpisz `!hands`
                            """
            );
            event.getChannel().sendMessageEmbeds(ruleEmbed.build()).queue();
        }

        if (event.getMessage().getContentDisplay().equals("!hands")) {
            EmbedBuilder handsEmbed = new EmbedBuilder();
            handsEmbed.setTitle("Kombinacje pokerowe");
            handsEmbed.setDescription(
                    """
                                    1.  Poker kr\u00F3lewski (Royal Flush) - pi\u0119\u0107 kart tego samego koloru oraz warto\u015Bciach od 10 do asa
                                    2.  Poker (Straight Flush) - pi\u0119\u0107 kart tego samego koloru oraz kolejnych warto\u015Bciach
                                    3.  Kareta (Four of a Kind) - cztery karty tej samej warto\u015Bci
                                    4.  Full (Full House) - trzy karty tej samej warto\u015Bci oraz dwie kolejne tej samej warto\u015Bci
                                    5.  Kolor (Flush) - pi\u0119\u0107 kart tego samego koloru
                                    6.  Strit (Straight) - pi\u0119\u0107 kart kolejnych warto\u015Bci
                                    7.  Trojka (Three of a Kind) - trzy karty tej samej warto\u015Bci
                                    8.  Dwie pary (Two Pairs) - dwie pary kart tej samej warto\u015Bci
                                    9.  Jedna para (One Pair) - para kart tej samej warto\u015Bci
                                    10. Wysoka karta (High Card) - pi\u0119\u0107 dowolnych kart
                                    
                                    Wpisz `!hand <numer>` aby zobaczy\u0107 szczeg\u00F3\u0142y danej kombinacji wraz z przyk\u0142adem
                            """
            );
            event.getChannel().sendMessageEmbeds(handsEmbed.build()).queue();
        }

        if (event.getMessage().getContentDisplay().contains("!hand")) {
            String hand = event.getMessage().getContentDisplay().split(" ")[1];

            List<Card> royalFlush = List.of(
                    new Card(Ranks.Ten, Suits.Clubs),
                    new Card(Ranks.Jack, Suits.Clubs),
                    new Card(Ranks.Queen, Suits.Clubs),
                    new Card(Ranks.King, Suits.Clubs),
                    new Card(Ranks.Ace, Suits.Clubs)
            );
            List<Card> straightFlush = List.of(
                    new Card(Ranks.Seven, Suits.Clubs),
                    new Card(Ranks.Eight, Suits.Clubs),
                    new Card(Ranks.Nine, Suits.Clubs),
                    new Card(Ranks.Ten, Suits.Clubs),
                    new Card(Ranks.Jack, Suits.Clubs)
            );
            List<Card> fourOfAKind = List.of(
                    new Card(Ranks.Seven, Suits.Clubs),
                    new Card(Ranks.Seven, Suits.Diamonds),
                    new Card(Ranks.Seven, Suits.Hearts),
                    new Card(Ranks.Seven, Suits.Spades),
                    new Card(Ranks.Jack, Suits.Clubs)
            );
            List<Card> fullHouse = List.of(
                    new Card(Ranks.Seven, Suits.Clubs),
                    new Card(Ranks.Seven, Suits.Diamonds),
                    new Card(Ranks.Seven, Suits.Hearts),
                    new Card(Ranks.Jack, Suits.Clubs),
                    new Card(Ranks.Jack, Suits.Diamonds)
            );
            List<Card> flush = List.of(
                    new Card(Ranks.Two, Suits.Clubs),
                    new Card(Ranks.Four, Suits.Clubs),
                    new Card(Ranks.Seven, Suits.Clubs),
                    new Card(Ranks.Nine, Suits.Clubs),
                    new Card(Ranks.Jack, Suits.Clubs)
            );
            List<Card> straight = List.of(
                    new Card(Ranks.Seven, Suits.Clubs),
                    new Card(Ranks.Eight, Suits.Diamonds),
                    new Card(Ranks.Nine, Suits.Hearts),
                    new Card(Ranks.Ten, Suits.Diamonds),
                    new Card(Ranks.Jack, Suits.Spades)
            );
            List<Card> threeOfAKind = List.of(
                    new Card(Ranks.Seven, Suits.Clubs),
                    new Card(Ranks.Seven, Suits.Diamonds),
                    new Card(Ranks.Seven, Suits.Hearts),
                    new Card(Ranks.Eight, Suits.Clubs),
                    new Card(Ranks.King, Suits.Diamonds)
            );
            List<Card> twoPair = List.of(
                    new Card(Ranks.Seven, Suits.Clubs),
                    new Card(Ranks.Seven, Suits.Diamonds),
                    new Card(Ranks.Eight, Suits.Hearts),
                    new Card(Ranks.Eight, Suits.Clubs),
                    new Card(Ranks.Jack, Suits.Diamonds)
            );
            List<Card> onePair = List.of(
                    new Card(Ranks.Seven, Suits.Clubs),
                    new Card(Ranks.Seven, Suits.Diamonds),
                    new Card(Ranks.Eight, Suits.Hearts),
                    new Card(Ranks.Nine, Suits.Clubs),
                    new Card(Ranks.Jack, Suits.Diamonds)
            );
            List<Card> highCard = List.of(
                    new Card(Ranks.Three, Suits.Clubs),
                    new Card(Ranks.Five, Suits.Diamonds),
                    new Card(Ranks.Nine, Suits.Hearts),
                    new Card(Ranks.Jack, Suits.Clubs),
                    new Card(Ranks.King, Suits.Diamonds)
            );

            switch (hand) {
                case "1" ->
                        sendHandHelp(event, "Poker kr\u00F3lewski (Royal Flush)", "Najmocniejszy i tym samym najlepiej punktowany uk\u0142ad kart w pokerze. Sk\u0142ada si\u0119 na niego pi\u0119\u0107 kart w tym samym kolorze \u2014 as, kr\u00F3l, dama, walet i dziesi\u0105tka. Mo\u017Cliwo\u015Bci wyst\u0105pienia takiego u\u0142o\u017Cenia podczas jednej gry s\u0105 niewielkie, jest ich zaledwie 4.", royalFlush);
                case "2" ->
                        sendHandHelp(event, "Poker (Straight Flush)", "U\u0142o\u017Cenie kart znajduj\u0105ce si\u0119 w hierarchii nieco ni\u017Cej od pokera kr\u00F3lewskiego, ale wci\u0105\u017C b\u0119d\u0105ce bardzo dobrze punktowanym. Ten uk\u0142ad sk\u0142ada si\u0119 z pi\u0119ciu kart w tym samym kolorze, kt\u00F3rych warto\u015Bci nast\u0119puj\u0105 po sobie. Mog\u0105 by\u0107 to np. karty od dziesi\u0105tki do sz\u00F3stki w karo. Je\u015Bli w trakcie rozgrywki dw\u00F3ch uczestnik\u00F3w zaprezentuje ten uk\u0142ad w fazie wy\u0142o\u017Cenia kart, to wygrywa ta osoba, kt\u00F3rej karta jest wy\u017Csza. ", straightFlush);
                case "3" ->
                        sendHandHelp(event, "Kareta (Four of a Kind)", "Ten uk\u0142ad pokerowy sk\u0142ada si\u0119 z czterech kart o identycznej warto\u015Bci. Mog\u0105 to by\u0107 cztery dziesi\u0105tki, walety, kr\u00F3le lub jakiekolwiek inne figury. Je\u015Bli dw\u00F3ch graczy wy\u0142o\u017Cy na st\u00F3\u0142 taki uk\u0142ad, to wygrywa ten, kt\u00F3rego karty s\u0105 wy\u017Cej w hierarchii.", fourOfAKind);
                case "4" ->
                        sendHandHelp(event, "Full (Full House)", "Tworz\u0105 go trzy karty o bli\u017Aniaczej warto\u015Bci i kolejne dwie r\u00F3wnie\u017C o identycznych warto\u015Bciach. Mog\u0105 to by\u0107 np. trzy karty kr\u00F3li i dwie karty dziesi\u0105tek. Je\u015Bli dwie osoby maj\u0105 fulla w grze, wygrywa ten, kt\u00F3ry ma wy\u017Csze karty w tr\u00F3jce, je\u015Bli za\u015B tr\u00F3jk\u0119 u obu uczestnik\u00F3w gry tworz\u0105 te same karty, to zwyci\u0119zc\u0105 jest posiadacz wy\u017Cszej dw\u00F3jki.", fullHouse);
                case "5" ->
                        sendHandHelp(event, "Kolor (Flush)", "Pi\u0119\u0107 kart o tym samym kolorze, kt\u00F3rych warto\u015Bci s\u0105 przypadkowe, tworzy uk\u0142ad pokerowy zwany kolorem. Standardowo \u2014 zawsze wygrywa ten gracz, kt\u00F3rego kolor zawiera kart\u0119 wy\u017Csz\u0105 od kart przeciwnika.", flush);
                case "6" ->
                        sendHandHelp(event, "Strit (Straight)", "Sk\u0142ada si\u0119 na niego pi\u0119\u0107 kart w r\u00F3\u017Cnych kolorach, ale za to u\u0142o\u017Conych w kolejno\u015Bci. Mo\u017Ce to by\u0107 np. uk\u0142ad w pokerze stworzony z kr\u00F3la, damy, waleta, dziesi\u0105tki i dziewi\u0105tki. Zawsze wygrywa ten strit, kt\u00F3ry sk\u0142ada si\u0119 z kart znajduj\u0105cych si\u0119 wy\u017Cej w hierarchii. ", straight);
                case "7" ->
                        sendHandHelp(event, "Trojka (Three of a Kind)", "Trzy karty o takiej samej warto\u015Bci to tr\u00F3jka. Dwie kolejne karty trzymane na r\u0119ce s\u0105 w\u00F3wczas przypadkowe. Wygrywaj\u0105cym graczem jest zawsze ten b\u0119d\u0105cy w posiadaniu wy\u017Cszej tr\u00F3jki. ", threeOfAKind);
                case "8" ->
                        sendHandHelp(event, "Dwie pary (Two Pairs)", "Tutaj mamy do czynienia z uk\u0142adem, kt\u00F3ry sk\u0142ada si\u0119 z dw\u00F3ch par kart o takich samych warto\u015Bciach. Mog\u0105 to by\u0107 np. dwa kr\u00F3le i dwie dziesi\u0105tki.", twoPair);
                case "9" ->
                        sendHandHelp(event, "Jedna para (One Pair)", "Dwie z pi\u0119ciu kart trzymanych na r\u0119ce tworz\u0105 par\u0119 wtedy, gdy ich warto\u015B\u0107 jest taka sama. Mog\u0105 to by\u0107 np. dwa asy. ", onePair);
                case "10" ->
                        sendHandHelp(event, "Wysoka karta (High Card)", "To najni\u017Cszy z uk\u0142ad\u00F3w w pokerze. Ma on zastosowanie wtedy, kiedy \u017Caden z uczestnik\u00F3w gry nie posiada \u017Cadnego z uk\u0142ad\u00F3w, kt\u00F3re zosta\u0142y opisane powy\u017Cej. W\u00F3wczas wygrywa ten, kt\u00F3ry ma najwy\u017Csz\u0105 kart\u0119 ze wszystkich.", highCard);
                default ->
                        sendHandHelp(event, "Niepoprawny numer", "Wpisz `!hands` aby zobaczy\u0107 list\u0119 dost\u0119pnych opcji", null);
            }


        }

        if (event.getMessage().getContentDisplay().equals("!help")) {

            EmbedBuilder helpEmbed = new EmbedBuilder();
            helpEmbed.setTitle("Pomoc");
            helpEmbed.setDescription(
                    """
                            `!init` - utw\u00F3rz st\u00F3\u0142 do gry w pokera
                            `!join` - do\u0142\u0105cz do utworzonego sto\u0142u
                            `!start` - zacznij rozgrywk\u0119
                            `!bet` - wy\u015Bwietl aktualna stawk\u0119
                            `!rank` - wy\u015Blij w wiadomo\u015Bci prywatnej aktualna kompozycje
                            `!fold` - spasuj
                            `!call` - wyr\u00F3wnaj do stawki
                            `!raise <warto\u015B\u0107>` - podbij stawk\u0119 o dana warto\u015B\u0107
                            `!check` - sprawd\u017A
                            `!rules` - wy\u015Bwietla zasady gry
                            `!hands` - wy\u015Bwietla hierachi\u0119 kombinacji pokerowych
                            `!hand <numer>` - wy\u015Bwietla szczeg\u00F3\u0142y kombinacji pokerowej o danym numerze
                                    """
            );
            event.getChannel().sendMessageEmbeds(helpEmbed.build()).queue();
        }
        
        if (event.getMessage().getContentDisplay().equals("!test")){
            URI resource = null;
            try {
                resource = getClass().getResource("/ja nie testuje.mp4").toURI();
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            
            File file = new File(resource);
            event.getChannel().sendMessage("Prawdziwi programi\u015Bci niczego nie testuj\u0105!!!").addFile(file).queue();
        }

        //-----------------------------------------------COMMANDS USED BEFORE GAME-----------------------------------

        if (event.getMessage().getContentDisplay().equals("!init") && !isGameInitiated) {

            EmbedBuilder initEmbed = new EmbedBuilder();
            initEmbed.setDescription("Gra jest ju\u017C rozpocz\u0119ta!");

            if (isGameStarted) {
                event.getChannel().sendMessageEmbeds(initEmbed.build()).queue();
                return;
            }

            initEmbed.setDescription("Rozpoczynamy gre! U\u017Cyj polecenia `!join` aby do\u0142\u0105czy\u0107 do gry.");
            event.getChannel().sendMessageEmbeds(initEmbed.build()).queue();
            table.initGame();
            isGameInitiated = true;
            table.whoWon();
            return;
        }

        if (event.getMessage().getContentDisplay().equals("!join") && isGameInitiated) {
            EmbedBuilder joinEmbed = new EmbedBuilder();

            List<String> nicknames = table.getPlayersList().stream().map(Player::getName).toList();
            if (nicknames.contains(event.getAuthor().getName())) {
                joinEmbed.setDescription("Ju\u017C jeste\u015B w grze!");
                event.getChannel().sendMessageEmbeds(joinEmbed.build()).queue();
                return;
            }
            if (isGameStarted) {
                joinEmbed.setDescription("Gra jest juz rozpocz\u0119ta!");
                event.getChannel().sendMessageEmbeds(joinEmbed.build()).queue();
                return;
            }

            Player player = new Player(event.getAuthor().getName(), 1000, 0, new Hand());
            try {
                table.joinGame(player);
                joinEmbed.setTitle("Gracz " + event.getAuthor().getName() + " do\u0142\u0105czy\u0142 do gry");
                joinEmbed.setDescription("Aktualna ilo\u015B\u0107 graczy: " + table.getPlayersList().size());
                event.getChannel().sendMessageEmbeds(joinEmbed.build()).queue();
            } catch (CannotJoinGame e) {
                joinEmbed.setTitle("B\u0142\u0105d");
                joinEmbed.setDescription("Nie uda\u0142o sie do\u0142\u0105czy\u0107 do gry.");
                event.getChannel().sendMessageEmbeds(joinEmbed.build()).queue();
                e.printStackTrace();
            }
            return;
        }

        if (event.getMessage().getContentDisplay().equals("!start") && isGameInitiated) {
            EmbedBuilder startEmbed = new EmbedBuilder();

            if (isGameStarted) {
                startEmbed.setDescription("Gra jest juz rozpocz\u0119ta!");
                event.getChannel().sendMessageEmbeds(startEmbed.build()).queue();
                return;
            }
            try {
                table.startGame();
            } catch (NotEnoughPlayersException e) {
                startEmbed.setTitle("Nie mo\u017Cna uruchomi\u0107 gry");
                startEmbed.setDescription("Zbyt ma\u0142a ilo\u015B\u0107 graczy: " + table.getPlayersList().size());
                event.getChannel().sendMessageEmbeds(startEmbed.build()).queue();
                return;
            }



            isGameStarted = true;
            isGameFinished = false;
            wasRaised = true;
            roundNumber = 0;

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


            startEmbed.setTitle("Rozpoczynamy gr\u0119!");
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
            event.getChannel().sendMessage("https://youtu.be/BZkKgq7EyP8?t=28").queue();

            isGameFinished = true;
        }

        if (event.getMessage().getContentDisplay().equals("!bet") && isGameStarted) {
            EmbedBuilder betEmbed = new EmbedBuilder();
            betEmbed.setDescription("Aktualna stawka: " + table.getCurrentBet());
            event.getChannel().sendMessageEmbeds(betEmbed.build()).queue();
            return;
        }

        if (event.getMessage().getContentDisplay().equals("!stats") && isGameStarted) {
            EmbedBuilder statsEmbed = new EmbedBuilder();
            statsEmbed.setTitle("Twoje Statystyki");

            String handInfo = table.getCommunityCards().size() != 0 ? "R\u0119ka: " + table.getPlayersList().get(currentPlayerIndex).getPlayerHand().checkHand() + "\n" : "";

            statsEmbed.setDescription("Tw\u00F3j aktualny stan konta: " + table.getPlayersList().get(currentPlayerIndex).getPlayerChips() + "\n"
                    + "Aktualna kwota zak\u0142adu: " + table.getCurrentBet() + "\n" +
                    handInfo
            );
            File file = new File(String.valueOf(prepareUserCards(event.getAuthor().getName())));
            statsEmbed.setImage("attachment://table.png");

            event.getAuthor()
                    .openPrivateChannel()
                    .flatMap(privateChannel -> privateChannel.sendMessageEmbeds(statsEmbed.build())).queue();
            return;
        }


        if (event.getMessage().getContentDisplay().equals("!rank") && isGameStarted) {
            Player player = table.getPlayersList()
                    .stream()
                    .filter(p -> p.getName().equals(event.getAuthor().getName()))
                    .findFirst()
                    .get();
            event.getAuthor()
                    .openPrivateChannel()
                    .flatMap(privateChannel -> privateChannel.sendMessage(player.getPlayerHand().checkHand())).queue();
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
                foldEmbed.setDescription("Wszyscy gracze z wyj\u0105tkiem " + winner.getName() + " oddali karty\nWygrywa gracz: " + winner.getName());
                event.getChannel().sendMessageEmbeds(foldEmbed.build()).queue();

                isGameStarted = false;
                isGameInitiated = false;
                return;
            }

            checkIsTheRoundFinished(event);
            if (!isGameFinished) {
                checkIsFold(event);
                foldEmbed.setDescription("Kolej na gracza: " + table.getPlayersList().get(currentPlayerIndex).getName());
                event.getChannel().sendMessageEmbeds(foldEmbed.build()).queue();
            }
            return;
        }

        if (event.getMessage().getContentDisplay().equals("!call") && isGameStarted) {
            EmbedBuilder callEmbed = new EmbedBuilder();

            if (!checkPlayer(event)) return;

            if (!wasRaised) {
                callEmbed.setDescription("Nie mo\u017Cesz wyr\u00F3wna\u0107 zak\u0142adu, je\u015Bli nikt go wcze\u015Bniej nie podbi\u0142");
                event.getChannel().sendMessageEmbeds(callEmbed.build()).queue();
                return;
            }

            callEmbed.setDescription("Gracz " + table.getPlayersList().get(currentPlayerIndex).getName() + " wyr\u00F3wna\u0142 zak\u0142ad. Stawka wynosi: " + table.getCurrentBet());
            event.getChannel().sendMessageEmbeds(callEmbed.build()).queue();
            table.getPlayersList().get(currentPlayerIndex).setBet(table.getCurrentBet());
            table.getPlayersList().get(currentPlayerIndex).call();

            previousPlayerIndex = currentPlayerIndex;
            currentPlayerIndex = currentPlayerIndex != table.getPlayersList().size() - 1 ? currentPlayerIndex + 1 : 0;

            checkIsTheRoundFinished(event);
            if (!isGameFinished) {
                checkIsFold(event);
                callEmbed.setDescription("Kolej na gracza: " + table.getPlayersList().get(currentPlayerIndex).getName());
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
                    raiseEmbed.setDescription("Nie masz wystarczaj\u0105cej liczby pieni\u0119dzy do obstawienia!");
                    event.getChannel().sendMessageEmbeds(raiseEmbed.build()).queue();
                    return;
                }

                if (amount <= table.getCurrentBet()) {
                    raiseEmbed.setDescription("Nie mo\u017Cesz obstawi\u0107 mniejszej stawki!");
                    event.getChannel().sendMessageEmbeds(raiseEmbed.build()).queue();
                    return;
                }

            } catch (Exception e) {
                raiseEmbed.setDescription("Podano niepoprawna kwot\u0119");
                event.getChannel().sendMessageEmbeds(raiseEmbed.build()).queue();
                return;
            }

            table.setCurrentBet(table.getCurrentBet() + amount);
            wasRaised = true;
            raiseEmbed.setDescription("Gracz " + table.getPlayersList().get(currentPlayerIndex).getName()
                    + " podni\u00F3s\u0142 stawk\u0119 o " + amount + ". Stawka wynosi: " + table.getCurrentBet());
            event.getChannel().sendMessageEmbeds(raiseEmbed.build()).queue();
            playerToActIndex = currentPlayerIndex;

            previousPlayerIndex = currentPlayerIndex;
            currentPlayerIndex = currentPlayerIndex != table.getPlayersList().size() - 1 ? currentPlayerIndex + 1 : 0;

            checkIsFold(event);

            raiseEmbed.setDescription("Kolej na gracza: " + table.getPlayersList().get(currentPlayerIndex).getName());
            event.getChannel().sendMessageEmbeds(raiseEmbed.build()).queue();

            return;

        }

        if (event.getMessage().getContentDisplay().equals("!check") && isGameStarted) {
            EmbedBuilder checkEmbed = new EmbedBuilder();

            if (!checkPlayer(event)) return;

            if (wasRaised) { // TODO
                checkEmbed.setDescription("Nie mo\u017Cna czeka\u0107, gdy\u017C gracz " + table.getPlayersList().get(previousPlayerIndex).getName() + " podni\u00F3s\u0142 stawk\u0119");
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
                checkEmbed.setDescription("Kolej na gracza: " + table.getPlayersList().get(currentPlayerIndex).getName());
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

    private void sendHandHelp(MessageReceivedEvent event, String title, String description, List<Card> cards) {
        EmbedBuilder messageEmbed = new EmbedBuilder();
        messageEmbed.setTitle(title);
        messageEmbed.setDescription(description);
        if (cards != null) {
            File file = null;
            try {
                file = generator.generateTable(cards, cards.size());
            } catch (Exception e) {
                e.printStackTrace();
            }
            messageEmbed.setImage("attachment://cards.png");
            messageEmbed.setFooter("Przyk\u0142adowe ulo\u017Cenie");

            event.getChannel().sendMessageEmbeds(messageEmbed.build()).addFile(file, "cards.png").queue();
        } else {
            event.getChannel().sendMessageEmbeds(messageEmbed.build()).queue();
        }
    }

    // Checks whether the round is finished
    private void checkIsTheRoundFinished(@NotNull MessageReceivedEvent event) {
        if (currentPlayerIndex == playerToActIndex) {
            if (roundNumber == ROUNDS_COUNT) {
                infoEmbed.setDescription("Koniec gry!");
                event.getChannel().sendMessageEmbeds(infoEmbed.build()).queue();
                isGameStarted = false;
                isGameInitiated = false;
                isGameFinished = true;
                List<Player> winners = table.whoWon();
                for (Player player : winners) {
                    infoEmbed.setDescription("Gracz " + player.getName() + " wygral!");
                    event.getChannel().sendMessageEmbeds(infoEmbed.build()).queue();
                }
                for (Player player : table.getPlayersList()) {
                    event.getChannel().sendMessage("Gracz " + player.getName() + player.getPlayerHand().checkHand()).queue();
                }
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
            infoEmbed.setDescription("Gracz " + player.getName() + " odda\u0142 karty - pomijanie gracza");
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
            infoEmbed.setDescription("Poczekaj chwil\u0119 <@" + event.getAuthor().getId() + ">. Teraz kolej na gracza " + player.getName());
            event.getChannel().sendMessageEmbeds(infoEmbed.build()).queue();
//            event.getChannel().sendMessage("Poczekaj chwile <@" + event.getAuthor().getId() + ">. Teraz kolej na gracza " + player.getName()).queue();
            return false;
        }
        return true;
    }

    private String getRoundName(int roundNumber) {
        return switch (roundNumber) {
            case 0 -> "Preflop Round";
            case 1 -> "Flop Round";
            case 2 -> "Turn Round";
            case 3 -> "River Round";
            default -> "Round";
        };
    }

}