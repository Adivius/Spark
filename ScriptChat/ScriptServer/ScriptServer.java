package ScriptServer;

import ScriptServer.packets.Packet;
import ScriptServer.packets.PacketDisconnect;
import ScriptServer.packets.PacketMessage;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScriptServer {
    private final int port;
    private final HashMap<String, User> users = new HashMap<>();

    public ScriptServer(int port) {
        this.port = port;
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
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
        sendPacket(users.get(id), new PacketDisconnect());
        broadcast(users.get(id).getUserName() + " quit", null);
        users.get(id).interrupt();
        //users.remove(id);
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

    public static void print(String message){
        System.out.println(message);
    }

}