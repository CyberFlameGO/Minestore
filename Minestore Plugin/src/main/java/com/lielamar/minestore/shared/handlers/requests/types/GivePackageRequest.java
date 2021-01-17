package com.lielamar.minestore.shared.handlers.requests.types;

import com.lielamar.minestore.shared.handlers.requests.Request;
import com.lielamar.minestore.shared.modules.MinestorePlugin;
import org.json.JSONObject;

import java.net.Socket;
import java.util.Map;

public abstract class GivePackageRequest extends Request {

    protected String playerIGN;
    protected String packageName;

    private String purchaseId;

    public GivePackageRequest(MinestorePlugin plugin, Socket socket, int protocolVersion, int requestId, JSONObject data) {
        super(plugin, socket, protocolVersion, requestId, data);

        loadRequestByVersion();
    }

    @Override
    public void loadRequestByVersion() {
        switch(getProtocolVersion()) {
            default:
                purchaseId = getData().getString("purchase_id");
                break;
        }
    }

    @Override
    public void runRequest() {
        int received_package = getPlugin().getStorageHandler().isPackageDelivered(purchaseId);
        if(received_package != 0)
            return;

        String packageId = getPlugin().getStorageHandler().getPackageId(purchaseId);
        this.playerIGN = getPlugin().getStorageHandler().getBuyerIGN(purchaseId);
        this.packageName = getPlugin().getStorageHandler().getPackageName(packageId);
        Map<String, String> commandServer = getPlugin().getStorageHandler().getCommandsOfPackage(packageId);

        giveItems(commandServer);
        getPlugin().getStorageHandler().setPackageDelivered(packageId, true);

        closeRequest();
    }

    public abstract void giveItems(Map<String, String> commandServer);
    public abstract void executeCommand(String command, String server);
}
