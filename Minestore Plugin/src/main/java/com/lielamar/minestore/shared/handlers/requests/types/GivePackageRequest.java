package com.lielamar.minestore.shared.handlers.requests.types;

import com.lielamar.minestore.shared.handlers.requests.Request;
import com.lielamar.minestore.shared.modules.MinestorePlugin;
import org.json.JSONObject;

import java.net.Socket;

public abstract class GivePackageRequest extends Request {

    private String purchaseId;

    public GivePackageRequest(MinestorePlugin plugin, Socket socket, int protocolVersion, int requestId, JSONObject data) {
        super(plugin, socket, protocolVersion, requestId, data);

        loadRequestByVersion();
    }

    @Override
    public void loadRequestByVersion() {
        switch(getProtocolVersion()) {
            default:
                System.out.println(getData());
                purchaseId = getData().getString("purchase_id");
                break;
        }
    }

    @Override
    public void runRequest() {
        System.out.println("Received a purchase with the id: " + purchaseId);
        // TODO:
        // Connect to the MySQL Database, get the IGN of the buyer and the commands
        // Then, execute commands here
        closeRequest();
    }
}
