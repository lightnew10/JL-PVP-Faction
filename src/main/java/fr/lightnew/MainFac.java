package fr.lightnew;

import fr.lightnew.faction.*;
import fr.lightnew.listeners.ChatManager;
import fr.lightnew.listeners.PlayerInteract;
import fr.lightnew.listeners.PlayerManager;
import fr.lightnew.tools.GetUUIDPlayer;
import fr.lightnew.tools.ObjectsPreset;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class MainFac extends JavaPlugin {

    public static MainFac instance;
    public static WeakHashMap<Player, Faction> factions = new WeakHashMap<>();
    private File folder = new File("plugins/JLFac/Factions");
    public File configFac = new File("plugins/JLFac/Factions", "config.yml");
    public List<Faction> listFaction = new ArrayList<>();
    public List<String> namesOfFactions = new ArrayList<>();
    public WeakHashMap<Player, UserData> playersCache = new WeakHashMap<>();
    public HashMap<Integer, Integer> powerWithTime = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        log(ChatColor.GRAY + "[" + ChatColor.GREEN + "JLFac" + ChatColor.GRAY + "] " + ChatColor.GREEN + "Plugin is Enable");
        //TODO LISTENER
        Bukkit.getPluginManager().registerEvents(new PlayerManager(), this);
        Bukkit.getPluginManager().registerEvents(new ChatManager(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteract(), this);
        //TODO COMMANDS
        getCommand("faction").setTabCompleter(new FacCommands());
        getCommand("faction").setExecutor(new FacCommands());
        //Load Preset
        loadPreset();
    }

    @Override
    public void onDisable() {
        YamlConfiguration conf  = YamlConfiguration.loadConfiguration(configFac);
        conf.set("Faction.id", ObjectsPreset.idFac);
        conf.set("list-name-faction", namesOfFactions);
        try {conf.save(configFac);} catch (IOException e) {throw new RuntimeException(e);}
        log(ChatColor.GRAY + "[" + ChatColor.RED + "JLFac" + ChatColor.GRAY + "] " + ChatColor.GREEN + "Plugin is Disable");
    }

    public void loadPreset() {

        log(ChatColor.GREEN + "=========================\n\nLoading preset...\n");

        new ObjectsPreset();

        if (!folder.exists())
            folder.mkdir();
        if (!configFac.exists()) {
            try {
                configFac.createNewFile();
                YamlConfiguration config = YamlConfiguration.loadConfiguration(configFac);
                config.set("Faction.id", 0);
                config.set("list-name-faction", new ArrayList<>());
                config.save(configFac);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        log(ChatColor.YELLOW + "IDFac base is loaded");
        if (configFac.exists()) {
            namesOfFactions.clear();
            YamlConfiguration config = YamlConfiguration.loadConfiguration(configFac);
            namesOfFactions = config.getStringList("listNameFaction");
        }

        log(ChatColor.YELLOW + "List factions name is loaded");

        /*Load all faction*/
        log(ChatColor.YELLOW + "Load configuration faction...");
        for (File file : folder.listFiles()) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            int id = config.getInt("information-details.id");
            String owner = config.getString("information-details.owner");
            String name = config.getString("information-details.name");
            String description = config.getString("information-details.description");
            int slots = config.getInt("information-details.slots");
            int level = config.getInt("information-details.level");
            List<Chunk> claims = (List<Chunk>) config.getList("information-details.claims");
            int power = config.getInt("information-details.power");
            Location location_home = (Location) config.get("information-details.location-home");
            List<Faction> ally = (List<Faction>) config.getList("information-everyone.ally");
            List<Faction> enemy = (List<Faction>) config.getList("information-everyone.enemy");
            HashMap<Player, Ranks> playerList = new HashMap<>();

            ConfigurationSection section = config.getConfigurationSection("information-list-member");
            //verify
            if (section != null) {
                //search in file
                for (String key : section.getKeys(false)) {
                    String rank = config.getString("information-list-member." + key + ".RANK");
                    String playerName = key;
                    playerList.put(Bukkit.getOfflinePlayer(playerName).getPlayer(), Ranks.valueOf(rank));
                }
            }
            listFaction.add(new Faction(id, name, description, slots, Bukkit.getPlayer(GetUUIDPlayer.getPlayerUUID(owner)), level, claims, power, ally, enemy, playerList, location_home));
        }
        log(ChatColor.YELLOW + "All Factions is loaded " + ChatColor.GRAY + "(" + (folder.listFiles().length-1) + " Faction's is loaded)");

        ConfigurationSection section = getConfig().getConfigurationSection("power");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                int time = getConfig().getInt("power." + key + ".time");
                int power = getConfig().getInt("power." + key + ".power");
                powerWithTime.put(time, power);
            }
            log(ChatColor.YELLOW + "All time of power is loaded");
        } else
            log(ChatColor.RED + "Time of power is not loaded\n");
        log(ChatColor.GREEN + "=========================");
    }

    public static WeakHashMap<Player, Faction> getFactions() {
        return factions;
    }

    public static void log(String s) {Bukkit.getConsoleSender().sendMessage(s);}
}
