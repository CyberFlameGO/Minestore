package com.lielamar.minestore.shared.modules;

import java.util.UUID;

public abstract class CustomPlayer {

    private boolean authenticated;

    public CustomPlayer() {
        this.authenticated = false;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public boolean hasAuthenticated() {
        return this.authenticated;
    }

    public abstract String getPlayerName();
    public abstract UUID getPlayerUUID();
}
