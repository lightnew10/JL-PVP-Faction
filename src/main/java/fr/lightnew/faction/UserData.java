package fr.lightnew.faction;

import fr.lightnew.MainFac;
import org.bukkit.entity.Player;

import java.util.Date;

public class UserData {

    private Player player;
    private int money;
    private int power;
    private String grade;
    private Ranks ranks;
    private Date connection_time;

    public UserData(Player player) {
        this.player = player;
        /*get all elements in sql*/
        this.money = 0;
        this.power = 10;
        this.grade = "";
        this.ranks = Ranks.NONE;
        connection_time = new Date();
        MainFac.instance.playersCache.put(player, this);
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
    public Ranks getRanks() {
        return ranks;
    }
    public Date getConnection_time() {
        return connection_time;
    }

    /*SETTER*/
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
        if (MainFac.instance.playersCache.containsKey(player)) {
            MainFac.instance.playersCache.put(player, this);
        } else MainFac.instance.playersCache.put(player, this);
    }

    public String toString() {
        return "Player -> " + player.getName() +
                "\nMoney -> " + money +
                "\nPower -> " + power +
                "\nGrade -> " + grade +
                "\nRanks -> " + ranks.toString() +
                "\nConnectionTime -> " + connection_time.toString();
    }
}
