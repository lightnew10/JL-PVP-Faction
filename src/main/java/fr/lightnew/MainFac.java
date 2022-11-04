package fr.lightnew;

import fr.lightnew.faction.FacCommands;
import fr.lightnew.faction.Faction;
import fr.lightnew.faction.Ranks;
import fr.lightnew.listeners.ChatManager;
import fr.lightnew.listeners.PlayerManager;
import fr.lightnew.tools.GetUUIDPlayer;
import fr.lightnew.tools.MessagesPreset;
import fr.lightnew.faction.PlayersCache;
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
    public int idFac;
    private File folder = new File("plugins/JLFac/Factions");
    private File configFac = new File("plugins/JLFac/Factions", "config.yml");
    public List<String> banWordNameFaction;
    public HashMap<Integer, Faction> listFaction = new HashMap<>();
    public List<String> listNameFaction = new ArrayList<>();
    public WeakHashMap<Player, PlayersCache> listPlayerCache = new WeakHashMap<>();
    public HashMap</*time*/Integer, /*power*/Integer> powerWithTime = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        log(ChatColor.GRAY + "[" + ChatColor.GREEN + "JLFac" + ChatColor.GRAY + "] " + ChatColor.GREEN + "Plugin is Enable");
        //TODO LISTENER
        Bukkit.getPluginManager().registerEvents(new PlayerManager(), this);
        Bukkit.getPluginManager().registerEvents(new ChatManager(), this);
        //TODO COMMANDS
        getCommand("faction").setTabCompleter(new FacCommands());
        getCommand("faction").setExecutor(new FacCommands());
        //Load Preset
        log(new Date().getTimezoneOffset() +"");
        loadPreset();
    }

    public void loadPreset() {
        log(ChatColor.GREEN + "=========================\n\nLoading preset...\n");
        new MessagesPreset();
        log(ChatColor.YELLOW + "Messages preset is loaded");
        banWordNameFaction = getConfig().getStringList("ban-word-name-faction");
        if  (!folder.exists())
            folder.mkdir();
        if  (!configFac.exists()) {
            try {
                configFac.createNewFile();
                YamlConfiguration config = YamlConfiguration.loadConfiguration(configFac);
                config.set("Faction.id", 0);
                config.set("list-name-faction", new ArrayList<>());
                config.save(configFac);
            } catch (IOException e) {throw new RuntimeException(e);}
        }
        idFac = YamlConfiguration.loadConfiguration(configFac).getInt("Faction.id");
        log(ChatColor.YELLOW + "IDFac base is loaded");
        if (configFac.exists()) {
            listNameFaction.clear();
            YamlConfiguration config = YamlConfiguration.loadConfiguration(configFac);
            listNameFaction = config.getStringList("listNameFaction");
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
            listFaction.put(id, new Faction(id, name, description, slots, Bukkit.getPlayer(GetUUIDPlayer.getPlayerUUID(owner)), level, claims, power, ally, enemy, playerList, location_home));
        }
        log(ChatColor.YELLOW + "All Factions is loaded");
        ConfigurationSection section = getConfig().getConfigurationSection("power");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                int time = getConfig().getInt("power." + key + ".time");
                int power = getConfig().getInt("power." + key + ".power");
                powerWithTime.put(time, power);
            }
            log(ChatColor.YELLOW + "All time of power is loaded");
        } else
            log(ChatColor.RED + "Time of power is not loaded");
        log(ChatColor.GREEN + "\n\n=========================");
    }

    @Override
    public void onDisable() {
        YamlConfiguration conf  = YamlConfiguration.loadConfiguration(configFac);
        conf.set("Faction.id", idFac);
        conf.set("list-name-faction", listNameFaction);
        try {conf.save(configFac);} catch (IOException e) {throw new RuntimeException(e);}
        log(ChatColor.GRAY + "[" + ChatColor.RED + "JLFac" + ChatColor.GRAY + "] " + ChatColor.GREEN + "Plugin is Disable");
    }

    public static void log(String s) {Bukkit.getConsoleSender().sendMessage(s);}
}
