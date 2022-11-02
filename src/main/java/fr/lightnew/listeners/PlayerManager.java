package fr.lightnew.listeners;

import fr.lightnew.MainFac;
import fr.lightnew.kit.DefaultKit;
import fr.lightnew.tools.MessagesPreset;
import fr.lightnew.tools.PlayersCache;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PlayerManager implements Listener {

    public static boolean toFaction(Player player) {
        if (MainFac.instance.listPlayerCache.get(player).getFactionID() == 0)
            return false;
        return true;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (Bukkit.getOfflinePlayer(player.getName()).hasPlayedBefore()) {
            player.sendMessage(MessagesPreset.message_custom_re_join.replace("%player%", player.getName()));
        } else {
            player.sendMessage(MessagesPreset.message_welcome.replace("%player%", player.getName()));
            DefaultKit.send(player);
        }
        event.setJoinMessage(MessagesPreset.message_join.replace("%player%", player.getName()));
        new PlayersCache(player);
    }

    @EventHandler
    public void move(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Chunk chunk = player.getWorld().getChunkAt(player.getLocation());
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
                chunk.getPersistentDataContainer().has(new NamespacedKey(MainFac.instance, "createBy"), PersistentDataType.STRING) +
                " | " +
                MainFac.instance.listPlayerCache.get(player).getFactionID()));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage(MessagesPreset.message_quit.replace("%player%", player.getName()));
    }
}