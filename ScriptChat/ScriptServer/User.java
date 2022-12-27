package ScriptServer;

import ScriptServer.packets.PacketDisconnect;
import ScriptServer.packets.PacketIds;
import ScriptServer.packets.PacketLog;
import ScriptServer.packets.PacketMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.ConcurrentModificationException;

public class User extends Thread {

    private PrintWriter writer;
    private BufferedReader reader;
    private String userName;
    private final ScriptServer server;
    private final Socket socket;
    private final String id;
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
            String[] connectPacket = response.split(PacketIds.SEPARATOR);
            if (!connectPacket[0].equals(Integer.toString(PacketIds.CONNECT))) {
                server.removeUserById(this.getUserId(), ": Invalid connect!");
                return;
            }
            if (connectPacket.length == 1) {
                server.removeUserById(this.getUserId(), ": Invalid name!");
                return;
            }
            String name = connectPacket[1].toLowerCase();
            if (server.hasUserByName(name)) {
                server.removeUserById(this.getUserId(), ": Invalid name!");
                return;
            }
            if (!Security.nameAllowed(name)) {
                server.removeUserById(this.getUserId(), ": Invalid name!");
                return;
            }
            int level = Security.MEMBER;
            if (connectPacket.length > 2) {
                level = Security.switchLevel(this, connectPacket[2]);
            }
            setUserName(name);
            setSecurityLevel(level);
            server.sendPacket(this, new PacketLog("Welcome " + userName + ", " + server.getUserCount() + " people are online"));
            server.broadcast(new PacketLog("New user connected: " + userName), this);
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
                        if (packetMessage.MESSAGE == null || packetMessage.MESSAGE.isEmpty()) {
                            break;
                        }
                        if (packetMessage.MESSAGE.startsWith("/")) {
                            String[] commands = packetMessage.MESSAGE.split(" ");
                            String command = commands[0].toLowerCase().substring(1);
                            String[] args = Arrays.copyOfRange(commands, 1, commands.length);
                            CommandHandler.handleCommand(this, command, server, args);
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

    public String getUserName() {
        return userName;
    }

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

    public void setUserName(String userName) {
        this.userName = userName;
    }
}