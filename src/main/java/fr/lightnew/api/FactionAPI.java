package fr.lightnew.api;

import fr.lightnew.MainFac;
import fr.lightnew.faction.Faction;
import fr.lightnew.listeners.PlayerManager;
import org.bukkit.entity.Player;

public class FactionAPI {

    public Faction getFactionPlayer(Player player) {
        return MainFac.getFactions().get(player);
    }

    public String getNameFactionChunk(Player player) {
        return PlayerManager.getClaimHere(player);
    }
}