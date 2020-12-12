package com.lielamar.minestore.shared.network;

import com.lielamar.minestore.shared.network.requests.Request;

import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public abstract class RequestHandler {

    private final Set<Request> requests;

    public RequestHandler() {
        this.requests = new HashSet<>();
    }

    public void addRequest(Request request) {
        this.requests.add(request);
    }

    public void removeRequest(Request request) {
        this.requests.remove(request);
    }

    public Set<Request> getRequests() {
        return this.requests;
    }

    public abstract Request getRequestById(Socket socket, int protocolVersion, int requestId, String data);
}
