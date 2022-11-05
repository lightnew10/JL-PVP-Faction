package fr.lightnew.faction;

import fr.lightnew.MainFac;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.HashMap;

public class PowerManager {

    private Player player;
    private int power;
    private Faction faction;
    private PlayersCache cache;
    private double percentage_power_kill = 0.2;
    private Date connection_time;
    private HashMap</*time*/Integer, /*power*/Integer> powerWithTime = MainFac.instance.powerWithTime;

    public PowerManager(Player player) {
        this.player = player;
       cache = new PlayersCache(player);
       this.power = cache.getPowerDataBase();
       this.faction = cache.getFaction();
       this.connection_time = cache.getConnection_time();
    }

    public int getPower() {
        return power;
    }
    public Faction getFaction() {
        return faction;
    }
    public double getPercentage_power_kill() {
        return percentage_power_kill;
    }

    public void addPower(int value) {
        if ((getPower() + value) >= 10)
            power = 10;
        else
            power = (getPower() + value);
    }
    public void remove(int value) {
        if ((getPower() - value) <= 0)
            power = 0;
        else
            power = (getPower() - value);
    }
    /*set % of power player other player (base -> 0.2%*/
    public void setPercentage_power_kill(double percentage_power_kill) {this.percentage_power_kill = percentage_power_kill;}

    /*Set power directly*/
    public void setPower(int power) {
        this.power = power;
    }

    /*if player kill other player he wins %power/kill*/
    public boolean addPowerKill() {
        if (getPower() <= 1) {
            addPower(1);
            return true;
        }
        if (!(getPower() == 9)) {
            int result = (int) (getPower()*getPercentage_power_kill());
            if (result >= 10)
                setPower(10);
            else
                addPower((int) (getPower()*getPercentage_power_kill()));
            return true;
        }
        return false;
    }

    /*Remove One power for each death*/
    public boolean removePowerDeath() {
        if ((getPower() <= 0))
            return false;
        remove(1);
        return true;
    }

    /**/
    public boolean addPowerWithTime() {
        Date date = new Date();
        for (int power : powerWithTime.values()) {
            int time = powerWithTime.get(power);
            int calculTimePlayer = (date.getMinutes()-connection_time.getMinutes());
            Bukkit.getConsoleSender().sendMessage("deux dates : " + date.toString() + " - " + connection_time.toString());

        }
        return false;
    }
}
