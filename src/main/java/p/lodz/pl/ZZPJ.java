package p.lodz.pl;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import p.lodz.pl.commands.Commands;
import p.lodz.pl.exceptions.NoCardsInDeck;
import p.lodz.pl.model.Deck;

import javax.security.auth.login.LoginException;

public class ZZPJ  {

    public static void main(String[] args)  {

        Dotenv dotenv = Dotenv.load();


//        System.out.println("Hello world");
        Deck deck = new Deck();

        try {
            JDABuilder builder = JDABuilder.createDefault(dotenv.get("TOKEN"));
            JDA jda = builder.build();

            jda.addEventListener(new Commands());
            jda.awaitReady();
        } catch (InterruptedException | LoginException e) {
            e.printStackTrace();
        }


//        System.out.println(deck);
//        try {
//            System.out.println(deck.draw());
//        } catch (NoCardsInDeck e) {
//            e.printStackTrace();
//        }
//        System.out.println(deck);
    }
}
