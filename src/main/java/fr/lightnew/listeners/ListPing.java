package fr.lightnew.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ListPing implements Listener {

    @EventHandler
    public void ping(ServerListPingEvent event) {
        event.setMotd(ChatColor.RED + "Serveur Joblife bientôt disponible !");
        event.setMaxPlayers(200);
    }
}
