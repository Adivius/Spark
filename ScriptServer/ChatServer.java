import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ChatServer {
    private final int port;
    private final HashMap<String, User> users = new HashMap<>();
    boolean running = false;
    private ServerSocket serverSocket;

    public ChatServer(int port) {
        this.port = port;
    }

    public void start(ChatServer server) {
        running = true;
        try {
            serverSocket = new ServerSocket(port);
            Server.print("Chat Server is listening on port " + port);
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                String ip = String.valueOf(socket.getInetAddress()).replace("/", "");
                int port = socket.getPort();
                String id = ip + Consts.SEPERATOR + port;
                Server.print("New user connected: " + id);
                User newUser = new User(socket, server, id);
                users.put(id, newUser);
                newUser.start();
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
                userPair.getValue().sendMessage(message);
            }
        }
    }

    void shutdown() {
        for(Map.Entry<String, User> userPair: users.entrySet()){
            userPair.getValue().interrupt();
            users.remove(userPair.getKey());
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        running = false;
        Server.print("Server closed");
    }

    String[] getUserNames() {
        String[] names = new String[users.size()];
        int i = 0;
        for(Map.Entry<String, User> userPair: users.entrySet()){
            names[i] = userPair.getValue().getUserName();
            i++;
        }
        return  names;
    }

    /**
     * Returns true if there are other users connected (not count the currently connected user)
     */
    boolean hasUsers() {
        return !this.users.isEmpty();
    }

    void removeUser(String  id){
        users.remove(id);
    }

}