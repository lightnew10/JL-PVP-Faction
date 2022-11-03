package fr.lightnew.faction;

import fr.lightnew.MainFac;
import fr.lightnew.tools.MessagesPreset;
import fr.lightnew.tools.Perms;
import fr.lightnew.tools.PlayersCache;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FacCommands implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ClaimsManager claimsManager = new ClaimsManager(player);

            if (args.length == 0) {player.sendMessage(MessagesPreset.help_faction_page_1);}
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("claim")) {
                    PlayersCache cache = MainFac.instance.listPlayerCache.get(player);
                    if (cache.getFactionID() <= 0) {
                        player.sendMessage(MessagesPreset.prefix_fac + ChatColor.RED + "Vous n'êtes pas dans une faction !");
                        return true;
                    }

                    Chunk chunk = player.getWorld().getChunkAt(player.getLocation());
                    if (claimsManager.setClaimedChunk(chunk)) {
                        player.sendMessage(MessagesPreset.chunk_is_available + ChatColor.GRAY + " (X:" + player.getLocation().getX() + " Y:" + player.getLocation().getY() + " Z: " + player.getLocation().getZ() +")");
                    } else
                        player.sendMessage(MessagesPreset.chunk_is_not_available);
                    return true;
                }
                if (args[0].equalsIgnoreCase("unclaim")) {
                    Chunk chunk = player.getWorld().getChunkAt(player.getLocation());
                    if (claimsManager.removeClaimedChunk(chunk)) {
                        player.sendMessage(MessagesPreset.claim_remove);
                    } else
                        player.sendMessage(MessagesPreset.claim_not_remove);
                    return true;
                }
                if (args[0].equalsIgnoreCase("help")) {
                    player.sendMessage(MessagesPreset.help_faction_page_1);
                    return true;
                }
                if (args[0].equalsIgnoreCase("f") || args[0].equalsIgnoreCase("faction") || args[0].equalsIgnoreCase("info")) {
                    player.sendMessage(MessagesPreset.information_faction(player));
                    return true;
                }
                player.sendMessage(MessagesPreset.prefix_fac + ChatColor.RED + "Il vous manque des informations -> " + ChatColor.GRAY + "/" + s + " " + args[0] + " <element>");
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("help")) {
                    if (isNumeric(args[1])) {
                        if (Integer.parseInt(args[1]) >= 1 || Integer.parseInt(args[1]) <= 3) {
                            if (Integer.parseInt(args[1]) == 1)
                                player.sendMessage(MessagesPreset.help_faction_page_1);
                            if (Integer.parseInt(args[1]) == 2)
                                player.sendMessage(MessagesPreset.help_faction_page_2);
                            if (Integer.parseInt(args[1]) == 3)
                                player.sendMessage(MessagesPreset.help_faction_page_3);
                        } else player.sendMessage(MessagesPreset.prefix_fac + ChatColor.RED + "Mettez un chiffre entre 1 à 3.");
                    } else player.sendMessage(MessagesPreset.error_numeric);
                    return true;
                }
                if (args[0].equalsIgnoreCase("create")) {
                    if (args[1].length() < 3) {
                        player.sendMessage(MessagesPreset.prefix_fac + ChatColor.RED + "Mettez au minimum 4 caractères !");
                        return true;
                    }
                    if (MainFac.instance.listNameFaction.contains(args[1])) {
                        player.sendMessage(MessagesPreset.prefix_fac + ChatColor.RED + "Ce nom existe déjà.");
                        return true;
                    }
                    if (MainFac.instance.banWordNameFaction.contains(args[1])) {
                        if (!player.hasPermission(Perms.createFac)) {
                            player.sendMessage(MessagesPreset.prefix_fac + ChatColor.RED + "Vous avez pas le droit d'utiliser ce nom !");
                            return true;
                        }
                    }
                    if (!StringUtils.isAlphanumeric(args[1])) {
                        player.sendMessage(MessagesPreset.prefix_fac + ChatColor.RED + "Vous devez mettre un nom correct !");
                        return true;
                    }
                    player.sendMessage(ChatColor.YELLOW + "Vous venez de créer votre faction ! " + ChatColor.GOLD + args[1] + ChatColor.GRAY + "\n(Si vous avez un nom non adapté vous pouvez être bannis définitivement !)");
                    new Faction(player, args[1], "Déscription par défaut");
                    MainFac.instance.listNameFaction.add(args[1]);
                    return true;
                }
            }
            if (args.length > 1) {
                if (args[0].equalsIgnoreCase("description")) {
                    StringBuilder sb = new StringBuilder();
                    for(int i = 1; i < args.length; i++)
                        sb.append(args[i]).append(" ");

                    if (MainFac.instance.listPlayerCache.get(player).getFactionID() != 0) {
                        Faction faction = new Faction(MainFac.instance.listPlayerCache.get(player).getFactionID());
                        faction.setDescription(sb.toString());
                        faction.sendModifications();
                        player.sendMessage(ChatColor.YELLOW + "Vous venez de modifier votre description par " + ChatColor.GREEN + sb);
                    } else
                        player.sendMessage(MessagesPreset.prefix_fac + ChatColor.RED + "Vous ne possédez pas de faction !");

                    return true;
                }
                player.sendMessage(MessagesPreset.prefix_fac + ChatColor.RED + "Mettez de bonnes informations ! " + ChatColor.GRAY + "</" + s + " " + args[0] + " " + args[1] + ">");
            }
        }
        return false;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        List<String> players = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers())
            players.add(p.getName());

        if (args.length == 1)
            return Arrays.asList("help", "settings", "create", "disband", "join", "quit", "map", "mapsize", "info", "playerinfo", "rename", "description", "invite", "uninvite", "claimsee", "claim", "unclaim", "sethome", "home", "ranks", "setrank", "setchef", "ally", "enemy", "neutre", "kick", "upgrade");

        if (args[0].equalsIgnoreCase("help"))
            return Arrays.asList("1", "2", "3");

        if (args[0].equalsIgnoreCase("playerinfo") || args[0].equalsIgnoreCase("invite") || args[0].equalsIgnoreCase("uninvite") || args[0].equalsIgnoreCase("setrank") || args[0].equalsIgnoreCase("setchef"))
            return players;

        return null;
    }
}
