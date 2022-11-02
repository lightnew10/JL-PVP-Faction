package fr.lightnew.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public abstract class GetUUIDPlayer {
    public static String getPlayerUUID(Player playerName) throws IOException {
        URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName);
        InputStream input = url.openConnection().getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String readLine = reader.readLine();
        String uuid = readLine.substring(readLine.length() - 34, readLine.length() - 2);
        StringBuffer send = new StringBuffer(uuid);
        send.insert(8, "-");
        send.insert(13, "-");
        send.insert(18, "-");
        send.insert(23, "-");
        return String.valueOf(send);
    }

    public static String getPlayerUUID(OfflinePlayer playerName) throws IOException {
        URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName);
        InputStream input = url.openConnection().getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String readLine = reader.readLine();
        String uuid = readLine.substring(readLine.length() - 34, readLine.length() - 2);
        StringBuffer send = new StringBuffer(uuid);
        send.insert(8, "-");
        send.insert(13, "-");
        send.insert(18, "-");
        send.insert(23, "-");
        return String.valueOf(send);
    }

    public static String getPlayerUUID(String playerName) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName);
            InputStream input = url.openConnection().getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String readLine = reader.readLine();
            String uuid = readLine.substring(readLine.length() - 34, readLine.length() - 2);
            StringBuffer send = new StringBuffer(uuid);
            send.insert(8, "-");
            send.insert(13, "-");
            send.insert(18, "-");
            send.insert(23, "-");
            return String.valueOf(send);
        }catch (IOException e) {throw new RuntimeException(e);}
    }
}
