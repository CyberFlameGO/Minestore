package com.lielamar.minestore.shared.handlers;

import com.lielamar.minestore.shared.handlers.requests.Request;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServerHandler {

    private final ServerSocket serverSocket;
    private final RequestHandler requestHandler;

    public SocketServerHandler(int port, RequestHandler requestHandler) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.requestHandler = requestHandler;

        listen();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void listen() throws IOException {
        while(true) {
            Socket socket = serverSocket.accept();
            System.out.println("connection with client established!");

            requestHandler.readRequest(socket);
        }
    }

    public void destroy() throws IOException {
        serverSocket.close();

        for(Request request : requestHandler.getRequests())
            request.getSocket().close();
    }
}
