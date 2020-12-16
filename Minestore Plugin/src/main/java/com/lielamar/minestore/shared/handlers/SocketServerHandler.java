package com.lielamar.minestore.shared.handlers;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class SocketServerHandler {

    private final int port;
    private final RequestHandler requestHandler;

    private ServerSocket serverSocket;

    public SocketServerHandler(int port, RequestHandler requestHandler) {
        this.port = port;
        this.requestHandler = requestHandler;

        new Thread(() -> {
            try {
                listen();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }).start();
    }

    private void listen() throws IOException {
        this.serverSocket = new ServerSocket(port);

        System.out.println("[Minestore Debug] Running ServerSocket on port " + port);

        while(true) {
            if(this.serverSocket.isClosed()) {
                System.out.println("[Minestore Debug] Disabling the ServerSocket on port " + port);
                break;
            }

            try {
                Socket socket = serverSocket.accept();
                System.out.println("[Minestore Debug] Connection with client established!");

                requestHandler.readRequest(socket);
            } catch(SocketException exception) {
                System.out.println("=========================================================================================================");
                System.out.println("[Minestore] Minestore detected a potential reload happening on your server.");
                System.out.println("[Minestore] If you did use /reload please read this message carefully.");
                System.out.println("[Minestore] It is recommended to avoid using /Reload but rather to close the server completely everytime.");
                System.out.println("[Minestore] If not done, the plugin might break!");
                System.out.println("=========================================================================================================");
            }
        }
    }

    public ServerSocket getServerSocket() {
        return this.serverSocket;
    }
}
