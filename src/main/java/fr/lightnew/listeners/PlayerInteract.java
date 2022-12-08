package fr.lightnew.listeners;

import fr.lightnew.MainFac;
import fr.lightnew.faction.*;
import fr.lightnew.tools.ObjectsPreset;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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

        if (title.equalsIgnoreCase(PermissionManager.nameInventory)) {
            if (item.isSimilar(PermissionManager.GLASS_PANE) || item.isSimilar(new ItemStack(Material.AIR))) {
                event.setCancelled(true);
                return;
            }
            ItemMeta meta = item.getItemMeta();
            if (meta.getEnchants().size() == 0) {
                meta.addEnchant(Enchantment.DURABILITY, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            } else
                meta.removeEnchant(Enchantment.DURABILITY);
            item.setItemMeta(meta);
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onCloseInv(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if (FacCommands.playersInPermInventory.containsKey(player)) {
            if (event.getView().getTitle().equalsIgnoreCase(PermissionManager.nameInventory)) {
                Faction faction = FacCommands.getFaction(player);
                RankManager manager = null;
                for (RankManager ranks : faction.getRanks())
                    if (ranks.getName().equalsIgnoreCase(FacCommands.playersInPermInventory.get(player)))
                        manager = ranks;
                if (manager != null) {
                    manager.setPermissions(PermissionManager.setPermissionsWithInventory(event.getInventory().getContents()));
                    faction.changePermissionRank(manager);
                    player.sendMessage(ChatColor.YELLOW + "Mise à jours des permissions de " + ChatColor.GOLD + FacCommands.playersInPermInventory.get(player) + ".");
                }
            }
        }
        FacCommands.playersInPermInventory.remove(player);
    }

}