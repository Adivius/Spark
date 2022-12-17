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
    private final ScriptServerThread server;
    private final Socket socket;
    private final String id;

    public User(Socket socket, ScriptServerThread server, String id) {
        this.socket = socket;
        this.server = server;
        this.id = id;
    }

    public void run() {
        if (socket.isClosed()) {
            return;
        }
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            String response;
            do {
                response = reader.readLine();
            } while (response == null);
            userName = response.split(PacketIds.SEPARATOR)[1];
            ScriptServerThread.print(response);
            server.sendMessage(this, "Welcome " + userName);
            server.broadcast("New user connected: " + userName, this);
            loop:
            while (!socket.isClosed() && socket.isConnected()) {
                response = reader.readLine();
                ScriptServerThread.print(response);
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
                        String command = commands[0];
                        String[] args = Arrays.copyOfRange(commands, 1, commands.length);
                        handleCommand(command, args);
                        break;
                }
            }
            server.removeUser(id);

        } catch (IOException ex) {
            ScriptServerThread.print("Error in User: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    void handleCommand(String command, String[] args) {
        switch (command) {
            case "listUser":
                server.sendMessage(this, server.getUserNames());
                break;
            case "bye":
                server.removeUser(id);
                break;
            case "kick":
                server.removeUserByName(args[0]);
                break;
            case "stop":
                server.shutdown();
                break;
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
        try {
            if (!socket.isClosed()) {
                socket.close();
            }
            reader.close();
            writer.close();
            this.interrupt();
        } catch (IOException e) {
            ScriptServerThread.print("Error in shutdown user: " + e.getMessage());
            e.printStackTrace();
        }
    }
}