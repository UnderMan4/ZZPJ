import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import p.lodz.pl.bot.commands.Game;

import javax.security.auth.login.LoginException;

public class ZZPJ {

    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.load();

        try {
            JDABuilder builder = JDABuilder.createDefault(dotenv.get("TOKEN"))
                    .setChunkingFilter(ChunkingFilter.ALL)
                    .enableIntents(GatewayIntent.GUILD_MEMBERS);
            JDA jda = builder.build();

            jda.addEventListener(new Game());
            jda.awaitReady();
        } catch (InterruptedException | LoginException e) {
            e.printStackTrace();
        }



    }
}