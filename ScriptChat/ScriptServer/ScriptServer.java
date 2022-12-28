package ScriptServer;

import ScriptServer.packets.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScriptServer extends Thread {

    private final int port;
    private ServerSocket serverSocket;
    private final HashMap<String, User> users = new HashMap<>();

    public ScriptServer(int port) {
        this.port = port;
    }

    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            CommandHandler.init();
            print("Chat ServerMain is listening on port " + port);
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                addUser(socket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void broadcast(String message, User excludeUser, String sender) {
        for (Map.Entry<String, User> userPair : users.entrySet()) {
            if (userPair.getValue() != excludeUser) {
                userPair.getValue().sendMessage(message, sender);
            }
        }
    }

    public void broadcast(Packet packet, User excludeUser) {
        for (Map.Entry<String, User> userPair : users.entrySet()) {
            if (userPair.getValue() != excludeUser) {
                userPair.getValue().sendPacket(packet);
            }
        }
    }



    public String getUserNames() {
        ArrayList<String> names = new ArrayList<>();
        for (Map.Entry<String, User> userPair : users.entrySet()) {
            String userName = userPair.getValue().getUserName();
            if (userName != null) {
                names.add(userName);
            }
        }
        return names.size() + " Users: " + names;
    }

    int getUserCount() {
        return users.size();
    }

    public void removeUserById(String id, String reason) {
        User removeUser = users.get(id);
        if (removeUser.getUserName() != null){
            broadcast(new PacketLog(removeUser.getUserName() + " quit, " + (getUserCount() - 1) + " people are online"), removeUser);
        }
        removeUser.sendPacket(new PacketDisconnect(reason));
        removeUser.shutdown();
        print("User disconnected: " + id + reason);
        users.remove(id);
    }

    public User getUserByName(String name) {
        User user = null;
        for (Map.Entry<String, User> userPair : users.entrySet()) {
            String userName = userPair.getValue().getUserName();
            if (userName == null) {
                continue;
            }
            if (userName.equals(name)) {
                user = userPair.getValue();
            }
        }
        return user;
    }

    public boolean hasUserByName(String name) {
        return (getUserByName(name) != null);
    }

    void addUser(Socket socket) {

        String ip = String.valueOf(socket.getInetAddress()).replace("/", "");
        int port = socket.getPort();
        String id = ip + PacketIds.SEPARATOR + port;
        User newUser = new User(socket, this, id, Security.VISITOR);
        print("New user connected: " + id);
        users.put(id, newUser);
        newUser.start();
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

    public HashMap<String, User> getUsers() {
        return users;
    }
}