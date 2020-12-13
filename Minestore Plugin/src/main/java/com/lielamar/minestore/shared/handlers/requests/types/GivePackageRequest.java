package com.lielamar.minestore.shared.handlers.requests.types;

import com.lielamar.minestore.shared.handlers.requests.Request;
import com.lielamar.minestore.shared.modules.MinestorePlugin;
import org.json.JSONObject;

import java.net.Socket;

public abstract class GivePackageRequest extends Request {

    private String targetPlayer;
    private int packageId;
    private int transactionId;

    public GivePackageRequest(MinestorePlugin plugin, Socket socket, int protocolVersion, int requestId, JSONObject data) {
        super(plugin, socket, protocolVersion, requestId, data);

        loadRequestByVersion();
    }

    @Override
    public void loadRequestByVersion() {
        switch(getProtocolVersion()) {
            default:
                targetPlayer = getData().getString("player_name");
                packageId = getData().getInt("package_id");
                transactionId = getData().getInt("transaction_id");
                break;
        }
    }

    @Override
    public void runRequest() {
        // TODO:
        // Write this method

        closeRequest();
    }
}
