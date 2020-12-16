package com.lielamar.minestore.shared.handlers;

import com.lielamar.minestore.shared.encryption.AES;
import com.lielamar.minestore.shared.encryption.EncryptionKey;
import com.lielamar.minestore.shared.handlers.requests.Request;
import com.lielamar.minestore.shared.modules.MinestorePlugin;
import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.Nullable;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public abstract class RequestHandler {

    protected final MinestorePlugin plugin;
    protected final Set<Request> requests;

    public RequestHandler(MinestorePlugin plugin) {
        this.plugin = plugin;
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


    /**
     * Reads a request, adding it to the list and getting the matching request object
     *
     * @param socket         Socket the read the request from
     * @throws IOException   IOException if something goes wrong when reading the stream
     */
    public void readRequest(Socket socket) throws IOException {
        // Reading the request
        JSONObject request = readJSONFromSocket(socket, plugin.getEncryptionKey());

        if(request != null) {
            int protocol_version = request.getInt("protocol_version");
            int request_id = request.getInt("request_id");
            JSONObject data = request.getJSONObject("data");

            Request requestObject = getRequestById(socket, protocol_version, request_id, data);
            if(requestObject != null) {
                addRequest(requestObject);
                requestObject.runRequest();
            }
        }
    }

    /**
     * Reads an InputStream and returns a JSON Object
     *
     * @param socket           Socket to get the stream of and read the message message from
     * @param key              Encryption key to use to decrypt the message
     * @return                 Created JSON Object
     * @throws IOException     IOException if something goes wrong when reading the stream
     * @throws JSONException   JSONException if the method couldn't create the JSON Object
     */
    @Nullable
    public static JSONObject readJSONFromSocket(Socket socket, @Nullable EncryptionKey key) throws IOException, JSONException {
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        StringBuilder message = new StringBuilder();

        String line;
        while((line = br.readLine()) != null)
            message.append(line).append("\n");

        if(key == null)
            return new JSONObject(message.toString());
        else {
//            String decrypted = AES.decrypt(message.toString(), key.getKey());
            String decrypted = message.toString();
//            if(decrypted != null)
            return new JSONObject(decrypted);
//            return null;
        }
    }

    /**
     * Sends a JSONObject to an output stream
     *
     * @param socket           Socket to get the stream of and send the message message to
     * @param key              Encryption key to use to encrypt the message
     * @param response         Response to send to the stream
     * @throws IOException     IOException if something goes wrong when reading the stream
     */
    public static void sendJSONToSocket(Socket socket, @Nullable EncryptionKey key, @Nullable JSONObject response) throws IOException {
        if(response == null) return;

        BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());

        if(key == null) {
            bos.write(response.toString().getBytes(StandardCharsets.UTF_8));
        } else {
//            String encrypted = AES.encrypt(response.toString(), key.getKey());
            String encrypted = response.toString();
            if(encrypted != null)
                bos.write(encrypted.getBytes(StandardCharsets.UTF_8));
        }

        bos.flush();
    }


    @Nullable
    protected abstract Request getRequestById(Socket socket, int protocolVersion, int requestId, JSONObject data);
}
