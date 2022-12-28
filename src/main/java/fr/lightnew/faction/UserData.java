package fr.lightnew.faction;

import fr.lightnew.MainFac;
import fr.lightnew.constructor.ConstructFactionRanks;
import fr.lightnew.constructor.ConstructPermissionsRank;
import fr.lightnew.constructor.ConstructPlayer;
import fr.lightnew.tools.Requests;
import org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.Date;
import java.util.UUID;

import static fr.lightnew.MainFac.groupManager;

public class UserData {

    private Player player;
    private int power;
    private String grade;
    private RankManager rank;
    private Date connection_time;

    private String getGroup(final Player player) {
        if (!MainFac.hasGroupManager()) return null;

        final AnjoPermissionsHandler handler = groupManager.getWorldsHolder().getWorldPermissions(player);
        if (handler == null) return null;

        return handler.getUserPrefix(player.getName());
    }

    private UUID getIDRank(UUID factID, String nameRank) {
        ConstructFactionRanks ranks = Requests.getRanksFaction(factID);
        for (String s : ranks.getListRanks().keySet())
            if (s.equalsIgnoreCase(nameRank))
                return ranks.getListRanks().get(s);
        return null;
    }

    public UserData(Player player) {
        ConstructPlayer cp = Requests.getPlayerData(player.getUniqueId());
        this.player = player;
        this.power = cp.getPower();
        this.grade = getGroup(player).replace('&', '§');
        /*if (cp.getFactionID() != null) {
            ConstructPermissionsRank perm = Requests.getPermissionsRank(cp.getFactionID(), getIDRank(cp.getFactionID(), cp.getRank_faction()));
            this.rank = new RankManager(cp.getRank_faction(), new PermissionManager(perm.getSettings(), perm.getDisband(), perm.getRenameFaction(), perm.getDescriptionFaction(), perm.getInvite(), perm.getUninvite(), perm.getClaimsee(), perm.getClaim(), perm.getUnclaim(), perm.getSethome(),
                    perm.getHome(), perm.getCreaterank(), perm.getSetrank(), perm.getAddally(), perm.getAddenemy(), perm.getKick(), perm.getUpgradeFaction(), perm.getPromote()));//get in sql
        } else*/
        this.rank = null;
        connection_time = new Date();
        MainFac.instance.playersCache.put(player, this);
    }

    /*GETTER*/
    public int getPowerDataBase() {return power;}
    public int getPower() {
        return new PowerManager(player).getPower();
    }
    public String getGrade() {
        return grade;
    }
    public Date getConnection_time() {
        return connection_time;
    }
    public RankManager getRank() {
        return rank;
    }

    /*SETTER*/
    public void setRank(RankManager rank) {
        this.rank = rank;
    }

    public void sendModifications() {
        if (MainFac.instance.playersCache.containsKey(player)) {
            MainFac.instance.playersCache.replace(player, this);
        } else MainFac.instance.playersCache.put(player, this);
    }

    public String toString() {
        return "Player -> " + player.getName() +
                "\nPower -> " + power +
                "\nRanks -> " + rank +
                "\nConnectionTime -> " + connection_time.toString();
    }
}
