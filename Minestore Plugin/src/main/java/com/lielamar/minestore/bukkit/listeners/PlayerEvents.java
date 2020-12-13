package com.lielamar.minestore.bukkit.listeners;

import com.lielamar.minestore.bukkit.Minestore;
import com.lielamar.minestore.shared.modules.CustomPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEvents implements Listener {

    private final Minestore plugin;

    public PlayerEvents(Minestore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        CustomPlayer cp = new CustomPlayer(event.getPlayer().getName(), event.getPlayer().getUniqueId());
        plugin.getPlayerManager().addPlayer(cp);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.getPlayerManager().removePlayer(event.getPlayer().getUniqueId());
    }
}
