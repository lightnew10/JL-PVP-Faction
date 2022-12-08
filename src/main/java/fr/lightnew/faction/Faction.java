package fr.lightnew.faction;

import fr.lightnew.MainFac;
import fr.lightnew.tools.ObjectsPreset;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Faction {
    private UUID id;
    private String name;
    private String description;
    private int slots;
    private Player owner;
    private int level;

    private List<Chunk> claims;
    private int power;
    private List<UUID> ally;
    private List<UUID> enemy;
    private HashMap<Player, String> playerList = new HashMap<>();
    private List<RankManager> ranks = new ArrayList<>();

    private Location location_home;

    public Faction(Player player, String name, String description) {
        this.id = UUID.randomUUID();

        this.name = name;
        this.description = description;
        this.slots = 1;
        this.owner = player;
        this.level = 0;
        this.claims = new ArrayList<>();
        this.power = 10;
        this.ally = new ArrayList<>();
        this.enemy = new ArrayList<>();
        this.playerList.put(player, Ranks.CHEF.toString());
        location_home = null;
        MainFac.factions.put(player, this);
        MainFac.instance.namesOfFactions.add(name);
        PermissionManager manager = new PermissionManager();
        manager.setAllON();
        ranks.add(new RankManager(Ranks.CHEF.name(), Ranks.CHEF.name(), manager));
        PermissionManager m = new PermissionManager();
        m.setAllOFF();
        ranks.add(new RankManager(Ranks.RECRUE.name(), Ranks.RECRUE.name(), m));
        ranks.add(new RankManager(Ranks.ADJOINT.name(), Ranks.ADJOINT.name(), m));

        addCache();
    }

    /*GETTER*/
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getSlots() {
        return slots;
    }

    public Player getOwner() {
        return owner;
    }

    public int getLevel() {
        return level;
    }

    public int getPower() {
        //todo recreate function for power faction
        /*
        * Calcul power :
        * get all power member and create mean /10
        */
        int calcul = 0;
        for (Player member : getPlayerList().keySet()) {
            UserData cache = new UserData(member);
            calcul = calcul + cache.getPower();
        }
        if (calcul <= 0) {
            setPower(0);
            return 0;
        }
        setPower(calcul);
        return calcul/getPlayerList().size();
    }

    public List<UUID> getAlly() {
        return ally;
    }

    public List<UUID> getEnemy() {
        return enemy;
    }

    public List<Chunk> getClaims() {
        return claims;
    }

    public Location getLocation_home() {
        return location_home;
    }

    public HashMap<Player, String> getPlayerList() {
        return playerList;
    }

    public int getMaxSlot() {
        return ObjectsPreset.maxSlotFaction;
    }

    public List<RankManager> getRanks() {
        return ranks;
    }

    /*SETTER*/

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setAlly(UUID ally) {
        this.ally.add(ally);
    }

    public void setLocation_home(Location location_home) {
        this.location_home = location_home;
    }

    public void setEnemy(UUID enemy) {
        this.enemy.add(enemy);
    }
    public boolean addClaim(Chunk claim) {
        return this.claims.add(claim);
    }
    public boolean removeClaim(Chunk claim) {
        return this.claims.remove(claim);
    }

    public void setPlayerList(Player player, String ranks) {
        this.playerList.put(player, ranks);
    }

    /*Construct Faction*/

    private void addCache() {
        if (MainFac.instance.listFaction.contains(this)) {
            MainFac.instance.listFaction.remove(this);
            MainFac.instance.listFaction.add(this);
        } else MainFac.instance.listFaction.add(this);
    }

    public void addPlayerFaction(Player target) {
        UserData data = MainFac.instance.playersCache.get(target);
        data.setRanks(Ranks.RECRUE);
        data.sendModifications();
        playerList.put(target, Ranks.RECRUE.toString());
        MainFac.getFactions().put(target, this);
    }

    public void removePlayer(Player target) {
        UserData data = MainFac.instance.playersCache.get(target);
        data.setRanks(Ranks.NONE);
        data.sendModifications();
        playerList.remove(target);
        MainFac.getFactions().remove(target, this);
    }

    public void addRank(RankManager manager) {
        if (!ranks.contains(manager))
            ranks.add(manager);
    }

    public Boolean removeRanks(RankManager manager) {
        if (ranks.contains(manager)) {
            ranks.remove(manager);
            return true;
        }
        return false;
    }

    public Boolean changePermissionRank(RankManager manager) {
        if (ranks.contains(manager)) {
            ranks.set(ranks.indexOf(manager), manager);
            return true;
        }
        return false;
    }

    public Boolean remove() throws InterruptedException {
        if (claims.size() != 0) {
            PersistentDataContainer container;
            NamespacedKey key_is_claimed = new NamespacedKey(MainFac.instance, "claim");
            NamespacedKey key_get_faction = new NamespacedKey(MainFac.instance, "faction");
            NamespacedKey key_get_creator = new NamespacedKey(MainFac.instance, "createBy");
            NamespacedKey key_get_faction_name = new NamespacedKey(MainFac.instance, "faction_name");
            for (int i = 0; i < claims.size(); i++) {
                container = claims.get(i).getPersistentDataContainer();
                if (container.has(key_is_claimed, PersistentDataType.INTEGER))
                    container.remove(key_is_claimed);
                if (container.has(key_get_faction, PersistentDataType.STRING))
                    container.remove(key_get_faction);
                if (container.has(key_get_creator, PersistentDataType.STRING))
                    container.remove(key_get_creator);
                if (container.has(key_get_faction_name, PersistentDataType.STRING))
                    container.remove(key_get_faction_name);
            }
            claims.clear();
        }
        MainFac.instance.namesOfFactions.remove(getName());
        MainFac.instance.listFaction.remove(this);
        for (Player player : playerList.keySet())
            MainFac.getFactions().remove(player, this);
        playerList.clear();
        ally.clear();
        enemy.clear();
        location_home = null;
        return true;
    }

}
