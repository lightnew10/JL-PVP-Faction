package fr.lightnew.listeners;

import fr.lightnew.MainFac;
import fr.lightnew.faction.Faction;
import fr.lightnew.tools.ObjectsPreset;
import fr.lightnew.faction.PlayersCache;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Date;

public class ChatManager implements Listener {

    @EventHandler
    public void chat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Date date = new Date();
        String hour = date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
        PlayersCache cache = MainFac.instance.listPlayerCache.get(player);

        if (PlayerManager.toFaction(player)) {
            String formatWithFaction = ObjectsPreset.chat_format_with_faction.replace("%faction%", new Faction(cache.getFaction().getId()).getName())
                    .replace("%grade%", cache.getGrade()).replace("%player%", player.getName()).replace("%hour%", hour);

            if (player.hasPermission("chat.color"))
                event.setFormat(formatWithFaction.replace("%message%", ChatColor.translateAlternateColorCodes('&', event.getMessage())));
            else
                event.setFormat(formatWithFaction.replace("%message%", event.getMessage()));
        } else {
            String formatWithOutFaction = ObjectsPreset.chat_format_without_faction.replace("%grade%", cache.getGrade())
                    .replace("%player%", player.getName()).replace("%hour%", hour);

            if (player.hasPermission("chat.color"))
                event.setFormat(formatWithOutFaction.replace("%message%", ChatColor.translateAlternateColorCodes('&', event.getMessage())));
            else
                event.setFormat(formatWithOutFaction.replace("%message%", event.getMessage()));
        }
    }
}
