package fr.lightnew.listeners;

import fr.lightnew.MainFac;
import fr.lightnew.api.CombatAPI;
import fr.lightnew.faction.Spawn;
import fr.lightnew.faction.UserData;
import fr.lightnew.kit.DefaultKit;
import fr.lightnew.tools.ObjectsPreset;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class PlayerManager implements Listener {

    public static boolean toFaction(Player player) {
        if (MainFac.getFactions().get(player) == null)
            return false;
        return true;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (Bukkit.getOfflinePlayer(player.getName()).hasPlayedBefore()) {
            player.sendMessage(ObjectsPreset.message_custom_re_join.replace("%player%", player.getName()).replace("&", "§"));
        } else {
            player.sendMessage(ObjectsPreset.message_welcome.replace("%player%", player.getName()));
            DefaultKit.send(player);
        }
        event.setJoinMessage(ObjectsPreset.message_join.replace("%player%", player.getName()));
        new UserData(player);
    }

    public static String getClaimHere(Player player) {
        Chunk chunk = player.getWorld().getChunkAt(player.getLocation());
        if (chunk.getPersistentDataContainer().has(new NamespacedKey(MainFac.instance, "faction"), PersistentDataType.STRING)) {
            if (Objects.equals(chunk.getPersistentDataContainer().get(new NamespacedKey(MainFac.instance, "faction_name"), PersistentDataType.STRING), ObjectsPreset.name_claim_spawn))
                return ChatColor.GOLD + "Zone Safe";
            return ChatColor.RED + chunk.getPersistentDataContainer().get(new NamespacedKey(MainFac.instance, "faction_name"), PersistentDataType.STRING);
        }
        return ChatColor.GREEN + "Wilderness";
    }

    @EventHandler
    public void move(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        CombatAPI combatAPI = new CombatAPI(player);
        if (!combatAPI.inCombat()) {
            TextComponent text = new TextComponent(getClaimHere(player));

            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(text));
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage(ObjectsPreset.message_quit.replace("%player%", player.getName()));
    }

    @EventHandler
    public void respawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (Spawn.spawnLocation != null)
            player.teleport(Spawn.spawnLocation);
    }

}