package com.lielamar.minestore.bukkit.handlers.requests;

import com.lielamar.minestore.bukkit.Minestore;
import com.lielamar.minestore.shared.modules.CustomPlayer;
import com.lielamar.minestore.shared.network.requests.types.AuthenticationRequest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class BukkitAuthenticationRequest extends AuthenticationRequest {

    /**
     * Bukkit AuthenticationRequest.
     * When running this request, a message is being sent to the target player if they are online.
     * In addition, a 5 minutes timer starts, in which the player can use /Minestore auth to authenticate
     * their next purchase.
     */

    private final Minestore plugin;

    public BukkitAuthenticationRequest(Minestore plugin, Socket socket, int protocolVersion, int requestId, String data) {
        super(socket, protocolVersion, requestId, data);

        this.plugin = plugin;
    }

    @Override
    public void runRequest() {
        Player targetPlayer = Bukkit.getPlayer(getTargetPlayerName());

        if(targetPlayer != null)
            targetPlayer.sendMessage(ChatColor.GRAY + "[STORE] " + ChatColor.AQUA + "Please authenticate your purchase via /Minestore auth!");

        new BukkitRunnable() {
            CustomPlayer customPlayer = null;
            int time = 300;

            @Override
            public void run() {
                if(time <= 0) {
                    this.cancel();

                    plugin.getRequestHandler().removeRequest(BukkitAuthenticationRequest.this);
                    return;
                }

                if(targetPlayer != null && targetPlayer.isOnline()) {
                    if(customPlayer == null) {
                        customPlayer = plugin.getPlayerManager().getPlayer(targetPlayer.getUniqueId());
                        // Removing the "previous" authentication to prevent authenticating automatically
                        customPlayer.setAuthenticated(false);
                    }

                    if(customPlayer.hasAuthenticated()) {
                        try {
                            DataOutputStream out = new DataOutputStream(getSocket().getOutputStream());
                            out.writeInt(getProtocolVersion());
                            out.writeInt(getRequestId());
                            out.writeBoolean(true);
                            out.flush();

                            getSocket().close();
                            cancel();

                            plugin.getRequestHandler().removeRequest(BukkitAuthenticationRequest.this);
                            customPlayer.setAuthenticated(false);
                            targetPlayer.sendMessage(ChatColor.GRAY + "[STORE] " + ChatColor.AQUA + "You have authenticated your purchase!");
                            return;
                        } catch(IOException exception) {
                            exception.printStackTrace();
                        }
                    }
                }
                time--;
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 20L);
    }
}
