package com.lielamar.minestore.bukkit.handlers.requests;

import com.lielamar.minestore.shared.handlers.requests.types.GivePackageRequest;
import com.lielamar.minestore.shared.modules.MinestorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import java.net.Socket;
import java.util.Map;

public class BukkitGivePackageRequest extends GivePackageRequest {

    /**
     * Bukkit GivePacketRequest.
     * When running this request, the server checks if the player is online.
     * If not, it adds the data to a database and checks if the player joins, then it gives them the items.
     * If they are online, it automatically gives them the items.
     */

    public BukkitGivePackageRequest(MinestorePlugin plugin, Socket socket, int protocolVersion, int requestId, JSONObject data) {
        super(plugin, socket, protocolVersion, requestId, data);
    }

    @Override
    public void giveItems(Map<String, String> commandServer) {
        Player player = Bukkit.getPlayer(playerIGN);
        if(player == null)
            return;

        for(String command : commandServer.keySet()) {
            String server = commandServer.get(command);

            executeCommand(command, server);
        }
    }

    @Override
    public void executeCommand(String command, String server) {
        command = command.replace("%player%", playerIGN).replaceAll("%package%", packageName);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }
}
