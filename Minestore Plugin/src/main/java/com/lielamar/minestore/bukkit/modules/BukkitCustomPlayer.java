package com.lielamar.minestore.bukkit.modules;

import com.lielamar.minestore.shared.modules.CustomPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BukkitCustomPlayer extends CustomPlayer {

    private final Player player;

    public BukkitCustomPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    public String getPlayerName() {
        return this.player.getName();
    }

    public UUID getPlayerUUID() {
        return this.player.getUniqueId();
    }
}
