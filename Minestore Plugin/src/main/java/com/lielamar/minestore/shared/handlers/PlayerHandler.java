package com.lielamar.minestore.shared.handlers;

import com.lielamar.minestore.shared.modules.CustomPlayer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class PlayerHandler {

    private final Set<CustomPlayer> players;

    public PlayerHandler() {
        this.players = new HashSet<>();
    }

    public void addPlayer(@Nonnull CustomPlayer player) {
        this.players.add(player);
    }

    public void removePlayer(@Nonnull UUID uuid) {
        Iterator<CustomPlayer> it = this.players.iterator();
        CustomPlayer cp;

        while(it.hasNext()) {
            cp = it.next();

            if(cp.getPlayerUUID() == uuid)
                this.players.remove(cp);
        }
    }

    @Nullable
    public CustomPlayer getPlayer(UUID uuid) {
        for(CustomPlayer cp : this.players) {
            if(cp.getPlayerUUID() == uuid)
                return cp;
        }
        return null;
    }
}
