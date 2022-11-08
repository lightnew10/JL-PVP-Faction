package fr.lightnew.tools;

import fr.lightnew.MainFac;
import fr.lightnew.faction.Faction;
import fr.lightnew.faction.UserData;
import fr.lightnew.faction.Ranks;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class ObjectsPreset {

    public ObjectsPreset() {
        String error_config = ChatColor.RED + "ERREUR, contact admin";
        message_custom_re_join = ChatColor.translateAlternateColorCodes('&', MainFac.instance.getConfig().getString("PlayerManager.join.message-custom-re-join") == null ? error_config : MainFac.instance.getConfig().getString("PlayerManager.join.message-custom-re-join"));
        message_join = ChatColor.translateAlternateColorCodes('&', MainFac.instance.getConfig().getString("PlayerManager.join.message-join") == null ? error_config : MainFac.instance.getConfig().getString("PlayerManager.join.message-join"));
        message_welcome = ChatColor.translateAlternateColorCodes('&', MainFac.instance.getConfig().getString("PlayerManager.join.welcome-message") == null ? error_config : MainFac.instance.getConfig().getString("PlayerManager.join.welcome-message"));
        message_quit = ChatColor.translateAlternateColorCodes('&', MainFac.instance.getConfig().getString("PlayerManager.quit.message-quit") == null ? error_config : MainFac.instance.getConfig().getString("PlayerManager.quit.message-quit"));
        chat_format_with_faction = MainFac.instance.getConfig().getString("chat-format.with-faction").replace('&', '§');
        chat_format_without_faction = MainFac.instance.getConfig().getString("chat-format.without-faction");
        maxslotFaction = MainFac.instance.getConfig().getInt("Faction.slots");
        banWordNameFaction = MainFac.instance.getConfig().getStringList("ban-word-name-faction");
        idFac = YamlConfiguration.loadConfiguration(MainFac.instance.configFac).getInt("Faction.id");
        MainFac.instance.log(ChatColor.YELLOW + "ObjectsPreset is loaded");
    }

    /*PlayerManager*/
    public static String prefix_fac = ChatColor.GRAY + "[" + ChatColor.YELLOW + "JLFac" + ChatColor.GRAY + "] " + ChatColor.RESET;
    public static String message_custom_re_join;
    public static String message_join;
    public static String message_quit;
    public static String message_welcome;
    public static String chat_format_with_faction;
    public static String chat_format_without_faction;
    public static int maxslotFaction;
    public static List<String> banWordNameFaction;
    public static int idFac;
    public static String error_numeric = prefix_fac + ChatColor.RED + "Vous devez mettre un chiffe !";
    public static String chunk_is_not_available = prefix_fac + ChatColor.RED + "Ce chunk est déjà pris !";
    public static String chunk_is_available = prefix_fac + ChatColor.YELLOW + "Vous venez de claim ce chunk";
    public static String claim_remove = prefix_fac + ChatColor.YELLOW + "Votre claim à été retiré";
    public static String claim_not_remove = prefix_fac + ChatColor.RED  + "Claim non retiré";
    public static String your_are_not_in_faction = prefix_fac + ChatColor.RED + "Vous n'êtes pas dans une faction !";
    public static String your_are_not_owner = prefix_fac + ChatColor.RED + "Vous n'êtes pas le chef de la faction !";
    public static String how_get_upgrade_faction = "null pour le moment";

    public static String help_faction_page_1 = ChatColor.YELLOW + "\nVoici le help "+ChatColor.RED+"[N°1/3]\n" +
            ChatColor.GOLD + "==================================================\n" +
            ChatColor.BLUE + "/f "+ChatColor.DARK_AQUA+"help " + ChatColor.AQUA + "[N°Page] " + ChatColor.YELLOW + "Ouvrir les autres page d'aide.\n"+
            ChatColor.BLUE + "/f "+ChatColor.DARK_AQUA+"settings " + ChatColor.YELLOW + "Ouvre le menu de votre faction.\n"+
            ChatColor.BLUE + "/f "+ChatColor.DARK_AQUA+"create " + ChatColor.AQUA + "<nom de la faction> " + ChatColor.YELLOW + "Créer votre faction.\n"+
            ChatColor.BLUE + "/f "+ChatColor.DARK_AQUA+"disband " + ChatColor.YELLOW + "Supprime votre faction.\n"+
            ChatColor.BLUE + "/f "+ChatColor.DARK_AQUA+"join " + ChatColor.AQUA + "<nom de la faction> " + ChatColor.YELLOW + "Accepte une invitation de faction.\n"+
            ChatColor.BLUE + "/f "+ChatColor.DARK_AQUA+"quit " + ChatColor.AQUA + "<nom de la faction> " + ChatColor.YELLOW + "Quitter votre faction.\n"+
            ChatColor.BLUE + "/f "+ChatColor.DARK_AQUA+"map " + ChatColor.YELLOW + "Affiche une carte des morceaux environnants.\n"+
            ChatColor.BLUE + "/f "+ChatColor.DARK_AQUA+"mapsize " + ChatColor.AQUA + "<1,2,3> " + ChatColor.YELLOW + "Change la taille de la carte.\n"+
            ChatColor.BLUE + "/f "+ChatColor.DARK_AQUA+"info " + ChatColor.AQUA + "<faction> " + ChatColor.YELLOW + "Affiche les informations sur la faction.\n"+
            ChatColor.BLUE + "/f "+ChatColor.DARK_AQUA+"playerinfo " + ChatColor.AQUA + "<joueur> " + ChatColor.YELLOW + "Affiche les informations sur le joueur et sa faction.\n"+
            ChatColor.BLUE + "/f "+ChatColor.DARK_AQUA+"rename " + ChatColor.AQUA + "<nom> " + ChatColor.YELLOW + "Définit un nouveau nom pour votre faction (peut être changé une fois).\n";

    public static String help_faction_page_2 = ChatColor.YELLOW + "\nVoici le help "+ChatColor.RED+"[N°2/3]\n" +
            ChatColor.GOLD + "==================================================\n" +
            ChatColor.BLUE + "/f "+ChatColor.DARK_AQUA+"description " + ChatColor.YELLOW + "Change la description de votre faction.\n"+
            ChatColor.BLUE + "/f "+ChatColor.DARK_AQUA+"invite " + ChatColor.AQUA + "<joueur> " + ChatColor.YELLOW + "Inviter un joueur.\n"+
            ChatColor.BLUE + "/f "+ChatColor.DARK_AQUA+"uninvite " + ChatColor.AQUA + "<joueur> " + ChatColor.YELLOW + "Désinvite le joueur.\n"+
            ChatColor.BLUE + "/f "+ChatColor.DARK_AQUA+"claimsee " + ChatColor.YELLOW + "Voir les coordonnées de vos claims.\n"+
            ChatColor.BLUE + "/f "+ChatColor.DARK_AQUA+"claim " + ChatColor.YELLOW + "Revendiqué la zone où vous êtes.\n"+
            ChatColor.BLUE + "/f "+ChatColor.DARK_AQUA+"unclaim " + ChatColor.YELLOW + "Annule la revendication de la zone où vous êtes.\n"+
            ChatColor.BLUE + "/f "+ChatColor.DARK_AQUA+"sethome " + ChatColor.YELLOW + "Place votre point de téléportation de maison de faction.\n"+
            ChatColor.BLUE + "/f "+ChatColor.DARK_AQUA+"home " + ChatColor.YELLOW + "Vous téléporte à la maison de faction.\n"+
            ChatColor.BLUE + "/f "+ChatColor.DARK_AQUA+"ranks " + ChatColor.YELLOW + "Affiche les rangs de vos factions.\n"+
            ChatColor.BLUE + "/f "+ChatColor.DARK_AQUA+"setrank " + ChatColor.AQUA + "<joueur> " + ChatColor.YELLOW + "Change le rank du joueur.\n"+
            ChatColor.BLUE + "/f "+ChatColor.DARK_AQUA+"setowner " + ChatColor.AQUA + "<joueur> " + ChatColor.YELLOW + "Met le joueur chef de la faction.\n";

    public static String help_faction_page_3 = ChatColor.YELLOW + "\nVoici le help "+ChatColor.RED+"[N°3/3]\n" +
            ChatColor.GOLD + "==================================================\n" +
            ChatColor.BLUE + "/f "+ChatColor.DARK_AQUA+"ally " + ChatColor.AQUA + "<faction ou joueur> " + ChatColor.YELLOW + "Met la faction en alliée.\n"+
            ChatColor.BLUE + "/f "+ChatColor.DARK_AQUA+"enemy " + ChatColor.AQUA + "<faction ou joueur> " + ChatColor.YELLOW + "Met la faction en ennemie.\n"+
            ChatColor.BLUE + "/f "+ChatColor.DARK_AQUA+"neutre " + ChatColor.AQUA + "<faction ou joueur> " + ChatColor.YELLOW + "Remet la faction en neutre.\n"+
            ChatColor.BLUE + "/f "+ChatColor.DARK_AQUA+"kick " + ChatColor.AQUA + "<joueur> " + ChatColor.YELLOW + "Retirer un joueur de la faction.\n"+
            ChatColor.BLUE + "/f "+ChatColor.DARK_AQUA+"faction ou f " + ChatColor.YELLOW + "Retirer un joueur de la faction.\n"+
            ChatColor.BLUE + "/f "+ChatColor.DARK_AQUA+"upgrade " + ChatColor.YELLOW + "Augmente la faction au niveau supérieur.\n";

    public static String information_faction(Player player) {
        if (!MainFac.getFactions().containsKey(player))
            return prefix_fac + ChatColor.RED + "Vous n'êtes pas dans une faction !";

        Faction faction = MainFac.getFactions().get(player);
        String base = ChatColor.GRAY + "\n===========================" +
                ChatColor.YELLOW + ChatColor.YELLOW + "\nNom de Faction ► " + ChatColor.GOLD + faction.getName() +
                ChatColor.YELLOW + "\nDescription ► " + ChatColor.GOLD + faction.getDescription() +
                ChatColor.YELLOW + "\nChef de la faction : " + ChatColor.GOLD + faction.getOwner().getName() +
                ChatColor.YELLOW + "\nPower ► " + ChatColor.GOLD + faction.getPower() + "/10" +
                ChatColor.YELLOW + "\nListe des membres ► \n";

        StringBuilder builder = new StringBuilder(base);
        for (Player p : faction.getPlayerList().keySet()) {
            Ranks ranks = faction.getPlayerList().get(p);
            builder.append("- " + ChatColor.GOLD + ranks + " " + p.getName());
        }

        if (!faction.getAlly().isEmpty()) {
            builder.append("Alliés ► \n");
            for (Faction f : faction.getAlly())
                builder.append("- " + ChatColor.GOLD + f.getName());
        } else builder.append(ChatColor.YELLOW + "\nAlliés ► Aucun");

        if (!faction.getEnemy().isEmpty()) {
            builder.append("\nEnnemies ► \n");
            for (Faction f : faction.getEnemy())
                builder.append("- " + ChatColor.GOLD + f.getName());
        } else builder.append(ChatColor.YELLOW + "\nEnnemies ► Aucun");

        return builder.toString();
    }
}