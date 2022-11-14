package fr.lightnew.tools;

import fr.lightnew.MainFac;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.WeakHashMap;

public class Cooldown {

    public static WeakHashMap<Player, Long> cooldownList = new WeakHashMap<>();

    public static boolean create(Player player, int time) {
        if (cooldownList.containsKey(player)) {
            long timeSinceCreation = System.currentTimeMillis() - cooldownList.get(player);
            float timeLeft = time - (timeSinceCreation / 1000f);
            float secondsLeft = ((int) (timeLeft * 10)) / 10;

            player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.RED + "Il vous reste encore " + secondsLeft + " secondes avant de refaire la commande !");
            return false;
        }
        if (!cooldownList.containsKey(player))
            cooldownList.put(player, System.currentTimeMillis());

        new BukkitRunnable() {

            @Override
            public void run() {
                if (!player.isOnline())
                    cancel();
                if (!cooldownList.containsKey(player))
                    cancel();

                long timeSinceCreation = System.currentTimeMillis() - cooldownList.get(player);
                float timeLeft = 5 - (timeSinceCreation / 1000f);
                float secondsLeft = ((int) (timeLeft * 10)) / 10;

                if (secondsLeft <= 0) {
                    cooldownList.remove(player);
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(MainFac.instance, 0, 20);
        return true;
    }
}
