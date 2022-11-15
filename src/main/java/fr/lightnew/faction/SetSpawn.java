package fr.lightnew.faction;

import fr.lightnew.MainFac;
import fr.lightnew.tools.ObjectsPreset;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;

public class SetSpawn implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.isOp()) {
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
        }
        return false;
    }
}
