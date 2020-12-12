package com.lielamar.minestore.shared.network;

import com.lielamar.minestore.shared.modules.CustomPlayer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class PlayerManager {

    private final List<CustomPlayer> players;

    public PlayerManager() {
        this.players = new ArrayList<>();
    }

    public List<CustomPlayer> getPlayers() {
        return this.players;
    }

    public void addPlayer(CustomPlayer player) {
        this.players.add(player);
    }

    public void removePlayer(CustomPlayer player) {
        this.players.remove(player);
    }

    public void removePlayer(UUID uuid) {
        Iterator<CustomPlayer> it = this.players.iterator();
        CustomPlayer cp;

        while(it.hasNext()) {
            cp = it.next();

            if(cp.getPlayerUUID() == uuid)
                this.players.remove(cp);
        }
    }

    public CustomPlayer getPlayer(UUID uuid) {
        for(CustomPlayer cp : this.players) {
            if(cp.getPlayerUUID() == uuid)
                return cp;
        }
        return null;
    }
}
