package ScriptServer;

import ScriptServer.packets.PacketCommand;
import ScriptServer.packets.PacketIds;
import ScriptServer.packets.PacketMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

public class User extends Thread {

    private PrintWriter writer;
    private BufferedReader reader;
    private String userName;
    private final ScriptServer server;
    private final Socket socket;
    private final String id;
    private int securityLevel;
    private boolean running;

    public User(Socket socket, ScriptServer server, String id, int securityLevel) {
        this.socket = socket;
        this.server = server;
        this.id = id;
        this.securityLevel = securityLevel;
    }

    public void run() {
        if (socket.isClosed()) {
            return;
        }
        running = true;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            String response;
            do {
                response = reader.readLine();
            } while (response == null);
            userName = response.split(PacketIds.SEPARATOR)[1];
            ScriptServer.print(response);
            server.sendMessage(this, "Welcome " + userName + ", " + server.getUserCount() + " people are online");
            server.broadcast("New user connected: " + userName, this);
            loop:
            while (!socket.isClosed()) {
                if (!reader.ready() || !socket.isConnected()) {
                    continue;
                }
                response = reader.readLine();
                ScriptServer.print(response);
                String[] packet = response.split(PacketIds.SEPARATOR);
                int packetID = Integer.parseInt(packet[0]);
                switch (packetID) {
                    case PacketIds.MESSAGE:
                        PacketMessage packetMessage = new PacketMessage(packet);
                        if (packetMessage.MESSAGE == null) {
                            break;
                        }
                        String message = "[" + userName + "]: " + packetMessage.MESSAGE;
                        server.broadcast(message, null);
                        break;
                    case PacketIds.DISCONNECT:
                        break loop;
                    case PacketIds.COMMAND:
                        PacketCommand packetCommand = new PacketCommand(packet);
                        String[] commands = packetCommand.COMMAND.split(" ");
                        String command = commands[0].toLowerCase();
                        String[] args = Arrays.copyOfRange(commands, 1, commands.length);
                        server.getCommandHandler().handleCommand(this, command, server, args);
                        break;
                }
            }
            if (!socket.isClosed()) {
                server.removeUserById(id);
            }

        } catch (IOException ex) {
            ScriptServer.print("Error in User: " + ex.getMessage());
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

    public void shutdown() {
        running = false;
        try {
            if (!socket.isClosed()) {
                socket.close();
            }
            reader.close();
            writer.close();
            this.interrupt();
        } catch (IOException e) {
            ScriptServer.print("Error in shutdown user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String getUserId() {
        return id;
    }

    public int getSecurityLevel() {
        return securityLevel;
    }

    public ScriptServer getServer() {
        return server;
    }

    public void setSecurityLevel(int securityLevel) {
        this.securityLevel = securityLevel;
    }
}