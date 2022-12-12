package fr.lightnew.listeners;

import fr.lightnew.MainFac;
import fr.lightnew.faction.FacCommands;
import fr.lightnew.tools.ObjectsPreset;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class ProtectClaim implements Listener {

    private static NamespacedKey key_is_claimed = new NamespacedKey(MainFac.instance, "claim");
    private static NamespacedKey key_get_faction_name = new NamespacedKey(MainFac.instance, "faction_name");

    public static Boolean hasPermissionChunk(Player player) {
        Chunk chunk = player.getWorld().getChunkAt(player.getLocation());
        PersistentDataContainer container = chunk.getPersistentDataContainer();
        if (container.has(key_is_claimed, PersistentDataType.INTEGER)) {
            if (Objects.equals(container.get(key_get_faction_name, PersistentDataType.STRING), ObjectsPreset.name_claim_spawn))
                return false;
            if (FacCommands.getInFaction(player))
                if (Objects.equals(container.get(key_get_faction_name, PersistentDataType.STRING), FacCommands.getFaction(player).getName()))
                    return true;
        }
        return true;
    }

    public static Boolean inSpawn(Player player) {
        Chunk chunk = player.getWorld().getChunkAt(player.getLocation());
        PersistentDataContainer container = chunk.getPersistentDataContainer();
        if (container.has(key_is_claimed, PersistentDataType.INTEGER) && Objects.equals(container.get(key_get_faction_name, PersistentDataType.STRING), ObjectsPreset.name_claim_spawn))
            return true;
        return false;
    }

    @EventHandler
    public void playerBlockBreak(BlockBreakEvent event) {
        if (!hasPermissionChunk(event.getPlayer()))
            event.setCancelled(true);
    }

    @EventHandler
    public void playerBlockPlace(BlockPlaceEvent event) {
        if (!hasPermissionChunk(event.getPlayer()))
            event.setCancelled(true);
    }

    @EventHandler
    public void playerInteract(PlayerInteractEvent event) {
        if (!hasPermissionChunk(event.getPlayer()))
            event.setCancelled(true);
    }

    @EventHandler
    public void damage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (inSpawn(player))
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void damageByBlock(EntityDamageByBlockEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (inSpawn(player))
                event.setCancelled(true);
        }
    }
}
