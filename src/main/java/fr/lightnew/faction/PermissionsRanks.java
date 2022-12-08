package fr.lightnew.faction;

import org.bukkit.ChatColor;

public enum PermissionsRanks {
    SETTINGS(ChatColor.YELLOW + "Paramètre", ChatColor.GRAY + "§nCette permission permet de :", "", ChatColor.GRAY + "Accéder aux paramètres de la faction", "", ChatColor.GRAY + "Permission >>", ChatColor.RED + "Désactivé"),
    DISBAND(ChatColor.YELLOW + "Dissoudre", ChatColor.GRAY + "§nCette permission permet de :" , "", ChatColor.GRAY + "Détruite la faction", "", ChatColor.GRAY + "Permission >>", ChatColor.RED + "Désactivé"),
    RENAME(ChatColor.YELLOW + "Renommé", ChatColor.GRAY + "§nCette permission permet de :" , "", ChatColor.GRAY + "Renommé la faction", "", ChatColor.GRAY + "Permission >>", ChatColor.RED + "Désactivé"),
    DESCRIPTION(ChatColor.YELLOW + "Description", ChatColor.GRAY + "§nCette permission permet de :" , "", ChatColor.GRAY + "Changer la description", "", ChatColor.GRAY + "Permission >>", ChatColor.RED + "Désactivé"),
    INVITE(ChatColor.YELLOW + "Invitation", ChatColor.GRAY + "§nCette permission permet de :" , "", ChatColor.GRAY + "Invité un joueur", "", ChatColor.GRAY + "Permission >>", ChatColor.RED + "Désactivé"),
    UNINVITE(ChatColor.YELLOW + "Dé-invitation", ChatColor.GRAY + "§nCette permission permet de :" , "", ChatColor.GRAY + "Supprimé l'invitation", "", ChatColor.GRAY + "Permission >>", ChatColor.RED + "Désactivé"),
    CLAIMSEE(ChatColor.YELLOW + "Tout les claims", ChatColor.GRAY + "§nCette permission permet de :" , "", ChatColor.GRAY + "Voir tout les claims", "", ChatColor.GRAY + "Permission >>", ChatColor.RED + "Désactivé"),
    CLAIM(ChatColor.YELLOW + "Claim", ChatColor.GRAY + "§nCette permission permet de :" , "", ChatColor.GRAY + "Ajouter un claim", "", ChatColor.GRAY + "Permission >>", ChatColor.RED + "Désactivé"),
    UNCLAIM(ChatColor.YELLOW + "UnClaim", ChatColor.GRAY + "§nCette permission permet de :" , "", ChatColor.GRAY + "Retirer un claim", "", ChatColor.GRAY + "Permission >>", ChatColor.RED + "Désactivé"),
    SET_HOME(ChatColor.YELLOW + "Set-Home", ChatColor.GRAY + "§nCette permission permet de :" , "", ChatColor.GRAY + "Placé le home de faction", "", ChatColor.GRAY + "Permission >>", ChatColor.RED + "Désactivé"),
    HOME(ChatColor.YELLOW + "Home", ChatColor.GRAY + "§nCette permission permet de :" , "", ChatColor.GRAY + "Se téléporter au home", "", ChatColor.GRAY + "Permission >>", ChatColor.RED + "Désactivé"),
    CREATE_RANK(ChatColor.YELLOW + "Create-Rank", ChatColor.GRAY + "§nCette permission permet de :" , "", ChatColor.GRAY + "Créer un nouveau rank", "", ChatColor.GRAY + "Permission >>", ChatColor.RED + "Désactivé"),
    SET_RANK(ChatColor.YELLOW + "Set-Rank", ChatColor.GRAY + "§nCette permission permet de :" , "", ChatColor.GRAY + "Mettre un nouveau rank au joueur", "", ChatColor.GRAY + "Permission >>", ChatColor.RED + "Désactivé"),
    ADD_ALLY(ChatColor.YELLOW + "Add Ally", ChatColor.GRAY + "§nCette permission permet de :" , "", ChatColor.GRAY + "Ajouter un allié", "", ChatColor.GRAY + "Permission >>", ChatColor.RED + "Désactivé"),
    ADD_ENEMY(ChatColor.YELLOW + "Add Enemy", ChatColor.GRAY + "§nCette permission permet de :" , "", ChatColor.GRAY + "Ajouter un ennemi", "", ChatColor.GRAY + "Permission >>", ChatColor.RED + "Désactivé"),
    KICK(ChatColor.YELLOW + "Kick", ChatColor.GRAY + "§nCette permission permet de :" , "", ChatColor.GRAY + "Retirer un membre de la faction", "", ChatColor.GRAY + "Permission >>", ChatColor.RED + "Désactivé"),
    UPGRADE(ChatColor.YELLOW + "Upgrade", ChatColor.GRAY + "§nCette permission permet de :" , "", ChatColor.GRAY + "Faire monter la faction", "", ChatColor.GRAY + "Permission >>", ChatColor.RED + "Désactivé"),
    PROMOTE(ChatColor.YELLOW + "Promote", ChatColor.GRAY + "§nCette permission permet de :" , "", ChatColor.GRAY + "Augmenter le rank d'un joueur", "", ChatColor.GRAY + "Permission >>", ChatColor.RED + "Désactivé");

    @Override
    public String toString() {
        return super.toString();
    }

    public final String name;
    public final String[] description;

    PermissionsRanks(String name, String... description) {
        this.name = name;
        this.description = description;
    }
}

