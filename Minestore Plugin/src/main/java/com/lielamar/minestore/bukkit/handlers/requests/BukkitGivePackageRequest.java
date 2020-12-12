package com.lielamar.minestore.bukkit.handlers.requests;

import com.lielamar.minestore.bukkit.Minestore;
import com.lielamar.minestore.shared.network.requests.types.AuthenticationRequest;

import java.net.Socket;

public class BukkitGivePackageRequest extends AuthenticationRequest {

    /**
     * Bukkit GivePacketRequest.
     * When running this request, the server checks if the player is online.
     * If not, it adds the data to a database and checks if the player joins, then it gives them the items.
     * If they are online, it automatically gives them the items.
     */

    private final Minestore plugin;

    public BukkitGivePackageRequest(Minestore plugin, Socket socket, int protocolVersion, int requestId, String data) {
        super(socket, protocolVersion, requestId, data);

        this.plugin = plugin;
    }

    @Override
    public void runRequest() {

        // TODO:
        // Write this method

        plugin.getRequestHandler().removeRequest(this);
        return;
    }
}
