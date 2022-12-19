package ScriptServer;

import ScriptServer.packets.Packet;
import ScriptServer.packets.PacketDisconnect;
import ScriptServer.packets.PacketIds;
import ScriptServer.packets.PacketMessage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ScriptServer extends Thread {
    private final int port;
    private ServerSocket serverSocket;
    private CommandHandler commandHandler;
    private final HashMap<String, User> users = new HashMap<>();
    private final File CHATFILE = new File("chat.txt");

    public ScriptServer(int port) {
        this.port = port;
        this.commandHandler = new CommandHandler();
    }

    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            print("Chat ServerMain is listening on port " + port);
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                addUser(socket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Delivers a message from one user to others (broadcasting)
     */
    void broadcast(String message, User excludeUser) {
        for (Map.Entry<String, User> userPair : users.entrySet()) {
            if (userPair.getValue() != excludeUser) {
                sendPacket(userPair.getValue(), new PacketMessage(message));
            }
        }
    }

    String getChat() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(CHATFILE));
            while (bufferedReader.readLine() != null) {
                if (!bufferedReader.readLine().equals("null")) {
                    stringBuilder.append(bufferedReader.readLine());
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            print(e.getMessage());
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    void sendPacket(User user, Packet packet) {
        user.send(packet.encode());
    }

    void sendMessage(User user, String message) {
        sendPacket(user, new PacketMessage(message));
    }

    String getUserNames() {
        ArrayList<String> names = new ArrayList<>();
        for (Map.Entry<String, User> userPair : users.entrySet()) {
            String userName = userPair.getValue().getUserName();
            if (userName != null) {
                names.add(userName);
            }
        }
        return "Users: " + names;
    }

    int getUserCount() {
        return users.size();
    }

    void removeUserById(String id) {
        sendPacket(users.get(id), new PacketDisconnect());
        users.get(id).shutdown();
        broadcast(users.get(id).getUserName() + " quit", null);
        print("User disconnected: " + id);
        users.remove(id);
    }

    void removeUserById(String id, String reason) {
        sendPacket(users.get(id), new PacketDisconnect(reason));
        users.get(id).shutdown();
        broadcast(users.get(id).getUserName() + " quit", null);
        print("User disconnected: " + id);
        users.remove(id);
    }

    User getUserByName(String name) {
        User user = null;
        for (Map.Entry<String, User> userPair : users.entrySet()) {
            String userName = userPair.getValue().getUserName();
            if (userName.equals(name)) {
                user = userPair.getValue();
            }
        }
        return user;
    }

    boolean hasUserByName(String name) {
        return (getUserByName(name) != null);
    }

    void addUser(Socket socket) {

        String ip = String.valueOf(socket.getInetAddress()).replace("/", "");
        int port = socket.getPort();
        String id = ip + PacketIds.SEPARATOR + port;
        User newUser = new User(socket, this, id, Security.OPERATOR);
        print("New user connected: " + id);
        users.put(id, newUser);
        newUser.start();
    }

    public void removeUserByName(String name) {
        try {
            for (Map.Entry<String, User> userPair : users.entrySet()) {
                String userName = userPair.getValue().getUserName();
                if (Objects.equals(userName, name)) {
                    removeUserById(userPair.getKey());
                }
            }
        } catch (Exception e) {
            print(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void print(String message) {
        System.out.println(message);
    }

    public void shutdown() {
        for (String user : users.keySet()) {
            removeUserById(user, ": Server shutdown");
            try {
                serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            this.interrupt();
            System.exit(0);
        }
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }
}