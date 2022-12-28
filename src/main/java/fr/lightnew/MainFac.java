package fr.lightnew;

import fr.lightnew.faction.*;
import fr.lightnew.listeners.*;
import fr.lightnew.tools.ObjectsPreset;
import org.anjocaido.groupmanager.GroupManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Server;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
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
    public static GroupManager groupManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        final Plugin GMplugin = getServer().getPluginManager().getPlugin("GroupManager");
        groupManager = (GroupManager)GMplugin;
        log(ChatColor.GRAY + "[" + ChatColor.GREEN + "JLFaction" + ChatColor.GRAY + "] " + ChatColor.GREEN + "Plugin is Enable");
        //TODO LISTENER
        Bukkit.getPluginManager().registerEvents(new PlayerManager(), this);
        Bukkit.getPluginManager().registerEvents(new ChatManager(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteract(), this);
        Bukkit.getPluginManager().registerEvents(new ProtectClaim(), this);
        Bukkit.getPluginManager().registerEvents(new ListPing(), this);
        //TODO COMMANDS
        getCommand("faction").setTabCompleter(new FacCommands());
        getCommand("faction").setExecutor(new FacCommands());
        getCommand("spawn").setExecutor(new Spawn());
        getCommand("setspawn").setExecutor(new SetSpawn());
        //Load Preset
        loadPreset();
    }

    public static boolean hasGroupManager() {

        if (MainFac.groupManager != null) return true;

        final PluginManager pluginManager = MainFac.instance.getServer().getPluginManager();
        final Plugin GMplugin = pluginManager.getPlugin("GroupManager");

        if (GMplugin != null && GMplugin.isEnabled()) {
            groupManager = (GroupManager)GMplugin;
            return true;
        }
        return false;
    }


    @Override
    public void onDisable() {
        if (!MainFac.spawnFile.exists()) {
            try {MainFac.spawnFile.createNewFile();} catch (IOException e) {throw new RuntimeException(e);}
        } else {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(MainFac.spawnFile);
            List<int[]> loc = new ArrayList<>();
            if (ObjectsPreset.chunks_zone_safe != null) {
                for (Chunk chunk : ObjectsPreset.chunks_zone_safe)
                    loc.add(new int[]{chunk.getZ(), chunk.getX()});
                config.set("zone-safe", loc);
                try {config.save(MainFac.spawnFile);} catch (IOException e) {throw new RuntimeException(e);}
            }
        }
        // todo send lobby
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isOnline())
                player.kickPlayer(ChatColor.RED + "Reload en cours...");
        }
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
