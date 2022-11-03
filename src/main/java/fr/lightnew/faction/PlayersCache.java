package fr.lightnew.faction;

import fr.lightnew.MainFac;
import fr.lightnew.faction.Faction;
import fr.lightnew.faction.Ranks;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayersCache {

    private Player player;
    private int money;
    private int power;
    private String grade;
    private int factionID;
    private Faction faction;
    private Ranks ranks;

    public PlayersCache(Player player) {
        this.player = player;
        /*get all elements in sql*/
        this.money = 0;
        this.power = 10;
        this.grade = "";
        this.factionID = 0;
        if (factionID != 0)
            this.faction = new Faction(factionID);
        this.ranks = Ranks.CHEF;
        MainFac.instance.listPlayerCache.put(player, this);
    }

    /*GETTER*/
    public int getMoney() {
        return money;
    }

    public int getPowerDataBase() {return power;}
    public int getPower() {
        return new PowerManager(player).getPower();
    }

    public String getGrade() {
        return grade;
    }

    public int getFactionID() {
        return factionID;
    }

    public Faction getFaction() {
        return faction;
    }

    public Ranks getRanks() {
        return ranks;
    }

    /*SETTER*/
    public void setFactionID(int factionID) {
        this.factionID = factionID;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setRanks(Ranks ranks) {
        this.ranks = ranks;
    }

    public void sendModifications() {
        if (MainFac.instance.listPlayerCache.containsKey(player)) {
            MainFac.instance.listPlayerCache.remove(player, this);
            MainFac.instance.listPlayerCache.put(player, this);
        } else MainFac.instance.listPlayerCache.put(player, this);
    }
}
