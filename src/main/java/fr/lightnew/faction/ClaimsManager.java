package fr.lightnew.faction;

import fr.lightnew.MainFac;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class ClaimsManager {
    private Player player;
    private List<Chunk> chunks;

    private PersistentDataContainer container;
    private Faction faction;
    private NamespacedKey key_is_claimed = new NamespacedKey(MainFac.instance, "claim");
    private NamespacedKey key_get_faction = new NamespacedKey(MainFac.instance, "faction");
    private NamespacedKey key_get_creator = new NamespacedKey(MainFac.instance, "createBy");

    public ClaimsManager(Faction faction, Player player) {
        this.player = player;
        if (faction != null) {
            this.chunks = faction.getClaims();
            this.faction = faction;
        } else
            chunks = null;
    }

    public ClaimsManager(Faction faction) {
        if (faction != null) {
            this.chunks = faction.getClaims();
            this.faction = faction;
        } else
            chunks = null;
    }

    /*GETTER && SETTER*/
    public Player getPlayer() {
        return player;
    }
    public List<Chunk> getChunks() {
        return chunks;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
    public boolean setClaimedChunk(Chunk chunk) {
        if (chunkHasClaimed(chunk))
            return false;
        container = chunk.getPersistentDataContainer();

        container.set(key_is_claimed, PersistentDataType.INTEGER, 1);
        container.set(key_get_faction, PersistentDataType.INTEGER, faction.getId());
        container.set(key_get_creator, PersistentDataType.STRING, player.getUniqueId().toString());
        faction.addClaim(chunk);

        return getChunks().add(chunk);
    }

    public void removeClaimedChunk(Chunk chunk) {
        container = chunk.getPersistentDataContainer();
        //if (container.has(key_is_claimed, PersistentDataType.INTEGER))
            container.remove(key_is_claimed);
        //if (container.has(key_get_faction, PersistentDataType.INTEGER))
            container.remove(key_get_faction);
        //if (container.has(key_get_creator, PersistentDataType.STRING))
            container.remove(key_get_creator);
        Bukkit.broadcastMessage("FAIT POUR LE CLAIM");
    }

    //TODO REMOVE FOR END PLUGIN
    public void removeAClaimedChunk(Chunk chunk) {
        container = chunk.getPersistentDataContainer();
        if (container.has(key_is_claimed, PersistentDataType.INTEGER))
            container.remove(key_is_claimed);
        if (container.has(key_get_faction, PersistentDataType.INTEGER))
            container.remove(key_get_faction);
        if (container.has(key_get_creator, PersistentDataType.STRING))
            container.remove(key_get_creator);
    }
    /*verify if chunk is claimed*/
    public boolean chunkHasClaimed(Chunk chunk) {
        if (chunk.getPersistentDataContainer().has(key_is_claimed, PersistentDataType.INTEGER))
            return true;
        return false;
    }
    public boolean chunkHasClaimedByYourFaction(Chunk chunk, Faction faction) {
        if (chunkHasClaimed(chunk)) {
            container = chunk.getPersistentDataContainer();
            if (container.get(key_get_faction, PersistentDataType.INTEGER) == faction.getId())
                return true;
            return false;
        }
        return false;
    }
}
