package com.lielamar.minestore.shared.network.requests.types;

import com.lielamar.minestore.shared.network.requests.Request;

import java.net.Socket;

public abstract class GivePackageRequest extends Request {

    private String targetPlayer;
    private int packageId;
    private int transactionId;

    public GivePackageRequest(Socket socket, int protocolVersion, int requestId, String data) {
        super(socket, protocolVersion, requestId, data);

        loadRequestByVersion();
    }

    @Override
    public void loadRequestByVersion() {
        String[] split;

        switch(getProtocolVersion()) {
            default:
                split = getData().split(";");

                targetPlayer = split[0];
                packageId = Integer.parseInt(split[1]);
                transactionId = Integer.parseInt(split[2]);
                break;
            case 2:
                targetPlayer = getData().split(";")[0];
                break;
        }
    }

    public String getTargetPlayer() {
        return targetPlayer;
    }

    public int getPackageId() {
        return packageId;
    }

    public int getTransactionId() {
        return transactionId;
    }
}
