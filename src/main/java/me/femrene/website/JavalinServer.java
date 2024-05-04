package me.femrene.website;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.javalin.Javalin;
import io.javalin.community.ssl.SslPlugin;
import io.javalin.http.Context;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class JavalinServer {

    private static Javalin javalin;

    public static void start(int port) {
        SslPlugin plugin = new SslPlugin(conf -> {
            conf.pemFromPath("/cert/cert.pem", "/cert/key.pem");
        });
        javalin = javalin.create(javalinConfig -> {
            javalinConfig.registerPlugin(plugin);
        });
        javalin.get("/", ctx -> ctx.result("Hello World"));
        javalin.get("/players", ctx -> ctx.json(getPlayers(ctx)));
        javalin.start(Bukkit.getIp(), port);
    }

    public static Javalin getJavalin() {
        return javalin;
    }

    private static String getPlayers(Context ctx) {
        JsonArray players = new JsonArray();
        Bukkit.getOnlinePlayers().forEach(player -> {
            JsonObject playerJson = new JsonObject();
            playerJson.addProperty("uuid", player.getUniqueId().toString());
            playerJson.addProperty("name", player.getName());
            playerJson.addProperty("ip", player.getAddress().getAddress().getHostAddress());
            playerJson.addProperty("status", "online");
            players.add(playerJson);
        });

        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            if (!Bukkit.getOnlinePlayers().contains(offlinePlayer)) {
                JsonObject playerJson = new JsonObject();
                playerJson.addProperty("uuid", offlinePlayer.getUniqueId().toString());
                playerJson.addProperty("name", offlinePlayer.getName());
                playerJson.addProperty("lastLogin", offlinePlayer.getLastLogin());
                playerJson.addProperty("lastseen", offlinePlayer.getLastSeen());
                playerJson.addProperty("lastLogin-TimeStamp", timeFormatter(offlinePlayer.getLastLogin()));
                playerJson.addProperty("lastseen-TimeStamp", timeFormatter(offlinePlayer.getLastSeen()));
                playerJson.addProperty("status", "offline");
                players.add(playerJson);
            }
        }
        return players.toString();
    }

    private static String timeFormatter(long millis) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }
}
