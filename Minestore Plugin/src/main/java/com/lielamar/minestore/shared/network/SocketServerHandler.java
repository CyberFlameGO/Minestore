package com.lielamar.minestore.shared.network;

import com.lielamar.minestore.shared.encryption.AES;
import com.lielamar.minestore.shared.encryption.EncryptionKey;
import com.lielamar.minestore.shared.network.requests.Request;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServerHandler {

    private final ServerSocket serverSocket;
    private final RequestHandler requestHandler;
    private final EncryptionKey encryptionKey;

    public SocketServerHandler(int port, RequestHandler requestHandler, EncryptionKey encryptionKey) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.requestHandler = requestHandler;
        this.encryptionKey = encryptionKey;

        listen();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void listen() throws IOException {
        while(true) {
            Socket socket = serverSocket.accept();
            System.out.println("connection with client established!");

            DataInputStream in = new DataInputStream(socket.getInputStream());
            int protocolVersion = in.readInt();
            int requestId = in.readInt();
            String data = AES.decrypt(in.readUTF(), this.encryptionKey.getKey());
            in.close();

            Request request = requestHandler.getRequestById(socket, protocolVersion, requestId, data);
            requestHandler.addRequest(request);
            request.runRequest();
        }
    }

    public void destroy() throws IOException {
        serverSocket.close();

        for(Request request : requestHandler.getRequests())
            request.getSocket().close();
    }
}
