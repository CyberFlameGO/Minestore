import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TestsSocketServerHandler {

    public static void main(String[] args) {
        try {
            new TestsSocketServerHandler(1804);
        } catch (IOException | JSONException exception) {
            exception.printStackTrace();
        }
    }

    private ServerSocket serverSocket;

    public TestsSocketServerHandler(int port) throws IOException, JSONException {
        this.serverSocket = new ServerSocket(port);
        System.out.println("Server is listening on port " + port);

        listen2();
    }

    private void listen2() throws IOException{
//        serverSocket.close();
        System.out.println("Server is closed");

        this.serverSocket = new ServerSocket(1804);
        System.out.println("Server is listening on port " + 1804);
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
}
