package fr.lightnew.listeners;

import fr.lightnew.MainFac;
import fr.lightnew.faction.Faction;
import fr.lightnew.faction.Ranks;
import fr.lightnew.faction.SettingsFaction;
import fr.lightnew.tools.ObjectsPreset;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteract implements Listener {

    public Faction getFaction(Player player) {return MainFac.factions.get(player);}

    @EventHandler
    public void onInteract(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        String title = event.getView().getTitle();

        if (item == null)
            return;

        if (title.equalsIgnoreCase(SettingsFaction.getTitle(player))) {
            if (getFaction(player).getPlayerList().get(player).equals(Ranks.CHEF) || getFaction(player).getPlayerList().get(player).equals(Ranks.ADJOINT)) {
                if (item.isSimilar(SettingsFaction.PLAYERS))
                    SettingsFaction.sendGuiPlayers(player);
                if (item.isSimilar(SettingsFaction.CLAIMS))
                    player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.RED + "Pas disponible pour le moment");
                if (item.isSimilar(SettingsFaction.BACK))
                    SettingsFaction.sendGuiSettings(player);
            } else
                player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.YELLOW + "Vous devez être Adjoint ou Chef de la faction !");
            event.setCancelled(true);
        }
    }

}
