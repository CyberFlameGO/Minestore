import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.*;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;

public class TestsSocketServerHandler {

    public static void main(String[] args) {
        try {
//            KeyStore key = KeyMaster.getFromPath(".\\cssl.pfx", "PKCS12", "123");
//            SSLServerSocketFactory fac = KeyMaster.getSSLServerSocketFactory(key, "TLS");
//            listener = KeyMaster.getSSLServerSocket(fac, 49015);

//            System.setProperties(javax.net.ssl.keyStore ssl.key);
//            System.setPropertiesjavax.net.ssl.keyStorePassword P@ssw0rd!);

            System.setProperty("javax.net.ssl.trustStore", "za.store");
            System.setProperty("javax.net.ssl.keyStorePassword", "123456");
            System.setProperty("javax.net.ssl.keyStore", "D:\\Workspace\\Minestore\\Minestore Plugin\\src\\main\\resources\\key.keystore");

            new TestsSocketServerHandler(1804);
        } catch (IOException | JSONException exception) {
            exception.printStackTrace();
        }
    }

    private SSLServerSocket serverSocket;

    public TestsSocketServerHandler(int port) throws IOException, JSONException {
//        this.serverSocket = new ServerSocket(port);
        SSLServerSocketFactory sslssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        this.serverSocket = (SSLServerSocket) sslssf.createServerSocket(port,15);
        System.out.println("Server is listening on port " + port);

        listen();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void listen() throws IOException, JSONException {
        while(true) {
            Socket socket = serverSocket.accept();
            System.out.println("DEBUG: Connection established");

            // Reading the request
            InputStream inputStream = socket.getInputStream();
            JSONObject request = readRequest(inputStream);

            System.out.println("CLIENT: " + request.toString());
            System.out.println("DEBUG: " + request.getInt("protocol_version"));

            for(int i = 0; i < 5; i++) {
                try {
                    System.out.println("i: " + i);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            int protocolVersion = request.getInt("protocol_version");
            int requestId = request.getInt("request_id");
            if(requestId == 0) {
                JSONObject response = new JSONObject();
                response.put("protocol_version", protocolVersion);
                response.put("request_id", requestId);

                JSONObject response_data = new JSONObject();
                if(request.getJSONObject("data").getString("player_name").equalsIgnoreCase("LielAmar"))
                    response_data.put("authenticated", true);
                else
                    response_data.put("authenticated", false);
                response.put("data", response_data);

                // Sending a response
                OutputStream outputStream = socket.getOutputStream();
                sendResponse(outputStream, response);

                System.out.println("SERVER: " + response.toString());
            } else if(requestId == 1) {
                System.out.println("SERVER: Received a request with id 1!");
            }

            // Closing the socket
            socket.close();
        }
    }

    public JSONObject readRequest(InputStream stream) throws IOException, JSONException {
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();

        String line;
        while((line = br.readLine()) != null)
            sb.append(line).append("\n");

        return new JSONObject(sb.toString());
    }

    public void sendResponse(OutputStream stream, JSONObject response) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(stream);
        bos.write(response.toString().getBytes(StandardCharsets.UTF_8));

        bos.flush();
    }



    public static class KeyMaster {
        public static SSLSocketFactory getSSLSocketFactory(KeyStore trustKey, String sslAlgorithm) {
            try {
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                tmf.init(trustKey);

                SSLContext context = SSLContext.getInstance(sslAlgorithm);//"SSL" "TLS"
                context.init(null, tmf.getTrustManagers(), null);

                return context.getSocketFactory();
            } catch (Exception e) {
                System.out.println("Err: getSSLSocketFactory(), ");
            }

            return null;
        }

        public static SSLServerSocketFactory getSSLServerSocketFactory(KeyStore trustKey, String sslAlgorithm) {
            try {
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                tmf.init(trustKey);

                SSLContext context = SSLContext.getInstance(sslAlgorithm);//"SSL" "TLS"
                context.init(null, tmf.getTrustManagers(), null);

                return context.getServerSocketFactory();
            } catch (Exception e) {
                System.out.println("Err: getSSLSocketFactory(), ");
            }

            return null;
        }

        public static SSLServerSocket getSSLServerSocket(SSLServerSocketFactory socketFactory, int port) {
            try {
                return (SSLServerSocket) socketFactory.createServerSocket(port);
            } catch (Exception e) {
                System.out.println("Err: getSSLSocket(), ");
            }

            return null;
        }

        public static KeyStore getFromPath(String path, String algorithm, String filePassword)//PKSC12
        {
            try {
                File f = new File(path);

                if (!f.exists())
                    throw new RuntimeException("Err: File not found.");

                FileInputStream keyFile = new FileInputStream(f);
                KeyStore keystore = KeyStore.getInstance(algorithm);
                keystore.load(keyFile, filePassword.toCharArray());
                keyFile.close();

                return keystore;
            } catch (Exception e) {
                System.out.println("Err: getFromPath(), " + e.toString());
            }

            return null;
        }
    }
}
