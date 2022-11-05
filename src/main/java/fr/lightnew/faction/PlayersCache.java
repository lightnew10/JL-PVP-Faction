package fr.lightnew.faction;

import fr.lightnew.MainFac;
import fr.lightnew.faction.Faction;
import fr.lightnew.faction.Ranks;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Date;

public class PlayersCache {

    private Player player;
    private int money;
    private int power;
    private String grade;
    private Faction faction;
    private Ranks ranks;
    private Date connection_time;

    public PlayersCache(Player player) {
        this.player = player;
        /*get all elements in sql*/
        this.money = 0;
        this.power = 10;
        this.grade = "";
        this.ranks = Ranks.CHEF;
        connection_time = new Date();
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

    public Faction getFaction() {
        return faction;
    }

    public Ranks getRanks() {
        return ranks;
    }

    public Date getConnection_time() {
        return connection_time;
    }

    /*SETTER*/
    public void setFaction(Faction faction) {
        this.faction = faction;
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
