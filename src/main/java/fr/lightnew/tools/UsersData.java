package fr.lightnew.tools;

import fr.lightnew.faction.Faction;
import org.bukkit.entity.Player;

public class UsersData {

    private final Player player;

    private int power;
    private Faction faction;

    public UsersData(Player p) {
        this.player = p;
    }

    public int getPower() {
        return power;
    }

    public Faction getFaction() {
        return faction;
    }
}
