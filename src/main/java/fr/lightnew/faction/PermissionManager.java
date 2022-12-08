package fr.lightnew.faction;

import fr.lightnew.tools.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PermissionManager {

    public static ItemStack SETTINGS = ItemBuilder.create(Material.FLINT, 1, PermissionsRanks.SETTINGS.name.toUpperCase(), PermissionsRanks.SETTINGS.description);
    public static ItemStack DISBAND = ItemBuilder.create(Material.BARRIER, 1, PermissionsRanks.DISBAND.name.toUpperCase(), PermissionsRanks.DISBAND.description);
    public static ItemStack RENAME = ItemBuilder.create(Material.NAME_TAG, 1, PermissionsRanks.RENAME.name.toUpperCase(), PermissionsRanks.RENAME.description);
    public static ItemStack DESCRIPTION = ItemBuilder.create(Material.WRITABLE_BOOK, 1, PermissionsRanks.DESCRIPTION.name.toUpperCase(), PermissionsRanks.DESCRIPTION.description);
    public static ItemStack INVITE = ItemBuilder.create(Material.REDSTONE_TORCH, 1, PermissionsRanks.INVITE.name.toUpperCase(), PermissionsRanks.INVITE.description);
    public static ItemStack UNINVITE = ItemBuilder.create(Material.SOUL_TORCH, 1, PermissionsRanks.UNINVITE.name.toUpperCase(), PermissionsRanks.UNINVITE.description);
    public static ItemStack CLAIMSEE = ItemBuilder.create(Material.ENDER_EYE, 1, PermissionsRanks.CLAIMSEE.name.toUpperCase(), PermissionsRanks.CLAIMSEE.description);
    public static ItemStack CLAIM = ItemBuilder.create(Material.LIME_DYE, 1, PermissionsRanks.CLAIM.name.toUpperCase(), PermissionsRanks.CLAIM.description);
    public static ItemStack UNCLAIM = ItemBuilder.create(Material.GRAY_DYE, 1, PermissionsRanks.UNCLAIM.name.toUpperCase(), PermissionsRanks.UNCLAIM.description);
    public static ItemStack SET_HOME = ItemBuilder.create(Material.CAMPFIRE, 1, PermissionsRanks.SET_HOME.name.toUpperCase(), PermissionsRanks.SETTINGS.description);
    public static ItemStack HOME = ItemBuilder.create(Material.SOUL_CAMPFIRE, 1, PermissionsRanks.HOME.name.toUpperCase(), PermissionsRanks.HOME.description);
    public static ItemStack CREATE_RANK = ItemBuilder.create(Material.PAPER, 1, PermissionsRanks.CREATE_RANK.name.toUpperCase(), PermissionsRanks.CREATE_RANK.description);
    public static ItemStack SET_RANK = ItemBuilder.create(Material.BOOK, 1, PermissionsRanks.SET_RANK.name.toUpperCase(), PermissionsRanks.SET_RANK.description);
    public static ItemStack ADD_ALLY = ItemBuilder.create(Material.BLUE_WOOL, 1, PermissionsRanks.ADD_ALLY.name.toUpperCase(), PermissionsRanks.ADD_ALLY.description);
    public static ItemStack ADD_ENEMY = ItemBuilder.create(Material.RED_WOOL, 1, PermissionsRanks.ADD_ENEMY.name.toUpperCase(), PermissionsRanks.ADD_ENEMY.description);
    public static ItemStack KICK = ItemBuilder.create(Material.TNT, 1, PermissionsRanks.KICK.name.toUpperCase(), PermissionsRanks.KICK.description);
    public static ItemStack UPGRADE = ItemBuilder.create(Material.GOLDEN_APPLE, 1, PermissionsRanks.UPGRADE.name.toUpperCase(), PermissionsRanks.UPGRADE.description);
    public static ItemStack PROMOTE = ItemBuilder.create(Material.SLIME_BALL, 1, PermissionsRanks.PROMOTE.name.toUpperCase().toUpperCase(), PermissionsRanks.PROMOTE.description);
    public static ItemStack GLASS_PANE = ItemBuilder.create(Material.RED_STAINED_GLASS_PANE, 1, "");

    public static String nameInventory = ChatColor.YELLOW + "Réglage Permissions";
    public static void sendGui(Player player, String nameRank) {
        Inventory inventory = Bukkit.createInventory(player, 6*9, nameInventory);

        int[] slots_glass = new int[]{0,1,7,8,9,17,36,44,45,46,52,53};
        int[] slots_items = new int[]{4,11,12,13,14,15,20,21,22,23,24,29,30,31,32,33,39,41};
        for (int slots : slots_glass)
            inventory.setItem(slots, GLASS_PANE);

        ItemStack[] items = new ItemStack[]{SETTINGS, DISBAND, RENAME, DESCRIPTION, INVITE, UNINVITE, CLAIMSEE, CLAIM, UNCLAIM, SET_HOME, HOME, CREATE_RANK, SET_RANK, ADD_ALLY, ADD_ENEMY, KICK, UPGRADE, PROMOTE};
        for (int i = 0; i < slots_items.length; i++)
            inventory.setItem(slots_items[i], items[i]);

        FacCommands.playersInPermInventory.put(player, nameRank);
        player.openInventory(inventory);
    }

    private static Boolean isActive(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        return (meta.getEnchants().size() > 0);
    }

    public static PermissionManager setPermissionsWithInventory(ItemStack[] items) {
        PermissionManager manager = new PermissionManager();
        for (ItemStack item : items) {
            if (item != null) {
                if (item.getType().equals(SETTINGS.getType()))
                    if (isActive(item))
                        manager.setSettings(true);
                if (item.getType().equals(DISBAND.getType()))
                    if (isActive(item))
                        manager.setDisband(true);
                if (item.getType().equals(RENAME.getType()))
                    if (isActive(item))
                        manager.setRename(true);
                if (item.getType().equals(DESCRIPTION.getType()))
                    if (isActive(item))
                        manager.setDescription(true);
                if (item.getType().equals(INVITE.getType()))
                    if (isActive(item))
                        manager.setInvite(true);
                if (item.getType().equals(UNINVITE.getType()))
                    if (isActive(item))
                        manager.setUninvite(true);
                if (item.getType().equals(CLAIMSEE.getType()))
                    if (isActive(item))
                        manager.setClaimsee(true);
                if (item.getType().equals(CLAIM.getType()))
                    if (isActive(item))
                        manager.setClaim(true);
                if (item.getType().equals(UNCLAIM.getType()))
                    if (isActive(item))
                        manager.setUnclaim(true);
                if (item.getType().equals(SET_HOME.getType()))
                    if (isActive(item))
                        manager.setSet_home(true);
                if (item.getType().equals(HOME.getType()))
                    if (isActive(item))
                        manager.setHome(true);
                if (item.getType().equals(CREATE_RANK.getType()))
                    if (isActive(item))
                        manager.setCreate_rank(true);
                if (item.getType().equals(SET_RANK.getType()))
                    if (isActive(item))
                        manager.setSet_rank(true);
                if (item.getType().equals(ADD_ALLY.getType()))
                    if (isActive(item))
                        manager.setAdd_ally(true);
                if (item.getType().equals(ADD_ENEMY.getType()))
                    if (isActive(item))
                        manager.setAdd_enemy(true);
                if (item.getType().equals(KICK.getType()))
                    if (isActive(item))
                        manager.setKick(true);
                if (item.getType().equals(UPGRADE.getType()))
                    if (isActive(item))
                        manager.setUpgrade(true);
                if (item.getType().equals(PROMOTE.getType()))
                    if (isActive(item))
                        manager.setPromote(true);
            }
        }
        return manager;
    }

    private Boolean settings;
    private Boolean disband;
    private Boolean rename;
    private Boolean description;
    private Boolean invite;
    private Boolean uninvite;
    private Boolean claimsee;
    private Boolean claim;
    private Boolean unclaim;
    private Boolean set_home;
    private Boolean home;
    private Boolean create_rank;
    private Boolean set_rank;
    private Boolean add_ally;
    private Boolean add_enemy;
    private Boolean kick;
    private Boolean upgrade;
    private Boolean promote;

    public PermissionManager() {
        this.settings = false;
        this.disband = false;
        this.rename = false;
        this.description = false;
        this.invite = false;
        this.uninvite = false;
        this.claimsee = false;
        this.claim = false;
        this.unclaim = false;
        this.set_home = false;
        this.home = false;
        this.create_rank = false;
        this.set_rank = false;
        this.add_ally = false;
        this.add_enemy = false;
        this.kick = false;
        this.upgrade = false;
        this.promote = false;
    }

    @Override
    public String toString() {
        return  "§e\nSettings : §6" + this.settings+
                "§e\nDisband : §6" + this.disband+
                "§e\nRename : §6" + this.rename+
                "§e\nDescription : §6" + this.description+
                "§e\nInvite : §6" + this.invite+
                "§e\nUn-Invite : §6" + this.uninvite+
                "§e\nClaim-see : §6" + this.claimsee+
                "§e\nClaim : §6" + this.claim+
                "§e\nUn-Claim : §6" + this.unclaim+
                "§e\nSet-Home : §6" + this.set_home+
                "§e\nHome : §6" + this.home+
                "§e\nCreate-Rank : §6" + this.create_rank+
                "§e\nSet-Rank : §6" + this.set_rank+
                "§e\nAdd-Ally : §6" + this.add_ally+
                "§e\nAdd-Enemy : §6" + this.add_enemy+
                "§e\nKick : §6" + this.kick+
                "§e\nUpgrade : §6" + this.upgrade+
                "§e\nPromote : §6" + this.promote;
    }

    /*GETTER*/
    public Boolean getSettings() {
        return settings;
    }

    public Boolean getDisband() {
        return disband;
    }

    public Boolean getRename() {
        return rename;
    }

    public Boolean getDescription() {
        return description;
    }

    public Boolean getInvite() {
        return invite;
    }

    public Boolean getUninvite() {
        return uninvite;
    }

    public Boolean getClaimsee() {
        return claimsee;
    }

    public Boolean getClaim() {
        return claim;
    }

    public Boolean getUnclaim() {
        return unclaim;
    }

    public Boolean getSet_home() {
        return set_home;
    }

    public Boolean getHome() {
        return home;
    }

    public Boolean getCreate_rank() {
        return create_rank;
    }

    public Boolean getSet_rank() {
        return set_rank;
    }

    public Boolean getAdd_ally() {
        return add_ally;
    }

    public Boolean getAdd_enemy() {
        return add_enemy;
    }

    public Boolean getKick() {
        return kick;
    }

    public Boolean getUpgrade() {
        return upgrade;
    }

    public Boolean getPromote() {
        return promote;
    }

    /*SETTER*/
    public void setAllOFF() {
        this.settings = false;
        this.disband = false;
        this.rename = false;
        this.description = false;
        this.invite = false;
        this.uninvite = false;
        this.claimsee = false;
        this.claim = false;
        this.unclaim = false;
        this.set_home = false;
        this.home = false;
        this.create_rank = false;
        this.set_rank = false;
        this.add_ally = false;
        this.add_enemy = false;
        this.kick = false;
        this.upgrade = false;
        this.promote = false;
    }

    public void setAllON() {
        this.settings = true;
        this.disband = true;
        this.rename = true;
        this.description = true;
        this.invite = true;
        this.uninvite = true;
        this.claimsee = true;
        this.claim = true;
        this.unclaim = true;
        this.set_home = true;
        this.home = true;
        this.create_rank = true;
        this.set_rank = true;
        this.add_ally = true;
        this.add_enemy = true;
        this.kick = true;
        this.upgrade = true;
        this.promote = true;
    }

    public void setSettings(Boolean settings) {
        this.settings = settings;
    }

    public void setDisband(Boolean disband) {
        this.disband = disband;
    }

    public void setRename(Boolean rename) {
        this.rename = rename;
    }

    public void setDescription(Boolean description) {
        this.description = description;
    }

    public void setInvite(Boolean invite) {
        this.invite = invite;
    }

    public void setUninvite(Boolean uninvite) {
        this.uninvite = uninvite;
    }

    public void setClaimsee(Boolean claimsee) {
        this.claimsee = claimsee;
    }

    public void setClaim(Boolean claim) {
        this.claim = claim;
    }

    public void setUnclaim(Boolean unclaim) {
        this.unclaim = unclaim;
    }

    public void setSet_home(Boolean set_home) {
        this.set_home = set_home;
    }

    public void setHome(Boolean home) {
        this.home = home;
    }

    public void setCreate_rank(Boolean create_rank) {
        this.create_rank = create_rank;
    }

    public void setSet_rank(Boolean set_rank) {
        this.set_rank = set_rank;
    }

    public void setAdd_ally(Boolean add_ally) {
        this.add_ally = add_ally;
    }

    public void setAdd_enemy(Boolean add_enemy) {
        this.add_enemy = add_enemy;
    }

    public void setKick(Boolean kick) {
        this.kick = kick;
    }

    public void setUpgrade(Boolean upgrade) {
        this.upgrade = upgrade;
    }

    public void setPromote(Boolean promote) {
        this.promote = promote;
    }
}
