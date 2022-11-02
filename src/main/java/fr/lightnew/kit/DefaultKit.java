package fr.lightnew.kit;

import fr.lightnew.tools.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DefaultKit {
    public static ItemStack HELMET = ItemBuilder.create(Material.IRON_HELMET, 1, ChatColor.GRAY + "Kit de défaut");
    public static ItemStack CHESTPLATE = ItemBuilder.create(Material.IRON_CHESTPLATE, 1, ChatColor.GRAY + "Kit de défaut");
    public static ItemStack LEGGINGS = ItemBuilder.create(Material.IRON_LEGGINGS, 1, ChatColor.GRAY + "Kit de défaut");
    public static ItemStack BOOTS = ItemBuilder.create(Material.IRON_BOOTS, 1, ChatColor.GRAY + "Kit de défaut");
    public static ItemStack PICKAXE = ItemBuilder.create(Material.IRON_PICKAXE, 1, ChatColor.GRAY + "Kit de défaut");
    public static ItemStack SWORD = ItemBuilder.create(Material.IRON_SWORD, 1, ChatColor.GRAY + "Kit de défaut");

    public static void send(Player player) {
        player.getInventory().addItem(SWORD,PICKAXE,HELMET,CHESTPLATE,LEGGINGS,BOOTS);
    }

}
