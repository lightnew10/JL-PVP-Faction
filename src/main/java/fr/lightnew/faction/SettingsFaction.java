package fr.lightnew.faction;

import fr.lightnew.MainFac;
import fr.lightnew.tools.ItemBuilder;
import fr.lightnew.tools.ObjectsPreset;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class SettingsFaction {

    public static ItemStack PLAYERS = ItemBuilder.create(Material.SKELETON_SKULL, 1, ChatColor.YELLOW + "Tous les membres", "", ChatColor.GRAY + "§nClic gauche pour :", ChatColor.GRAY + "Voir tous les membres de la faction avec leurs grades");
    public static ItemStack BACK = ItemBuilder.create(Material.ARROW, 1, ChatColor.RED + "Retour");
    public static ItemStack CLAIMS = ItemBuilder.create(Material.IRON_BLOCK, 1, ChatColor.YELLOW + "Voir tous les claims de la faction", "", ChatColor.RED + "Pas disponible pour le moment"/*ChatColor.GRAY + "§nClic gauche pour ouvrir"*/);
    public static ItemStack GLASS = ItemBuilder.create(Material.RED_STAINED_GLASS_PANE, 1, "");
    private static int[] slots_glass = new int[]{0, 1, 7, 8, 9, 17, 27, 36, 37, 35, 43, 44};
    private static int[] slots_players = new int[] {12,13,14,20,21,22,23,24};
    private static List<Integer> slots_available_players = new ArrayList<>();

    public static String getTitle(Player player) {
        Faction faction = MainFac.getFactions().get(player);
        if (faction == null)
            return ChatColor.RED+ "Pas de faction";
        return ChatColor.YELLOW + "Settings " + faction.getName() + ChatColor.GRAY + " (" + faction.getSlots() + "/" + faction.getMaxSlot() + ")";
    }

    public static void sendGuiSettings(Player player) {
        Faction faction = MainFac.getFactions().get(player);
        ItemStack POWER = ItemBuilder.create(Material.REDSTONE, faction.getPower(), ChatColor.RED + "POWER " + faction.getPower(), ChatColor.GRAY + "Vous voulez avoir du power ? ", ChatColor.GRAY + "§nBah arrête de mourir");
        ItemStack LVL = ItemBuilder.create(Material.EMERALD, 1, ChatColor.GREEN + "Niveau " + faction.getLevel(), ChatColor.GRAY + ObjectsPreset.how_get_upgrade_faction);
        ItemStack OWNER = ItemBuilder.skull(1, ChatColor.RED + "Chef de la faction", faction.getOwner().getName(), "", ChatColor.GRAY + "Nom du chef : " + faction.getOwner().getName(), "\n");
        ItemStack DESCRIPTION = ItemBuilder.create(Material.OAK_SIGN, 1, ChatColor.YELLOW + "§l§nVotre Description:", "", ChatColor.GRAY + faction.getDescription(), "\n");

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

    public static void sendGuiPlayers(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 5*9, getTitle(player));
        Faction faction = MainFac.getFactions().get(player);
        slots_available_players.clear();
        for (int slot : slots_glass)
            inventory.setItem(slot, GLASS);
        for (int i : slots_players)
            slots_available_players.add(i);

        for (Player players : faction.getPlayerList().keySet()) {
            UserData data = MainFac.instance.playersCache.get(player);
            String statut;
            if (players.isOnline())
                statut = ChatColor.GREEN + "■";
            else statut = ChatColor.RED + "■";
            ItemStack skull = ItemBuilder.skull(1, "§r§l§e"+ players.getName(), players.getName(), "", ChatColor.GOLD +  "§nRank§r§e : " + data.getRanks(), "", ChatColor.RED + "§nPower§r§e : " + ChatColor.GRAY + data.getPower(), "", ChatColor.GRAY + "§nStatut§e : " + statut);
            inventory.setItem(slots_available_players.get(0), skull);
            slots_available_players.remove(0);
        }
        inventory.setItem(36, BACK);
        player.openInventory(inventory);
    }
}
