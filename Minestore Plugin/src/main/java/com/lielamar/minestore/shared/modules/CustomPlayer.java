package com.lielamar.minestore.shared.modules;

import java.util.UUID;

public class CustomPlayer {

    private final String name;
    private final UUID uuid;

    private boolean authenticated;

    public CustomPlayer(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;

        this.authenticated = false;
    }

    public String getPlayerName() {
        return this.name;
    }

    public UUID getPlayerUUID() {
        return this.uuid;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public boolean hasAuthenticated() {
        return this.authenticated;
    }
}