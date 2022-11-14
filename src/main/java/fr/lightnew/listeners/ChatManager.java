package fr.lightnew.listeners;

import fr.lightnew.MainFac;
import fr.lightnew.faction.Faction;
import fr.lightnew.tools.ObjectsPreset;
import fr.lightnew.faction.UserData;
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
        UserData cache = MainFac.instance.playersCache.get(player);
        String message = event.getMessage().replace("%", "%%");

        if (PlayerManager.toFaction(player)) {
            String formatWithFaction = ObjectsPreset.chat_format_with_faction.replace('&', '§').replace("%faction%", MainFac.getFactions().get(player).getName())
                    .replace("%grade%", cache.getGrade()).replace("%player%", player.getName()).replace("%hour%", hour);

            if (player.hasPermission("chat.color"))
                event.setFormat(ChatColor.RED + "✱" + ChatColor.RESET + formatWithFaction.replace("%message%", ChatColor.translateAlternateColorCodes('&', message)));
            else
                event.setFormat(ChatColor.RED + "✱" + ChatColor.RESET + formatWithFaction.replace("%message%", message));
        } else {
            String formatWithOutFaction = ObjectsPreset.chat_format_without_faction.replace('&', '§').replace("%grade%", cache.getGrade())
                    .replace("%player%", player.getName()).replace("%hour%", hour);

            if (player.hasPermission("chat.color"))
                event.setFormat(ChatColor.RED + "✱" + ChatColor.RESET + formatWithOutFaction.replace("%message%", ChatColor.translateAlternateColorCodes('&', message)));
            else
                event.setFormat(ChatColor.RED + "✱" + ChatColor.RESET + formatWithOutFaction.replace("%message%", message));
        }
    }
}
