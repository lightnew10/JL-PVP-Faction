package fr.lightnew.faction;

import fr.lightnew.MainFac;
import fr.lightnew.tools.ObjectsPreset;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.WeakHashMap;

public class Spawn implements CommandExecutor {

    public WeakHashMap<Player, Location> waitTeleport = new WeakHashMap<>();
    public static Location spawnLocation;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                if (spawnLocation == null) {
                    player.sendMessage(ChatColor.RED + "Le point de spawn à pas encore été placé.");
                    return true;
                }
                if (!waitTeleport.containsKey(player)) {
                    teleportSpawn(player);
                    return true;
                } else
                    player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.RED + "Vous êtes déjà en attente de téléportation");
            }
        }
        return false;
    }

    public void teleportSpawn(Player player) {
        waitTeleport.put(player, player.getLocation());
        BukkitTask task = new BukkitRunnable() {
            int result = 5;

            @Override
            public void run() {
                if (!player.isOnline())
                    cancel();
                if (!waitTeleport.containsKey(player))
                    cancel();
                if (player.getLocation().getX() != waitTeleport.get(player).getX() || player.getLocation().getY() != waitTeleport.get(player).getY() || player.getLocation().getZ() != waitTeleport.get(player).getZ()) {
                    player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.RED + "Téléportation annulé, vous avez bougé !");
                    waitTeleport.remove(player);
                    cancel();
                }

                if (result < 6)
                    player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.YELLOW + "Vous allez être téléporter dans " + ChatColor.GOLD + result + " secondes");

                if (result == 0) {
                    player.teleport(spawnLocation);
                    player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.YELLOW + "Vous avez été téléporter au spawn");
                    waitTeleport.remove(player);
                    cancel();
                }
                result--;
            }
        }.runTaskTimer(MainFac.instance, 0, 20);
    }
}
