package com.lielamar.minestore.shared.network.requests;

import java.net.Socket;

public abstract class Request {

    private final Socket socket;
    private final int protocolVersion;
    private final int requestId;
    private final String data;

    public Request(Socket socket, int protocolVersion, int requestId, String data) {
        this.socket = socket;
        this.protocolVersion = protocolVersion;
        this.requestId = requestId;
        this.data = data;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public int getProtocolVersion() {
        return this.protocolVersion;
    }

    public int getRequestId() {
        return this.requestId;
    }

    public String getData() {
        return this.data;
    }


    public abstract void loadRequestByVersion();
    public abstract void runRequest();
}
