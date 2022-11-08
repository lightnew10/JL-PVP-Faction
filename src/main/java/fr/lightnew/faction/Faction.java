package fr.lightnew.faction;

import fr.lightnew.MainFac;
import fr.lightnew.tools.ObjectsPreset;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Faction {
    private String filePath = new File("plugins/JLFac/Factions").getPath();
    private int id;
    private String name;
    private String description;
    private int slots;
    private Player owner;
    private int level;
    private List<Chunk> claims;
    private int power;
    private List<Faction> ally;
    private List<Faction> enemy;
    private HashMap<Player, Ranks> playerList = new HashMap<>();

    private Location location_home;
    private int maxSlot;

    public Faction(Player player, String name, String description) {
        this.id = (ObjectsPreset.idFac + 1);

        ObjectsPreset.idFac = (ObjectsPreset.idFac+1);

        this.name = name;
        this.description = description;
        this.slots = 1;
        this.owner = player;
        this.level = 0;
        this.claims = new ArrayList<>();
        this.power = 10;
        this.ally = new ArrayList<>();
        this.enemy = new ArrayList<>();
        this.playerList.put(player, Ranks.NONE);
        location_home = null;
        this.maxSlot = ObjectsPreset.maxslotFaction;
        MainFac.factions.put(player, this);
        MainFac.instance.NamesOfFactions.add(name);

        addCache();
        createFile();
    }

    public Faction(int id) {
        this.id = id;
        this.name = MainFac.instance.listFaction.get(id).getName();
        this.description = MainFac.instance.listFaction.get(id).getDescription();
        this.slots = MainFac.instance.listFaction.get(id).getSlots();
        this.owner = MainFac.instance.listFaction.get(id).getOwner();
        this.level = MainFac.instance.listFaction.get(id).getLevel();
        this.claims = MainFac.instance.listFaction.get(id).getClaims();
        this.power = MainFac.instance.listFaction.get(id).getPower();
        this.ally = MainFac.instance.listFaction.get(id).getAlly();
        this.enemy = MainFac.instance.listFaction.get(id).getEnemy();
        this.playerList = MainFac.instance.listFaction.get(id).getPlayerList();
        this.location_home = MainFac.instance.listFaction.get(id).getLocation_home();
    }

    public Faction(int idLoad, String nameLoad, String descriptionLoad, int slotsLoad, Player ownerLoad, int levelLoad, List<Chunk> claimsLoad, int powerLoad, List<Faction> ally, List<Faction> enemy, HashMap<Player, Ranks> playerListLoad, Location location_homeLoad) {
        this.id = idLoad;
        this.name = nameLoad;
        this.description = descriptionLoad;
        this.slots = slotsLoad;
        this.owner = ownerLoad;
        this.level = levelLoad;
        this.claims = claimsLoad;
        this.power = powerLoad;
        this.ally = ally;
        this.enemy = enemy;
        this.playerList = playerListLoad;
        this.location_home = location_homeLoad;
    }

    /*GETTER*/
    public int getId() {
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
        /*
        * Calcul power :
        * get all power member and create mean /10
        */
        int calcul = 0;
        for (Player member : getPlayerList().keySet()) {
            UserData cache = new UserData(member);
            calcul = calcul + cache.getPower();
        }
        if (calcul <=0) {
            setPower(0);
            return 0;
        }
        setPower(calcul);
        return calcul;
    }

    public List<Faction> getAlly() {
        return ally;
    }

    public List<Faction> getEnemy() {
        return enemy;
    }

    public List<Chunk> getClaims() {
        return claims;
    }

    public Location getLocation_home() {
        return location_home;
    }

    public HashMap<Player, Ranks> getPlayerList() {
        return playerList;
    }

    public int getMaxSlot() {
        return maxSlot;
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

    public void setAlly(Faction ally) {
        this.ally.add(ally);
    }

    public void setEnemy(Faction enemy) {
        this.enemy.add(enemy);
    }
    public boolean setClaim(Chunk claim) {
        return this.claims.add(claim);
    }
    public boolean removeClaim(Chunk claim) {
        return this.claims.remove(claim);
    }

    public void setPlayerList(Player player, Ranks ranks) {
        this.playerList.put(player, ranks);
    }

    /*Construct Faction*/
    private void createFile() {
        File file = new File(filePath, getName() + "_" + getId() + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

                config.set("information-details.id", getId());
                config.set("information-details.owner", getOwner().getName());
                config.set("information-details.owner-UUID", getOwner().getUniqueId().toString());
                config.set("information-details.name", getName());
                config.set("information-details.description", getDescription());
                config.set("information-details.slots", getSlots());
                config.set("information-details.level", getLevel());
                config.set("information-details.claims", getClaims());
                config.set("information-details.power", getPower());
                config.set("information-details.location-home", getLocation_home());

                config.set("information-everyone.ally", getAlly());
                config.set("information-everyone.enemy", getEnemy());

                config.set("information-list-member." + getOwner().getName() + ".UUID", getOwner().getUniqueId().toString());
                config.set("information-list-member." + getOwner().getName() + ".RANK", getPlayerList().get(getOwner()).name());

                config.save(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void addCache() {
        if (MainFac.instance.listFaction.contains(this)) {
            MainFac.instance.listFaction.remove(this);
            MainFac.instance.listFaction.add(this);
        } else MainFac.instance.listFaction.add(this);
    }

    public Boolean remove() {
        File file = new File(filePath, getName() + "_" + getId() + ".yml");
        MainFac.instance.NamesOfFactions.remove(getName());
        MainFac.instance.listFaction.remove(getId());
        playerList.clear();
        for (Chunk c : claims)
            this.claims.remove(c);
        if (file.exists()) {
            if (file.delete())
                return true;
            else return false;
        }
        return false;
    }

}
