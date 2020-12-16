package com.lielamar.minestore.bukkit;

import com.lielamar.lielsutils.bstats.MetricsSpigot;
import com.lielamar.lielsutils.update.UpdateChecker;
import com.lielamar.minestore.bukkit.commands.MinestoreCommand;
import com.lielamar.minestore.bukkit.handlers.BukkitRequestHandler;
import com.lielamar.minestore.bukkit.listeners.OnReloadCommand;
import com.lielamar.minestore.bukkit.listeners.PlayerEvents;
import com.lielamar.minestore.shared.encryption.EncryptionKey;
import com.lielamar.minestore.shared.handlers.PlayerHandler;
import com.lielamar.minestore.shared.handlers.SocketServerHandler;
import com.lielamar.minestore.shared.modules.CustomPlayer;
import com.lielamar.minestore.shared.modules.MinestorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;

public class Minestore extends JavaPlugin implements MinestorePlugin {

    private EncryptionKey encryptionKey;
    private BukkitRequestHandler requestHandler;
    private SocketServerHandler socketServerHandler;
    private PlayerHandler playerHandler;

    @Override
    public void onEnable() {
        // TODO: replace resource id & plugin id
        new UpdateChecker(this, 12345);
        new MetricsSpigot(this, 12345);

        saveDefaultConfig();

        loadMinestore();
        registerEvents();
    }

    @Override
    public void onDisable() {
        saveConfig();
    }


    public void loadMinestore() {
        new MinestoreCommand(this);
        int port = getConfig().getInt("Socket Server.port");

        this.encryptionKey = new EncryptionKey(getDataFolder().getPath());
        this.requestHandler = new BukkitRequestHandler(this);
        this.socketServerHandler = new SocketServerHandler(port, requestHandler);
        this.playerHandler = new PlayerHandler();
        for(Player pl : Bukkit.getOnlinePlayers()) {
            this.playerHandler.addPlayer(new CustomPlayer(pl.getName(), pl.getUniqueId()));
        }
    }

    public void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new PlayerEvents(this), this);
        pm.registerEvents(new OnReloadCommand(this), this);
    }

    @Nullable
    public EncryptionKey getEncryptionKey() { return this.encryptionKey; }
    public BukkitRequestHandler getRequestHandler() { return this.requestHandler; }
    public SocketServerHandler getSocketServerHandler() { return this.socketServerHandler; }
    public PlayerHandler getPlayerHandler() { return this.playerHandler; }
}
