package ScriptServer;

import ScriptServer.packets.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.ConcurrentModificationException;

public class User extends Thread {

    private final ScriptServer server;
    private final Socket socket;
    private final String id;
    private PrintWriter writer;
    private BufferedReader reader;
    private String userName = null;
    private int securityLevel;
    private String disconnectReason = null;

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
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            String response;
            do {
                response = reader.readLine();
            } while (response == null);
            System.out.println(response);
            String[] connectPacket = response.split(PacketIds.SEPARATOR);
            if (!connectPacket[0].equals(Integer.toString(PacketIds.CONNECT))) {
                server.removeUserById(this.getUserId(), "Invalid connect!");
                return;
            }
            if (connectPacket.length == 1) {
                server.removeUserById(this.getUserId(), "Invalid name!");
                return;
            }
            String name = connectPacket[1].toLowerCase();
            if (name.length() > Security.NAME_MAX_LENGTH) {
                server.removeUserById(this.getUserId(), "Name to long!");
                return;
            }
            if (server.hasUserByName(name)) {
                server.removeUserById(this.getUserId(), "Name is occupied!");
                return;
            }
            if (Security.nameDenied(name)) {
                server.removeUserById(this.getUserId(), "Name is blocked!");
                return;
            }
            int level = Security.MEMBER;
            if (connectPacket.length > 2) {
                level = Security.switchLevel(this, connectPacket[2]);
            }
            setUserName(name);
            setSecurityLevel(level);
            sendLog("Welcome " + userName + ", " + server.getUserCount() + " people are online");
            server.broadcast(new PacketLog("New user connected: " + userName), this);
            loop:
            while (!socket.isClosed()) {
                if (!reader.ready() || !socket.isConnected()) {
                    continue;
                }
                response = reader.readLine();
                ScriptServer.print(response);
                if (response == null) {
                    ScriptServer.print("Response was null");
                    continue;
                }
                String[] packet = response.split(PacketIds.SEPARATOR);
                if (Security.isInvalidInt(packet[0])) {
                    ScriptServer.print("Invalid packet ID: " + packet[0]);
                    continue;
                }
                int packetID = Integer.parseInt(packet[0]);
                switch (packetID) {
                    case PacketIds.MESSAGE:
                        PacketMessage packetMessage = new PacketMessage(packet);
                        if (packetMessage.MESSAGE == null || packetMessage.MESSAGE.isEmpty()) {
                            break;
                        }
                        if (packetMessage.MESSAGE.startsWith("/")) {
                            String[] commands = packetMessage.MESSAGE.split(" ");
                            String command = commands[0].toLowerCase().substring(1);
                            String[] args = Arrays.copyOfRange(commands, 1, commands.length);
                            CommandHandler.handleCommand(this, command, args);
                            break;
                        }
                        if (!Security.hasPermission(this, Security.MEMBER)) {
                            continue;
                        }
                        server.broadcast(packetMessage.MESSAGE, null, userName);
                        break;
                    case PacketIds.DISCONNECT:
                        PacketDisconnect packetDisconnect = new PacketDisconnect(packet);
                        disconnectReason = packetDisconnect.REASON;
                        break loop;
                }
            }
            if (!socket.isClosed()) {
                server.removeUserById(id, disconnectReason);
            }

        } catch (IOException | ConcurrentModificationException ex) {
            ScriptServer.print("Error in User: " + ex.getMessage());
            ex.printStackTrace();
        }
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
            ScriptServer.print("Error in shutdown user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    void send(String bytes) {
        writer.println(bytes);
    }

    public void sendPacket(Packet packet) {
        send(packet.encode());
    }

    public void sendMessage(String message, String sender) {
        String username = sender.isEmpty() ? "System" : sender;
        sendPacket(new PacketMessage(message, username, System.currentTimeMillis()));
    }

    public void sendLog(String log) {
        sendPacket(new PacketLog(log));
    }

    public String getUserId() {
        return id;
    }

    public ScriptServer getServer() {
        return server;
    }

    public int getSecurityLevel() {
        return securityLevel;
    }

    public void setSecurityLevel(int securityLevel) {
        this.securityLevel = securityLevel;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        sendPacket(new PacketName(userName));
    }
}