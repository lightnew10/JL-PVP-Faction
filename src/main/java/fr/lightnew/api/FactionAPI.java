package fr.lightnew.api;

import fr.lightnew.MainFac;
import fr.lightnew.faction.Faction;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class FactionAPI {

    public Faction getFactionPlayer(Player player) {
        return MainFac.getFactions().get(player);
    }

    public String getNameFactionChunk(Player player) {
        Chunk chunk = player.getWorld().getChunkAt(player.getLocation());
        return chunk.getPersistentDataContainer().has(new NamespacedKey(MainFac.instance, "faction"), PersistentDataType.INTEGER) ? ChatColor.RED + String.valueOf(chunk.getPersistentDataContainer().get(new NamespacedKey(MainFac.instance, "faction_name"), PersistentDataType.STRING)) : ChatColor.GREEN + "Wilderness";
    }
}