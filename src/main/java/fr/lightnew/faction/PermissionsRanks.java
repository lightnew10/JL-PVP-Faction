package fr.lightnew.faction;

import org.bukkit.ChatColor;

public enum PermissionsRanks {
    SETTINGS(ChatColor.YELLOW + "Paramètre", ChatColor.GRAY + "accéder aux paramètres"),
    DISBAND(ChatColor.YELLOW + "Dissoudre", ChatColor.GRAY + "Détruite la faction"),
    RENAME(ChatColor.YELLOW + "Renommé", ChatColor.GRAY + "Renommé la faction"),
    DESCRIPTION(ChatColor.YELLOW + "Description", ChatColor.GRAY + "Changer la description"),
    INVITE(ChatColor.YELLOW + "Invitation", ChatColor.GRAY + "Invité un joueur"),
    UNINVITE(ChatColor.YELLOW + "Dé-invitation", ChatColor.GRAY + "Supprimé l'invitation"),
    CLAIMSEE(ChatColor.YELLOW + "Tout les claims", ChatColor.GRAY + "Voir tout les claims"),
    CLAIM(ChatColor.YELLOW + "Claim", ChatColor.GRAY + "Ajouter un claim"),
    UNCLAIM(ChatColor.YELLOW + "UnClaim", ChatColor.GRAY + "Retirer un claim"),
    SET_HOME(ChatColor.YELLOW + "Set-Home", ChatColor.GRAY + "Placé le home de faction"),
    HOME(ChatColor.YELLOW + "Home", ChatColor.GRAY + "Se téléporter au home"),
    CREATE_RANK(ChatColor.YELLOW + "Create-Rank", ChatColor.GRAY + "Créer un nouveau rank"),
    SET_RANK(ChatColor.YELLOW + "Set-Rank", ChatColor.GRAY + "Mettre un nouveau rank au joueur"),
    ADD_ALLY(ChatColor.YELLOW + "Add Ally", ChatColor.GRAY + "Ajouter un allié"),
    ADD_ENEMY(ChatColor.YELLOW + "Add Enemy", ChatColor.GRAY + "Ajouter un ennemi"),
    KICK(ChatColor.YELLOW + "Kick", ChatColor.GRAY + "Retirer un membre de la faction"),
    UPGRADE(ChatColor.YELLOW + "Upgrade", ChatColor.GRAY + "Faire monter la faction"),
    PROMOTE(ChatColor.YELLOW + "Promote", ChatColor.GRAY + "Augmenter le rank d'un joueur");

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

