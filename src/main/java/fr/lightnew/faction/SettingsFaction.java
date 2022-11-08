package fr.lightnew.faction;

import fr.lightnew.MainFac;
import fr.lightnew.tools.ItemBuilder;
import fr.lightnew.tools.ObjectsPreset;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SettingsFaction {

    public static ItemStack DESCRIPTION = ItemBuilder.create(Material.OAK_SIGN, 1, ChatColor.YELLOW + "Description", ChatColor.GRAY + "Vous permet de changer la description");
    public static ItemStack PLAYERS = ItemBuilder.create(Material.SKELETON_SKULL, 1, ChatColor.YELLOW + "Tous les membres", ChatColor.GRAY + "Voir tous les membres de la faction avec leurs grades");
    public static ItemStack CLAIMS = ItemBuilder.create(Material.IRON_BLOCK, 1, ChatColor.YELLOW + "Voir tous les claims de la faction");
    public static ItemStack GLASS = ItemBuilder.create(Material.RED_STAINED_GLASS_PANE, 1, "");
    private static int[] slots_glass = new int[]{0, 1, 7, 8, 9, 17, 27, 36, 37, 35, 43, 44};

    public static String getTitle(Player player) {
        Faction faction = MainFac.getFactions().get(player);
        if (faction == null)
            return ChatColor.RED+ "Pas de faction";
        return ChatColor.YELLOW + "Settings " + faction.getName() + ChatColor.GRAY + "(" + faction.getSlots() + "/" + faction.getMaxSlot() + ")";
    }

    public static void sendGuiSettings(Player player) {
        Faction faction = MainFac.getFactions().get(player);
        ItemStack POWER = ItemBuilder.create(Material.REDSTONE, 1, ChatColor.RED + "POWER " + faction.getPower(), ChatColor.GRAY + "Vous voulez avoir du power ? Bah arrête de mourir");
        ItemStack LVL = ItemBuilder.create(Material.EMERALD, 1, ChatColor.GREEN + "Niveau " +faction.getLevel(), ChatColor.GRAY + ObjectsPreset.how_get_upgrade_faction);
        ItemStack OWNER = ItemBuilder.skull(1, ChatColor.RED + "Chef de la faction", faction.getOwner().getName(), ChatColor.GRAY + "Chef : " + faction.getOwner().getName());

        Inventory inventory = Bukkit.createInventory(player, 5*9, getTitle(player));
        for (int slot : slots_glass)
            inventory.setItem(slot, GLASS);
        inventory.setItem(12, DESCRIPTION);
        inventory.setItem(32, PLAYERS);
        inventory.setItem(13, POWER);
        inventory.setItem(4, OWNER);
        inventory.setItem(14, LVL);
        inventory.setItem(30, CLAIMS);
        player.openInventory(inventory);
    }
}
