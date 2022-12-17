package ScriptServer;

import ScriptServer.packets.PacketIds;
import ScriptServer.packets.PacketMessage;

import java.io.*;
import java.net.Socket;

public class User extends Thread {

    private PrintWriter writer;
    private String userName;
    private final ScriptServer server;
    private final Socket socket;
    private final String id;

    public User(Socket socket, ScriptServer server, String id) {
        this.socket = socket;
        this.server = server;
        this.id = id;
    }

    public void run() {
        if (socket.isClosed()) {
            return;
        }
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
            server.sendMessage(this, server.getUserNames());

            server.sendMessage(this, "Please enter a name: ");
            String name;
            while ((name = reader.readLine()) == null);
            userName = name.split(PacketIds.SEPARATOR)[1];
            server.sendMessage(this, "Welcome " + userName);

            String serverMessage = "New user connected: " + userName;
            server.broadcast(serverMessage, this);
            String response;
            while (!socket.isClosed() && socket.isConnected()){
                response = reader.readLine();
                System.out.println(response);
                String[] packet = response.split(PacketIds.SEPARATOR);
                int packetID = Integer.parseInt(packet[0]);
                switch (packetID){
                    case PacketIds.MESSAGE:
                        PacketMessage packetMessage = new PacketMessage(packet);
                        String message = "[" + userName + "]: " + packetMessage.MESSAGE;
                        server.broadcast(message, null);
                        break;
                    case PacketIds.DISCONNECT:
                        socket.close();
                        server.removeUser(id);
                        break;
                }
            }
            socket.close();
            server.removeUser(id);

        } catch (IOException ex) {
            System.out.println("Error in User: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    String getUserName() {
        return userName;
    }

    /**
     * Sends a message to the client.
     */
    void send(String bytes) {
        writer.println(bytes);
    }
}