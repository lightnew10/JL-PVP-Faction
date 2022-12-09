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
    public static File spawnFile = new File("plugins/JLFaction", "spawn.yml");
    public List<Faction> listFaction = new ArrayList<>();
    public List<String> namesOfFactions = new ArrayList<>();
    public WeakHashMap<Player, UserData> playersCache = new WeakHashMap<>();
    public HashMap<Integer, Integer> powerWithTime = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        log(ChatColor.GRAY + "[" + ChatColor.GREEN + "JLFaction" + ChatColor.GRAY + "] " + ChatColor.GREEN + "Plugin is Enable");
        //TODO LISTENER
        Bukkit.getPluginManager().registerEvents(new PlayerManager(), this);
        Bukkit.getPluginManager().registerEvents(new ChatManager(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteract(), this);
        //TODO COMMANDS
        getCommand("faction").setTabCompleter(new FacCommands());
        getCommand("faction").setExecutor(new FacCommands());
        getCommand("spawn").setExecutor(new Spawn());
        getCommand("setspawn").setExecutor(new SetSpawn());
        //Load Preset
        loadPreset();
    }

    @Override
    public void onDisable() {
        log(ChatColor.GRAY + "[" + ChatColor.RED + "JLFac" + ChatColor.GRAY + "] " + ChatColor.RED + "Plugin is Disable");
    }

    public void loadPreset() {

        log(ChatColor.GREEN + "=========================");
        log(ChatColor.GREEN + "Loading preset...");

        new ObjectsPreset();

        log(ChatColor.GREEN + "Loading spawnFile...");
        if (!spawnFile.exists()) {
            Spawn.spawnLocation = null;
            try {
                spawnFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(MainFac.spawnFile);
            Spawn.spawnLocation = config.getLocation("location");
        }
        log(ChatColor.YELLOW + "SpawnFile loaded !");
        log(ChatColor.GREEN + "=========================");
    }

    public static WeakHashMap<Player, Faction> getFactions() {
        return factions;
    }

    public static void log(String s) {Bukkit.getConsoleSender().sendMessage(s);}
}
