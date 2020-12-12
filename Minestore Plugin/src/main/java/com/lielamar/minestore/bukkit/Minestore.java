package com.lielamar.minestore.bukkit;

import com.lielamar.minestore.bukkit.commands.MinestoreCommand;
import com.lielamar.minestore.bukkit.handlers.BukkitRequestHandler;
import com.lielamar.minestore.bukkit.listeners.PlayerEvents;
import com.lielamar.minestore.shared.encryption.EncryptionKey;
import com.lielamar.minestore.shared.network.PlayerManager;
import com.lielamar.minestore.shared.network.SocketServerHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class Minestore extends JavaPlugin {

    private BukkitRequestHandler requestHandler;
    private SocketServerHandler socketServerHandler;
    private PlayerManager playerManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        loadMinestore();
        registerEvents();
    }

    @Override
    public void onDisable() {
        destroyMinestore();
    }

    public void loadMinestore() {
        new MinestoreCommand(this);
        int port = getConfig().getInt("Socket Server.port");

        try {
            this.requestHandler = new BukkitRequestHandler(this);
            this.socketServerHandler = new SocketServerHandler(port, requestHandler, new EncryptionKey(getDataFolder().getPath()));
            this.playerManager = new PlayerManager();
        } catch(IOException exception) {
            exception.printStackTrace();
        }
    }

    public void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new PlayerEvents(this), this);
    }

    public void destroyMinestore() {
        try {
            this.socketServerHandler.destroy();
        } catch(IOException exception) {
            exception.printStackTrace();
        }
    }

    public BukkitRequestHandler getRequestHandler() { return this.requestHandler; }
    public SocketServerHandler getSocketServerHandler() { return this.socketServerHandler; }
    public PlayerManager getPlayerManager() { return this.playerManager; }
}
