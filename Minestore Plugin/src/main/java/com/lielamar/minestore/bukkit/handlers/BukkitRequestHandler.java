package com.lielamar.minestore.bukkit.handlers;

import com.lielamar.minestore.bukkit.Minestore;
import com.lielamar.minestore.bukkit.handlers.requests.BukkitAuthenticationRequest;
import com.lielamar.minestore.bukkit.handlers.requests.BukkitGivePackageRequest;
import com.lielamar.minestore.shared.network.RequestHandler;
import com.lielamar.minestore.shared.network.requests.Request;

import java.net.Socket;

public class BukkitRequestHandler extends RequestHandler {

    private final Minestore plugin;

    public BukkitRequestHandler(Minestore plugin) {
        this.plugin = plugin;
    }

    /**
     * Builds a request out of a socket, a protocol version, a request id and data.
     *
     * @param socket            Socket of the request
     * @param protocolVersion   Protocol version
     * @param requestId         Request Id (Type)
     * @param data              Request data
     * @return                  Created request object
     */
    @Override
    public Request getRequestById(Socket socket, int protocolVersion, int requestId, String data) {
        switch(requestId) {
            case 0:
                return new BukkitAuthenticationRequest(plugin, socket, protocolVersion, requestId, data);
            case 1:
                return new BukkitGivePackageRequest(plugin, socket, protocolVersion, requestId, data);
            default:
                return null;
        }
    }
}
