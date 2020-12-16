package com.lielamar.minestore.bukkit.handlers.requests;

import com.lielamar.minestore.shared.handlers.requests.types.AuthenticationRequest;
import com.lielamar.minestore.shared.modules.MinestorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import javax.annotation.Nullable;
import java.net.Socket;
import java.util.UUID;

public class BukkitAuthenticationRequest extends AuthenticationRequest {

    /**
     * Bukkit AuthenticationRequest.
     * When running this request, a message is being sent to the target player if they are online.
     * In addition, a 5 minutes timer starts, in which the player can use /Minestore auth to authenticate
     * their next purchase.
     */

    public BukkitAuthenticationRequest(MinestorePlugin plugin, Socket socket, int protocolVersion, int requestId, JSONObject data) {
        super(plugin, socket, protocolVersion, requestId, data);
    }

    /**
     * Loads everything before starting the authentication timer.
     * Also loads the target player from the given UUID, sends them a message if they are online
     * and returns the object
     *
     * @param uuid   UUID of the target player
     * @return       Target player as a typeless object
     */
    @Override
    @Nullable
    public Object preTimer(UUID uuid) {
        Player targetPlayer = Bukkit.getPlayer(uuid);

        if(targetPlayer != null)
            targetPlayer.sendMessage(ChatColor.GRAY + "[STORE] " + ChatColor.AQUA + "Please authenticate your purchase via /Minestore auth!");

        return targetPlayer;
    }

    /**
     * Loads everything after the timer ends.
     * Also loads the target player from the given UUID and sends them a message if they are online
     *
     * @param uuid   UUID of the target player
     */
    @Override
    public void postTimer(UUID uuid) {
        Player targetPlayer = Bukkit.getPlayer(uuid);

        if(targetPlayer != null)
            targetPlayer.sendMessage(ChatColor.GRAY + "[STORE] " + ChatColor.AQUA + "You have authenticated your purchase!");
    }

    /**
     * Sends a message to the target player
     *
     * @param targetPlayer   Target player to send message to
     * @param message        Message to send
     */
    @Override
    public void sendMessage(Object targetPlayer, String message) {
        ((Player)targetPlayer).sendMessage(message);
    }
}