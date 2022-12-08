package fr.lightnew.faction;

import fr.lightnew.MainFac;
import fr.lightnew.api.EconomyAPI;
import fr.lightnew.tools.ClickMSG;
import fr.lightnew.tools.Cooldown;
import fr.lightnew.tools.ObjectsPreset;
import fr.lightnew.tools.Perms;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class FacCommands implements CommandExecutor, TabCompleter {

    public WeakHashMap<Player, Location> teleportHome = new WeakHashMap<>();
    public static WeakHashMap<Player, Faction> notificationInvite = new WeakHashMap<>();
    public static Boolean getInFaction(Player player) {return MainFac.getFactions().containsKey(player);}
    public static Faction getFaction(Player player) {return MainFac.factions.get(player);}
    public static WeakHashMap<Player, String> playersInPermInventory = new WeakHashMap<>();
    private EconomyAPI eco = new EconomyAPI();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ClaimsManager claimsManager = new ClaimsManager(getFaction(player), player);

            if (args.length == 0) {player.sendMessage(ObjectsPreset.help_faction_page_1);}

            if (args.length == 1) {
                if (!getInFaction(player)) {
                    player.sendMessage(ObjectsPreset.your_are_not_in_faction);
                    return true;
                }
                if (args[0].equalsIgnoreCase("claim")) {
                    if (!Cooldown.create(player, 3))
                        return true;
                    if (!getInFaction(player)) {
                        player.sendMessage(ObjectsPreset.your_are_not_in_faction);
                        return true;
                    }

                    Chunk chunk = player.getWorld().getChunkAt(player.getLocation());
                    if (claimsManager.setClaimedChunk(chunk)) {
                        player.sendMessage(ObjectsPreset.chunk_is_available + ChatColor.GRAY + " (X:" + player.getLocation().getX() + " Y:" + player.getLocation().getY() + " Z: " + player.getLocation().getZ() +")");
                    } else
                        player.sendMessage(ObjectsPreset.chunk_is_not_available);
                    return true;
                }
                if (args[0].equalsIgnoreCase("unclaim")) {
                    if (!Cooldown.create(player, 3))
                        return true;
                    if (!getInFaction(player)) {
                        player.sendMessage(ObjectsPreset.your_are_not_in_faction);
                        return true;
                    }
                    Chunk chunk = player.getWorld().getChunkAt(player.getLocation());
                    if (claimsManager.chunkHasClaimedByYourFaction(chunk, getFaction(player))) {
                        claimsManager.removeClaimedChunk(chunk);
                        player.sendMessage(ObjectsPreset.claim_remove);
                        return true;
                    } else
                        player.sendMessage(ObjectsPreset.claim_not_remove);
                    return true;
                }
                //only op player
                if (args[0].equalsIgnoreCase("aunclaim")) {
                    if (player.isOp()) {
                        claimsManager.removeClaimedChunk(player.getWorld().getChunkAt(player.getLocation()));
                        player.sendMessage(ObjectsPreset.claim_remove);
                        return true;
                    } else
                        player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.RED + "Vous ne pouvez pas faire ceci !");
                    return true;
                }
                if (args[0].equalsIgnoreCase("help")) {
                    if (!Cooldown.create(player, 3))
                        return true;
                    player.sendMessage(ObjectsPreset.help_faction_page_1);
                    return true;
                }
                if (args[0].equalsIgnoreCase("settings")) {
                    if (!Cooldown.create(player, 6))
                        return true;
                    if (!getInFaction(player)) {
                        player.sendMessage(ObjectsPreset.your_are_not_in_faction);
                        return true;
                    }
                    SettingsFaction.sendGuiSettings(player);
                    return true;
                }
                if (args[0].equalsIgnoreCase("f") || args[0].equalsIgnoreCase("faction") || args[0].equalsIgnoreCase("info")) {
                    if (!Cooldown.create(player, 6))
                        return true;
                    player.sendMessage(ObjectsPreset.information_faction(player));
                    return true;
                }
                if (args[0].equalsIgnoreCase("disband")) {
                    if (!getInFaction(player)) {
                        player.sendMessage(ObjectsPreset.your_are_not_in_faction);
                        return true;
                    }
                    if (!getFaction(player).getPlayerList().get(player).equals(Ranks.CHEF.toString())) {
                        player.sendMessage(ObjectsPreset.your_are_not_owner);
                        return true;
                    }
                    for (Player players : getFaction(player).getPlayerList().keySet())
                        if (players.isOnline())
                            players.sendMessage(ChatColor.GRAY + "\n§m§l------------------------------------\n" +
                                    ObjectsPreset.prefix_fac + ChatColor.GOLD + "Votre faction vient d'être supprimer !" +
                                    ChatColor.GRAY + "\n§m§l------------------------------------");
                    try {getFaction(player).remove();} catch (InterruptedException e) {throw new RuntimeException(e);}
                    return true;
                }
                if (args[0].equalsIgnoreCase("quit")) {
                    if (!getInFaction(player)) {
                        player.sendMessage(ObjectsPreset.your_are_not_in_faction);
                        return true;
                    }
                    if (getFaction(player).getPlayerList().get(player).equals(Ranks.CHEF.toString())) {
                        player.sendMessage(ObjectsPreset.your_are_owner_leave);
                        return true;
                    }
                    for (Player players : getFaction(player).getPlayerList().keySet())
                        if (players.isOnline())
                            players.sendMessage(/*method for player*/ObjectsPreset.player_entrer_faction(player));
                    getFaction(player).getPlayerList().remove(player);
                    MainFac.factions.remove(player);
                    return true;
                }
                if (args[0].equalsIgnoreCase("sethome")) {
                    if (!Cooldown.create(player, 6))
                        return true;
                    if (!getInFaction(player)) {
                        player.sendMessage(ObjectsPreset.your_are_not_in_faction);
                        return true;
                    }
                    if (getFaction(player).getPlayerList().get(player).equals(Ranks.CHEF.toString()) || getFaction(player).getPlayerList().get(player).equals(Ranks.ADJOINT.toString())) {
                        getFaction(player).setLocation_home(player.getLocation());
                        player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.YELLOW + "Vous venez de mettre votre HOME de Faction en " + ChatColor.GOLD + "X: " + player.getLocation().getBlockX() + " Y: " + player.getLocation().getBlockY() + " Z: " + player.getLocation().getBlockZ());
                        return true;
                    } else
                        player.sendMessage(ObjectsPreset.your_are_bad_rank);
                    return true;
                }
                if (args[0].equalsIgnoreCase("home")) {
                    if (!getInFaction(player)) {
                        player.sendMessage(ObjectsPreset.your_are_not_in_faction);
                        return true;
                    }
                    if (getFaction(player).getPlayerList().get(player).equals(Ranks.CHEF.toString()) || getFaction(player).getPlayerList().get(player).equals(Ranks.ADJOINT.toString()) || getFaction(player).getPlayerList().get(player).equals(Ranks.MEMBRE.toString())) {
                        if (getFaction(player).getLocation_home() != null) {
                            if (!teleportHome.containsKey(player)) {
                                teleportHome.put(player, player.getLocation());
                                teleportHome(player);
                            } else {
                                player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.RED + "Vous êtes déjà en attente de téléportation !");
                                return true;
                            }
                        } else player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.RED + "Le HOME à pas encore était placé");
                        return true;
                    } else
                        player.sendMessage(ObjectsPreset.your_are_bad_rank);
                    return true;
                }
                if (args[0].equalsIgnoreCase("map")) {
                    if (!getInFaction(player)) {
                        player.sendMessage(ObjectsPreset.your_are_not_in_faction);
                        return true;
                    }
                    if (!Cooldown.create(player, 6))
                        return true;
                    Chunk chunk = player.getWorld().getChunkAt(player.getLocation());
                    String faction_name = chunk.getPersistentDataContainer().has(new NamespacedKey(MainFac.instance, "faction"), PersistentDataType.INTEGER) ? ChatColor.RED + String.valueOf(chunk.getPersistentDataContainer().get(new NamespacedKey(MainFac.instance, "faction_name"), PersistentDataType.STRING)) : ChatColor.GREEN + "Wilderness";
                    player.sendMessage(ChatColor.GOLD + "_______________[" + " (" + ChatColor.GOLD + chunk.getZ() + ", " + chunk.getX() + ") " + faction_name + ChatColor.GOLD + "]_______________");
                    StringBuilder builder = new StringBuilder();
                    ClaimsManager manager = new ClaimsManager(getFaction(player));
                    for (Chunk c : getChunksAroundPlayer(player)) {
                        if (chunk == c)
                            builder.append(ChatColor.GREEN + "+");
                        else {
                            if (manager.chunkHasClaimed(c))
                                builder.append(ChatColor.RED + "/");
                            else builder.append(ChatColor.RESET + "/");
                        }
                    }
                    player.sendMessage(builder.toString());
                    return true;
                }
                player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.RED + "Il vous manque des informations -> " + ChatColor.GRAY + "/" + s + " " + args[0] + " <element>");
            }

            if (args.length == 2) {
                Faction faction = getFaction(player);
                if (args[0].equalsIgnoreCase("help")) {
                    if (!Cooldown.create(player, 3))
                        return true;
                    if (isNumeric(args[1])) {
                        if (Integer.parseInt(args[1]) >= 1 || Integer.parseInt(args[1]) <= 3) {
                            if (Integer.parseInt(args[1]) == 1)
                                player.sendMessage(ObjectsPreset.help_faction_page_1);
                            if (Integer.parseInt(args[1]) == 2)
                                player.sendMessage(ObjectsPreset.help_faction_page_2);
                            if (Integer.parseInt(args[1]) == 3)
                                player.sendMessage(ObjectsPreset.help_faction_page_3);
                        } else player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.RED + "Mettez un chiffre entre 1 à 3.");
                    } else player.sendMessage(ObjectsPreset.error_numeric);
                    return true;
                }
                if (args[0].equalsIgnoreCase("create")) {
                    if (eco.getBalance(player) < ObjectsPreset.price_to_create_faction) {
                        player.sendMessage(ObjectsPreset.player_no_money + ChatColor.GRAY + "(" + (ObjectsPreset.price_to_create_faction - eco.getBalance(player)) + eco.prefixMoney() + ")");
                        return true;
                    }
                    if (args[1].length() < 3) {
                        player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.RED + "Mettez au minimum 4 caractères !");
                        return true;
                    }
                    if (MainFac.instance.namesOfFactions.contains(args[1])) {
                        player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.RED + "Ce nom existe déjà.");
                        return true;
                    }
                    if (ObjectsPreset.banWordNameFaction.contains(args[1])) {
                        if (!player.hasPermission(Perms.byPass)) {
                            player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.RED + "Vous avez pas le droit d'utiliser ce nom !");
                            return true;
                        }
                    }
                    if (!StringUtils.isAlphanumeric(args[1])) {
                        player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.RED + "Vous devez mettre un nom correct !");
                        return true;
                    }
                    player.sendMessage(ChatColor.YELLOW + "Vous venez de créer votre faction ! " + ChatColor.GOLD + args[1] + ChatColor.GRAY + "\n(Si vous avez un nom non adapté vous pouvez être bannis définitivement !)");
                    new Faction(player, args[1], "Déscription par défaut");
                    eco.removeInBalance(player, Double.valueOf(ObjectsPreset.price_to_create_faction));
                    return true;
                }
                if (args[0].equalsIgnoreCase("rename")) {
                    if (eco.getBalance(player) < ObjectsPreset.price_to_rename_faction) {
                        player.sendMessage(ObjectsPreset.player_no_money + ChatColor.GRAY + "(" + (ObjectsPreset.price_to_rename_faction - eco.getBalance(player)) + eco.prefixMoney() + ")");
                        return true;
                    }
                    if (!getInFaction(player)) {
                        player.sendMessage(ObjectsPreset.your_are_not_in_faction);
                        return true;
                    }
                    if (getFaction(player).getPlayerList().get(player).equalsIgnoreCase(Ranks.CHEF.toString().toString()) || getFaction(player).getPlayerList().get(player).equalsIgnoreCase(Ranks.ADJOINT.toString().toString())) {
                        player.sendMessage(ObjectsPreset.your_are_bad_rank);
                        return true;
                    }
                    if (args[1].length() < 3) {
                        player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.RED + "Mettez au minimum 4 caractères !");
                        return true;
                    }
                    if (MainFac.instance.namesOfFactions.contains(args[1])) {
                        player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.RED + "Ce nom existe déjà.");
                        return true;
                    }
                    if (ObjectsPreset.banWordNameFaction.contains(args[1])) {
                        if (!player.hasPermission(Perms.byPass)) {
                            player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.RED + "Vous n'avez pas le droit d'utiliser ce nom !");
                            return true;
                        }
                    }
                    if (!StringUtils.isAlphanumeric(args[1])) {
                        player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.RED + "Vous devez mettre un nom correct !");
                        return true;
                    }
                    if (args[1].equalsIgnoreCase(getFaction(player).getName())) {
                        player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.RED + "Vous avez pas le droit de mettre le même nom ! ");
                        return true;
                    }
                    getFaction(player).setName(args[1]);
                    player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.YELLOW + "Vous venez de changer le nom de votre faction en " + ChatColor.GOLD + args[1]);
                    return true;
                }
                if (args[0].equalsIgnoreCase("invite") || args[0].equalsIgnoreCase("leave")) {
                    if (args[1].equalsIgnoreCase("accept") || args[1].equalsIgnoreCase("oui")) {
                        if (!notificationInvite.containsKey(player)) {
                            player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.RED + "Vous n'attendez pas d'invitation.");
                            return true;
                        }
                        if (getInFaction(player)) {
                            player.sendMessage(ObjectsPreset.player_already_in_faction);
                            notificationInvite.remove(player);
                            return true;
                        }

                        player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.GREEN + "Vous avez accepté l'invitation");
                        for (Player players : notificationInvite.get(player).getPlayerList().keySet())
                            players.sendMessage( ObjectsPreset.prefix_fac + ChatColor.GOLD + player.getName() + ChatColor.GREEN + " à rejoint la faction");
                        notificationInvite.get(player).addPlayerFaction(player);
                        notificationInvite.remove(player);
                        return true;
                    }
                    if (args[1].equalsIgnoreCase("refuse") || args[1].equalsIgnoreCase("non")) {
                        if (!notificationInvite.containsKey(player)) {
                            player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.RED + "Vous n'attendez pas d'invitation.");
                            return true;
                        }
                        if (getInFaction(player)) {
                            player.sendMessage(ObjectsPreset.player_already_in_faction);
                            notificationInvite.remove(player);
                            return true;
                        }

                        player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.RED + "Vous avez refusé l'invitation");
                        for (Player players : notificationInvite.get(player).getPlayerList().keySet())
                            players.sendMessage(ObjectsPreset.prefix_fac + player.getName() + ChatColor.RED + " à refusé l'invitation");
                        notificationInvite.remove(player);
                        return true;
                    }

                    if (!getInFaction(player)) {
                        player.sendMessage(ObjectsPreset.your_are_not_in_faction);
                        return true;
                    }

                    if (Bukkit.getPlayer(args[1]) == null) {
                        player.sendMessage(ObjectsPreset.player_do_not_exist);
                        return true;
                    }
                    Player target = Bukkit.getPlayer(args[1]);
                    if (getFaction(player).getPlayerList().get(player).equals(Ranks.CHEF.toString()) || getFaction(player).getPlayerList().get(player).equals(Ranks.ADJOINT.toString())) {

                        if (notificationInvite.containsKey(target)) {
                            player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.RED + "Ce joueur est déjà en attente d'invitation");
                            return true;
                        }
                        if (getInFaction(target)) {
                            player.sendMessage(ObjectsPreset.player_already_in_faction);
                            return true;
                        }
                        if (sendNotification(target, getFaction(player))) {
                            target.spigot().sendMessage(new TextComponent(ChatColor.GRAY + "\n§m§l-------------------------------------------" +
                                            ChatColor.YELLOW + "\nVous venez de recevoir une invitation pour rejoindre" +
                                            ChatColor.YELLOW + "\nLa faction " + ChatColor.GOLD + getFaction(player).getName() + ChatColor.YELLOW + " Vous voulez la rejoindre ?\n\n"),
                                    ClickMSG.clickMSG("                [OUI]", net.md_5.bungee.api.ChatColor.GREEN, HoverEvent.Action.SHOW_TEXT, ChatColor.GREEN + "Accepté", ClickEvent.Action.RUN_COMMAND, "/f invite accept"),
                                    ClickMSG.clickMSG("             [NON]", net.md_5.bungee.api.ChatColor.RED, HoverEvent.Action.SHOW_TEXT, ChatColor.RED + "Refusé", ClickEvent.Action.RUN_COMMAND, "/f invite refuse"),
                                    new TextComponent(ChatColor.GRAY + "\n§m§l-------------------------------------------\n"));

                            player.sendMessage(ChatColor.GRAY + "§m§l-------------------------------------------\n" +
                                    ObjectsPreset.prefix_fac + ChatColor.GOLD + "Votre invitation à bien été envoyé à " + ChatColor.YELLOW + target.getName() +
                                    ChatColor.GRAY + "\n§m§l-------------------------------------------");
                        }
                        return true;
                    } else {
                        player.sendMessage(ObjectsPreset.your_are_bad_rank);
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("uninvite")) {
                    if (!getInFaction(player)) {
                        player.sendMessage(ObjectsPreset.your_are_not_in_faction);
                        return true;
                    }

                    if (Bukkit.getPlayer(args[1]) == null) {
                        player.sendMessage(ObjectsPreset.player_do_not_exist);
                        return true;
                    }
                    Player target = Bukkit.getPlayer(args[1]);
                    if (notificationInvite.containsKey(target) && notificationInvite.get(target).getId() == getFaction(player).getId()) {
                        player.sendMessage(ChatColor.GRAY + "\n§m§l-------------------------------------------\n" +
                                ObjectsPreset.prefix_fac + ChatColor.GREEN + "Vous avez retirer votre l'invitation de " + ChatColor.GOLD + target.getName() +
                                ChatColor.GRAY + "\n§m§l-------------------------------------------");

                        target.sendMessage(ChatColor.GRAY + "\n§m§l-------------------------------------------\n" +
                                ObjectsPreset.prefix_fac + ChatColor.RED + "La demande d'invitation qui vous a été envoyé a été annulée" +
                                ChatColor.GRAY + "\n§m§l-------------------------------------------");
                        notificationInvite.remove(target);
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("playerinfo") || args[0].equalsIgnoreCase("info")) {
                    if (!Cooldown.create(player, 6))
                        return true;
                    if (Bukkit.getPlayer(args[1]) == null) {
                        player.sendMessage(ObjectsPreset.player_do_not_exist);
                        return true;
                    }
                    Player target = Bukkit.getPlayer(args[1]);
                    if (!getInFaction(target)) {
                        player.sendMessage(ObjectsPreset.prefix_fac + target.getName() + ChatColor.RED + " à pas de faction");
                        return true;
                    }
                    player.sendMessage(ObjectsPreset.information_faction(target));
                    return true;
                }
                if (args[0].equalsIgnoreCase("kick")) {
                    if (!getInFaction(player)) {
                        player.sendMessage(ObjectsPreset.your_are_not_in_faction);
                        return true;
                    }
                    if (getFaction(player).getPlayerList().get(player).equals(Ranks.CHEF.toString()) || getFaction(player).getPlayerList().get(player).equals(Ranks.ADJOINT.toString())) {
                        for (Player target : getFaction(player).getPlayerList().keySet()) {
                            if (args[1].equalsIgnoreCase(target.getName())) {
                                if (target == player || getFaction(player).getPlayerList().get(target).equals(Ranks.CHEF.toString()) || getFaction(player).getPlayerList().get(target).equals(Ranks.ADJOINT.toString())) {
                                    player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.RED + "Vous ne pouvez pas vous kick cette personne !");
                                    return true;
                                }
                                if (target.isOnline())
                                    target.sendMessage(ChatColor.GRAY + "\n§m§l-------------------------------------------\n" +
                                        ObjectsPreset.prefix_fac + ChatColor.YELLOW + "Vous avez été viré de votre faction." +
                                        ChatColor.GRAY + "\n§m§l-------------------------------------------\n");
                                player.sendMessage(ChatColor.GRAY + "\n§m§l-------------------------------------------\n" +
                                        ObjectsPreset.prefix_fac + ChatColor.YELLOW + "Vous avez viré " + ChatColor.GOLD + target.getName() +
                                        ChatColor.GRAY + "\n§m§l-------------------------------------------\n");
                                getFaction(player).removePlayer(target);
                                return true;
                            } else {
                                player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.RED + "Ce joueur est pas dans votre faction !");
                                return true;
                            }
                        }
                    } else {
                        player.sendMessage(ObjectsPreset.your_are_bad_rank);
                        return true;
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("promote")) {
                    if (!getInFaction(player)) {
                        player.sendMessage(ObjectsPreset.your_are_not_in_faction);
                        return true;
                    }
                    if (getFaction(player).getPlayerList().get(player).equals(Ranks.CHEF.toString()) || getFaction(player).getPlayerList().get(player).equals(Ranks.ADJOINT.toString())) {
                        for (Player target : getFaction(player).getPlayerList().keySet()) {
                            if (args[1].equalsIgnoreCase(target.getName())) {
                                if (promote(player, target)) {
                                    target.sendMessage(ObjectsPreset.prefix_fac + ChatColor.YELLOW + "Vous avez été promu");
                                    player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.YELLOW + "Vous avez promu " + target.getName());
                                } else
                                    player.sendMessage(ObjectsPreset.your_are_bad_rank + ", ou vous avez atteint le niveau maximum");
                                return true;
                            } else {
                                player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.RED + "Ce joueur est pas dans votre faction !");
                                return true;
                            }
                        }
                    } else {
                        player.sendMessage(ObjectsPreset.your_are_bad_rank);
                        return true;
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("setchef")) {
                    if (!getInFaction(player)) {
                        player.sendMessage(ObjectsPreset.your_are_not_in_faction);
                        return true;
                    }
                    if (getFaction(player).getPlayerList().get(player).equals(Ranks.CHEF.toString())) {
                        for (Player target : getFaction(player).getPlayerList().keySet()) {
                            if (args[1].equalsIgnoreCase(target.getName())) {
                                if (target == player) {
                                    player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.RED + "Vous ne pouvez pas vous mettre Chef vous même !");
                                    return true;
                                }
                                if (getFaction(player).getId() == getFaction(target).getId()) {
                                    getFaction(player).getPlayerList().replace(target, Ranks.CHEF.toString().toString());
                                    getFaction(player).getPlayerList().replace(player, Ranks.ADJOINT.toString().toString());
                                    target.sendMessage(ObjectsPreset.prefix_fac + ChatColor.GREEN + "Vous êtes dorénavant " + ChatColor.YELLOW + "CHEF" + ChatColor.GREEN + " de la faction !");
                                    player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.GREEN + "Vous êtes plus chef, dorénavant " + ChatColor.YELLOW + target.getName() + ChatColor.GREEN + " est chef de la faction" + ChatColor.GRAY + " (vous passez ADJOINT)");
                                    for (Player players : getFaction(player).getPlayerList().keySet())
                                        players.sendMessage(ChatColor.GRAY + "\n§m§l-------------------------------------------\n" +
                                                ObjectsPreset.prefix_fac + ChatColor.YELLOW + "Le " + ChatColor.GOLD + "CHEF" + ChatColor.YELLOW + " de la faction à changé ! Le nouveau chef est " + ChatColor.GOLD + target.getName() +
                                                ChatColor.GRAY + "\n§m§l-------------------------------------------\n");
                                    return true;
                                } else
                                    player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.RED + "Cette personne est pas dans votre faction.");
                                return true;
                            }
                        }
                    } else
                        player.sendMessage(ObjectsPreset.your_are_bad_rank);
                    return true;
                }

                if (args[0].equalsIgnoreCase("ally")) {
                    /*ALL VERIFICATION*/
                    if (!getInFaction(player)) {
                        player.sendMessage(ObjectsPreset.your_are_not_in_faction);
                        return true;
                    }
                    if  (Bukkit.getPlayer(args[1]) == null) {
                        player.sendMessage(ObjectsPreset.player_do_not_exist);
                        return true;
                    }
                    Player target = Bukkit.getPlayer(args[1]);
                    if (!getInFaction(player)) {
                        player.sendMessage(ObjectsPreset.player_are_not_in_faction);
                        return true;
                    }
                    if (target == player) {
                        player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.RED + "Vous ne pouvez pas faire cela sur vous même !");
                        return true;
                    }
                    if (getFaction(target).getId() == getFaction(player).getId()) {
                        player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.RED + "Cette personne est de votre faction...");
                        return true;
                    }
                    /*END VERIFICATION*/
                    getFaction(player).setAlly(getFaction(target).getId());
                    player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.YELLOW + "Vous venez de mettre la faction " + ChatColor.GOLD + getFaction(target).getName() + ChatColor.YELLOW + " dans vos alliés " + ChatColor.GRAY + "(Pour le moment cette fonctionnalité n'est pas encore utile)");
                    return true;
                }
                if (args[0].equalsIgnoreCase("enemy")) {
                    /*ALL VERIFICATION*/
                    if (!getInFaction(player)) {
                        player.sendMessage(ObjectsPreset.your_are_not_in_faction);
                        return true;
                    }
                    if  (Bukkit.getPlayer(args[1]) == null) {
                        player.sendMessage(ObjectsPreset.player_do_not_exist);
                        return true;
                    }
                    Player target = Bukkit.getPlayer(args[1]);
                    if (!getInFaction(player)) {
                        player.sendMessage(ObjectsPreset.player_are_not_in_faction);
                        return true;
                    }
                    if (target == player) {
                        player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.RED + "Vous ne pouvez pas faire cela sur vous même !");
                        return true;
                    }
                    if (getFaction(target).getId() == getFaction(player).getId()) {
                        player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.RED + "Cette personne est de votre faction...");
                        return true;
                    }
                    /*END VERIFICATION*/
                    if (getFaction(player).getAlly().contains(getFaction(target)))
                        getFaction(player).getAlly().remove(getFaction(target).getId());
                    getFaction(player).setEnemy(getFaction(target).getId());
                    player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.YELLOW + "Vous venez de mettre la faction " + ChatColor.GOLD + getFaction(target).getName() + ChatColor.YELLOW + " dans vos alliés " + ChatColor.GRAY + "(Pour le moment cette fonctionnalité n'est pas encore utile)");
                    return true;
                }

                if (args[0].equalsIgnoreCase("createrank") || args[0].equalsIgnoreCase("crank")) {
                    if (!Cooldown.create(player, 6))
                        return true;

                    for (RankManager a : faction.getRanks()) {
                        if (a.getName().equalsIgnoreCase(args[1])) {
                            player.sendMessage(ChatColor.RED + "Ce rank existe déjà !");
                            return true;
                        }
                    }
                    if (!StringUtils.isAlphanumeric(args[0])) {
                        player.sendMessage(ChatColor.RED + "Merci de mettre un nom correct !");
                        return true;
                    }
                    if (faction.getPlayerList().get(player).equals(Ranks.CHEF.toString())) {
                        player.sendMessage(ChatColor.YELLOW + "Création du nouveau rank " + ChatColor.GOLD + args[1]);
                        faction.addRank(new RankManager(args[1], args[1], new PermissionManager()));
                        player.sendMessage(ChatColor.YELLOW + "Ouverture des permissions...");
                        PermissionManager.sendGui(player, args[1]);
                        return true;
                    }
                    player.sendMessage(ObjectsPreset.your_are_not_owner);
                }
                if (args[0].equalsIgnoreCase("inforank")) {
                    if (!Cooldown.create(player, 6))
                        return true;
                    for (RankManager a : faction.getRanks())
                        if (a.getName().equalsIgnoreCase(args[1]))
                            if (args[1].equalsIgnoreCase(a.getName()))
                                player.sendMessage("§e§nPermissions de " + args[0] + " : \n" + faction.getRanks().get(faction.getRanks().indexOf(a)).getPermissions().toString());
                    return true;
                }
            }
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("setrank")) {
                    if (!getInFaction(player)) {
                        player.sendMessage(ObjectsPreset.your_are_not_in_faction);
                        return true;
                    }
                    if (getFaction(player).getPlayerList().get(player).equals(Ranks.CHEF.toString()) || getFaction(player).getPlayerList().get(player).equals(Ranks.ADJOINT.toString())) {
                        if (!Ranks.isRank(args[2])) {
                            player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.RED + "Utilisez les ranks disponibles !");
                            return true;
                        }
                        for (Player target : getFaction(player).getPlayerList().keySet()) {
                            if (args[1].equalsIgnoreCase(target.getName())) {
                                if (target == player) {
                                    player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.RED + "Vous ne pouvez pas vous rank vous même");
                                    return true;
                                }
                                if (promote(player, target, args[2])) {
                                    target.sendMessage(ObjectsPreset.prefix_fac + ChatColor.YELLOW + "Vous avez été promu au rank " + ChatColor.GOLD + args[2].toUpperCase());
                                    player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.YELLOW + "Vous avez promu " + ChatColor.GOLD + target.getName() + ChatColor.YELLOW + " en " + ChatColor.GOLD + args[2].toUpperCase());
                                } else
                                    player.sendMessage(ObjectsPreset.your_are_bad_rank + ", ou vous avez atteint le niveau maximum");
                                return true;
                            } else
                                player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.RED + "Ce joueur est pas dans votre faction !");
                        }
                    } else
                        player.sendMessage(ObjectsPreset.your_are_bad_rank);
                }
            }
            if (args.length > 1) {
                if (args[0].equalsIgnoreCase("description")) {
                    if (!getInFaction(player)) {
                        player.sendMessage(ObjectsPreset.your_are_not_in_faction);
                        return true;
                    }
                    if (getFaction(player).getPlayerList().get(player).equals(Ranks.CHEF.toString()) || getFaction(player).getPlayerList().get(player).equals(Ranks.ADJOINT.toString())) {

                        StringBuilder sb = new StringBuilder();
                        for (int i = 1; i < args.length; i++)
                            sb.append(args[i]).append(" ");
                        Faction faction = getFaction(player);
                        faction.setDescription(sb.toString());
                        player.sendMessage(ChatColor.YELLOW + "Vous venez de modifier votre description pour " + ChatColor.GREEN + sb);
                        return true;
                    } else {
                        player.sendMessage(ObjectsPreset.your_are_bad_rank);
                        return true;
                    }
                }
                player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.RED + "Mettez de bonnes informations ! " + ChatColor.GRAY + "</" + s + " " + args[0] + " " + args[1] + ">");
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
        List<String> playerList = new ArrayList<>();
        if (getInFaction((Player) sender))
            if (getFaction((Player) sender).getPlayerList().get((Player) sender).equals(Ranks.CHEF.toString()) || getFaction((Player) sender).getPlayerList().get((Player) sender).equals(Ranks.ADJOINT.toString()))
                for (Player target : getFaction((Player) sender).getPlayerList().keySet())
                    playerList.add(target.getName());

        for (Player p : Bukkit.getOnlinePlayers())
            players.add(p.getName());

        if (args.length == 1) {
            if (!getInFaction((Player) sender))
                return Arrays.asList("create", "playerinfo", "map");
            return Arrays.asList("help", "settings", "disband", "quit", "map", "mapsize", "info", "playerinfo", "rename", "description",
                    "invite", "uninvite", "claimsee", "claim", "unclaim", "sethome", "home", "ranks", "setrank", "setchef", "ally", "enemy", "kick",
                    "upgrade", "info", "promote", "createrank", "crank", "inforank");
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("setrank")) {
                if (getInFaction((Player) sender)) {
                    List<String> playerInFaction = new ArrayList<>();
                    for (Player p : getFaction((Player) sender).getPlayerList().keySet())
                        playerInFaction.add(p.getName());
                    return playerInFaction;
                }
            }
            if (args[0].equalsIgnoreCase("inforank")) {
                if (getInFaction((Player) sender)) {
                    List<String> nameRanks = new ArrayList<>();
                    for (RankManager a : getFaction((Player) sender).getRanks())
                        nameRanks.add(a.getName());
                    return nameRanks;
                }
            }
        }

        if (args.length == 3)
            if (args[0].equalsIgnoreCase("setrank"))
                if (getInFaction((Player) sender)) {
                    List<String> list_ranks = new ArrayList<>();
                    for (RankManager a : getFaction((Player) sender).getRanks())
                        list_ranks.add(a.getName());
                    return list_ranks;
                }


        if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("mapsize"))
            return Arrays.asList("1", "2", "3");

        if (args[0].equalsIgnoreCase("kick") || args[0].equalsIgnoreCase("promote") || args[0].equalsIgnoreCase("setrank") || args[0].equalsIgnoreCase("setchef") )
            return playerList;

        if (args[0].equalsIgnoreCase("playerinfo") || args[0].equalsIgnoreCase("invite") || args[0].equalsIgnoreCase("uninvite"))
            return players;

        return null;
    }

    public boolean sendNotification(Player player, Faction faction) {
        if (notificationInvite.containsKey(player))
            return false;
        notificationInvite.put(player, faction);
        return true;
    }
    //todo add automatisation for faction
    public Boolean promote(Player player, Player target) {
        String rank = getFaction(target).getPlayerList().get(target);
        if (rank.equals(Ranks.RECRUE.toString())) {
            getFaction(target).getPlayerList().replace(target, Ranks.MEMBRE.toString());
            return true;
        }
        if (getFaction(player).getPlayerList().get(player).equals(Ranks.CHEF.toString())) {
            if (rank.equals(Ranks.MEMBRE.toString())) {
                getFaction(target).getPlayerList().replace(target, Ranks.ADJOINT.toString());
                return true;
            }
        }
        return false;
    }
    public boolean promote(Player player, Player target, String rank) {
        if (getFaction(player).getPlayerList().get(player).equals(Ranks.CHEF.toString())) {
            if (rank.equals(Ranks.ADJOINT.toString())) {
                getFaction(target).getPlayerList().replace(target, Ranks.ADJOINT.toString());
                return true;
            }
        }
        if (!rank.equals(Ranks.CHEF.toString())) {
            getFaction(target).getPlayerList().replace(target, rank);
            return true;
        }
        return false;
    }

    public void teleportHome(Player player) {
        BukkitTask task = new BukkitRunnable() {
            int result = 10;
            @Override
            public void run() {
                if (!player.isOnline())
                    cancel();
                if (player.getLocation().getX() != teleportHome.get(player).getX() || player.getLocation().getY() != teleportHome.get(player).getY() || player.getLocation().getZ() != teleportHome.get(player).getZ()) {
                    player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.RED + "Téléportation annulé, vous avez bougé !");
                    teleportHome.remove(player);
                    cancel();
                }
                if (result == 10)
                    player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.YELLOW + "Téléportation dans " + ChatColor.GOLD + "10 secondes");

                if (result <6)
                    player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.YELLOW + "Vous allez être téléporter dans " + ChatColor.GOLD + result + " secondes");

                if (result == 0) {
                    player.teleport(getFaction(player).getLocation_home());
                    player.sendMessage(ObjectsPreset.prefix_fac + ChatColor.YELLOW + "Vous avez été téléporter au HOME de faction");
                    teleportHome.remove(player);
                    cancel();
                }
                result--;
            }
        }.runTaskTimer(MainFac.instance, 0, 20);
    }

    public Collection<Chunk> getChunksAroundPlayer(Player player) {
        int[] offset = {-1,0,1};

        World world = player.getWorld();
        int baseX = player.getLocation().getChunk().getX();
        int baseZ = player.getLocation().getChunk().getZ();

        Collection<Chunk> chunksAroundPlayer = new HashSet<>();
        for(int x : offset) {
            for(int z : offset) {
                Chunk chunk = world.getChunkAt(baseX + x, baseZ + z);
                chunksAroundPlayer.add(chunk);
            }
        } return chunksAroundPlayer;
    }
}
