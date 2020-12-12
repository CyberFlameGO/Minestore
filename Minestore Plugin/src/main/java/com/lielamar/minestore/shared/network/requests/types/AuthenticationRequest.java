package com.lielamar.minestore.shared.network.requests.types;

import com.lielamar.minestore.shared.network.requests.Request;

import java.net.Socket;

public abstract class AuthenticationRequest extends Request {

    private String targetPlayerName;

    public AuthenticationRequest(Socket socket, int protocolVersion, int requestId, String data) {
        super(socket, protocolVersion, requestId, data);

        loadRequestByVersion();
    }

    @Override
    public void loadRequestByVersion() {
        String[] split;

        switch(getProtocolVersion()) {
            default:
                targetPlayerName = getData();
                break;
            case 2:
                split = getData().split(";");
                targetPlayerName = split[0];
                break;
        }
    }

    public String getTargetPlayerName() {
        return this.targetPlayerName;
    }
}
