package fr.lightnew.faction;

import fr.lightnew.MainFac;
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

    public ClaimsManager(Player player) {
        this.player = player;
        if (MainFac.instance.listPlayerCache.get(player).getFactionID() > 0) {
            this.chunks = new Faction(MainFac.instance.listPlayerCache.get(player).getFactionID()).getClaims();
            faction = new Faction(MainFac.instance.listPlayerCache.get(player).getFactionID());
        } else {
            chunks = null;
            faction = null;
        }
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
        faction.setClaim(chunk);

        return getChunks().add(chunk);
    }
    public boolean removeClaimedChunk(Chunk chunk) {
        if (chunkHasClaimed(chunk)) {
            container = chunk.getPersistentDataContainer();

            container.remove(key_is_claimed);
            container.remove(key_get_faction);
            container.remove(key_get_creator);
            faction.removeClaim(chunk);

            return getChunks().remove(chunk);
        }
        return false;
    }
    /*verify if chunk is claimed*/
    public boolean chunkHasClaimed(Chunk chunk) {
        if (chunk.getPersistentDataContainer().has(key_is_claimed, PersistentDataType.INTEGER))
            return true;
        return false;
    }
}
