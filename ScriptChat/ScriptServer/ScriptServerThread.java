package ScriptServer;

import ScriptServer.packets.Packet;
import ScriptServer.packets.PacketMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ScriptServerThread extends Thread {
    private final int port;
    private ServerSocket serverSocket;
    private final HashMap<String, User> users = new HashMap<>();

    public ScriptServerThread(int port) {
        this.port = port;
    }

    public void run(){
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
        for(Map.Entry<String, User> userPair: users.entrySet()){
            if (userPair.getValue() != excludeUser){
                sendPacket(userPair.getValue(), new PacketMessage(message));
            }
        }
    }

    void sendPacket(User user, Packet packet){
        user.send(packet.encode());
    }

    void sendMessage(User user, String message){
        sendPacket(user, new PacketMessage(message));
    }

    String getUserNames(){
        ArrayList<String> names = new ArrayList<>();
        for(Map.Entry<String, User> userPair: users.entrySet()){
            String userName = userPair.getValue().getUserName();
            if (userName != null){
                names.add(userName);
            }
        }
        return "Users: " + names;
    }

    void removeUser(String  id){
        users.get(id).shutdown();
        broadcast(users.get(id).getUserName() + " quit", null);
        print("User disconnected: " + id);
        users.remove(id);
        System.out.println(users);
    }

    void addUser(Socket socket){

        String ip = String.valueOf(socket.getInetAddress()).replace("/", "");
        int port = socket.getPort();
        String id = ip + Consts.SEPARATOR + port;
        User newUser = new User(socket, this, id);
        print("New user connected: " + id);
        users.put(id, newUser);
        newUser.start();
    }

    public void removeUserByName(String name){
        for(Map.Entry<String, User> userPair: users.entrySet()){
            String userName = userPair.getValue().getUserName();
            if (Objects.equals(userName, name)){
                removeUser(userPair.getKey());
            }
        }
    }

    public static void print(String message){
        System.out.println(message);
    }

    public void shutdown(){
        for(Map.Entry<String, User> userPair: users.entrySet()){
            userPair.getValue().shutdown();
            try {
                serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            this.interrupt();
            System.exit(0);
        }
    }

}