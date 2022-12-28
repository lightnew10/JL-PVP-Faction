package fr.lightnew.listeners;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.ViaAPI;
import com.viaversion.viaversion.api.legacy.bossbar.BossBar;
import com.viaversion.viaversion.api.legacy.bossbar.BossColor;
import com.viaversion.viaversion.api.legacy.bossbar.BossStyle;
import fr.lightnew.MainFac;
import fr.lightnew.api.CombatAPI;
import fr.lightnew.api.ModerationAPI;
import fr.lightnew.faction.Spawn;
import fr.lightnew.faction.UserData;
import fr.lightnew.kit.DefaultKit;
import fr.lightnew.setter.SetterPlayerData;
import fr.lightnew.tools.ClickMSG;
import fr.lightnew.tools.ObjectsPreset;
import fr.lightnew.tools.Requests;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
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
        ViaAPI api = Via.getAPI();
        Requests.createDefaultDataPlayer(player);
        player.spigot().sendMessage(new TextComponent(ChatColor.GRAY + "\n" +
                        ChatColor.YELLOW + "\n           Bienvenue sur le serveur §cJobLife§e!\n                     Suivez nous !\n\n"),
                ClickMSG.clickMSG("                Twitter", net.md_5.bungee.api.ChatColor.BLUE, HoverEvent.Action.SHOW_TEXT, ChatColor.BLUE + "Twitter", ClickEvent.Action.OPEN_URL, "https://twitter.com/joblifeesport?s=21&t=xFt-UJIo2t9Q7pgNUfVpfg"),
                ClickMSG.clickMSG("        Discord", net.md_5.bungee.api.ChatColor.LIGHT_PURPLE, HoverEvent.Action.SHOW_TEXT, ChatColor.LIGHT_PURPLE + "Discord", ClickEvent.Action.OPEN_URL, "https://discord.gg/joblife"),
                new TextComponent(ChatColor.GRAY + "\n"));
        event.setJoinMessage(ObjectsPreset.message_join.replace("%player%", player.getName()));
        new UserData(player);
        if (api.getPlayerVersion(player.getUniqueId()) < 754)
            player.sendMessage(ChatColor.YELLOW + "Le serveur est en 1.16.5, si vous voulez ne pas avoir de \nsoucis avec les blocs mettez à jour votre version.");
        player.sendMessage(MainFac.instance.playersCache.get(player) + "");
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
        if (!combatAPI.inCombat() && !ModerationAPI.playerInMod(player).booleanValue()) {
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