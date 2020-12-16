package com.lielamar.minestore.bukkit.listeners;

import com.lielamar.minestore.shared.handlers.requests.Request;
import com.lielamar.minestore.shared.modules.MinestorePlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import java.io.IOException;

public class OnReloadCommand implements Listener {

    private final MinestorePlugin minestore;
    public OnReloadCommand(MinestorePlugin minestore) {
        this.minestore = minestore;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        check(event.getMessage().startsWith("/") ? event.getMessage().substring(1) : event.getMessage());
    }

    @EventHandler
    public void onConsoleCommand(ServerCommandEvent event) {
        check(event.getCommand());
    }

    public void check(String command) {
        if(command.equalsIgnoreCase("rl") || command.equalsIgnoreCase("reload")) {
            try {
                for(Request request : minestore.getRequestHandler().getRequests())
                    request.closeRequest();

                minestore.getSocketServerHandler().getServerSocket().close();
            } catch(IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}
