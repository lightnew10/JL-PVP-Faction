package fr.lightnew.faction;

import fr.lightnew.MainFac;
import fr.lightnew.tools.ObjectsPreset;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.IOException;
import java.util.Objects;

public class SetSpawn implements CommandExecutor {

    private NamespacedKey key_is_claimed = new NamespacedKey(MainFac.instance, "claim");
    private NamespacedKey key_get_faction = new NamespacedKey(MainFac.instance, "faction");
    private NamespacedKey key_get_faction_name = new NamespacedKey(MainFac.instance, "faction_name");
    private NamespacedKey key_get_creator = new NamespacedKey(MainFac.instance, "createBy");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.isOp()) {
                if (args.length == 0) {
                    Spawn.spawnLocation = player.getLocation();
                    player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.YELLOW + "Vous avez placé le point de spawn " + ChatColor.GRAY + "(X:" + player.getLocation().getBlockX() + " Y:" + player.getLocation().getBlockY() + " Z:" + player.getLocation().getBlockZ());
                    if (!MainFac.spawnFile.exists()) {
                        Spawn.spawnLocation = null;
                        try {
                            MainFac.spawnFile.createNewFile();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        YamlConfiguration config = YamlConfiguration.loadConfiguration(MainFac.spawnFile);
                        config.set("location", player.getLocation());
                        try {
                            config.save(MainFac.spawnFile);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    Bukkit.getWorld("world").setSpawnLocation(player.getLocation());
                }
                if (args.length == 1) {
                    PersistentDataContainer container = player.getWorld().getChunkAt(player.getLocation()).getPersistentDataContainer();
                    if (args[0].equalsIgnoreCase("claim")) {
                        if (ObjectsPreset.chunks_zone_safe != null)
                            if (ObjectsPreset.chunks_zone_safe.contains(player.getWorld().getChunkAt(player.getLocation()))) {
                                player.sendMessage("Ce claim est déjà dans la zone safe !");
                                return true;
                            }
                        if (container.has(key_get_faction_name, PersistentDataType.STRING)) {
                            player.sendMessage(ChatColor.RED + "Ce claim appartient à un joueur !");
                            return true;
                        }

                        container.set(key_is_claimed, PersistentDataType.INTEGER, 1);
                        container.set(key_get_faction, PersistentDataType.STRING, "0");
                        container.set(key_get_faction_name, PersistentDataType.STRING, ObjectsPreset.name_claim_spawn);
                        container.set(key_get_creator, PersistentDataType.STRING, player.getUniqueId().toString());

                        player.sendMessage(ChatColor.YELLOW + "Vous venez de protéger ce claim.");
                        ObjectsPreset.chunks_zone_safe.add(player.getWorld().getChunkAt(player.getLocation()));
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("unclaim")) {
                        if (Objects.equals(container.get(key_get_faction_name, PersistentDataType.STRING), ObjectsPreset.name_claim_spawn)) {
                            container.remove(key_is_claimed);
                            container.remove(key_get_faction);
                            container.remove(key_get_creator);
                            container.remove(key_get_faction_name);
                            player.sendMessage(ChatColor.GREEN + "Vous avez supprimer ce claim de la protection du spawn.");
                            ObjectsPreset.chunks_zone_safe.remove(player.getWorld().getChunkAt(player.getLocation()));
                            return true;
                        }
                        player.sendMessage(ChatColor.RED + "Ce Claim n'est pas dans le spawn et appartient à un joueur !");
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("clear")) {
                        player.sendMessage(ChatColor.GOLD + "Retrait de tout les claims de la zone safe...");
                        if (ObjectsPreset.chunks_zone_safe.size() != 0) {
                            for (int i = 0; i < ObjectsPreset.chunks_zone_safe.size(); i++) {
                                container = ObjectsPreset.chunks_zone_safe.get(i).getPersistentDataContainer();
                                if (container.has(key_is_claimed, PersistentDataType.INTEGER))
                                    container.remove(key_is_claimed);
                                if (container.has(key_get_faction, PersistentDataType.STRING))
                                    container.remove(key_get_faction);
                                if (container.has(key_get_creator, PersistentDataType.STRING))
                                    container.remove(key_get_creator);
                                if (container.has(key_get_faction_name, PersistentDataType.STRING))
                                    container.remove(key_get_faction_name);
                            }
                            ObjectsPreset.chunks_zone_safe.clear();
                        }
                        player.sendMessage(ChatColor.RED + "Vous avez retirer tout les claims de la zone safe !");
                        return true;
                    }
                    player.sendMessage(ChatColor.RED + "Mettez de bonnes informations. " + ChatColor.GRAY + "(/setspawn <claim/unclaim/clear>)");
                }
            }
        }
        return false;
    }
}
