package com.lielamar.minestore.shared.handlers.requests;

import com.lielamar.minestore.shared.modules.MinestorePlugin;
import org.json.JSONObject;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.Socket;

public abstract class Request {

    private final MinestorePlugin plugin;
    private final Socket socket;
    private final int protocolVersion;
    private final int requestId;
    private final JSONObject data;

    public Request(@Nonnull MinestorePlugin plugin, @Nonnull Socket socket, @Nonnull int protocolVersion, @Nonnull int requestId, @Nonnull JSONObject data) {
        this.plugin = plugin;
        this.socket = socket;
        this.protocolVersion = protocolVersion;
        this.requestId = requestId;
        this.data = data;
    }

    public void closeRequest() {
        try {
            getPlugin().getRequestHandler().removeRequest(this);
            getSocket().close();
        } catch(IOException exception) {
            exception.printStackTrace();
        }
    }

    public MinestorePlugin getPlugin() {
        return this.plugin;
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

    public JSONObject getData() {
        return this.data;
    }

    public abstract void loadRequestByVersion();
    public abstract void runRequest();
}
