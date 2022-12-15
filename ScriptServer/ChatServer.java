import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatServer {
    private final int port;
    private final HashMap<String, User> users = new HashMap<>();
    private ServerSocket serverSocket;

    public ChatServer(int port) {
        this.port = port;
    }

    public void start(ChatServer server) {
        try {
            serverSocket = new ServerSocket(port);
            print("Chat Server is listening on port " + port);
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                addUser(socket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void addUser(Socket socket){
        String ip = String.valueOf(socket.getInetAddress()).replace("/", "");
        int port = socket.getPort();
        String id = ip + Consts.SEPERATOR + port;
        User newUser = new User(socket, this, id);
        print("New user connected: " + id);
        users.put(id, newUser);
        newUser.start();
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

    ArrayList<String> getUserNames(){
        ArrayList<String> names = new ArrayList<>();
        for(Map.Entry<String, User> userPair: users.entrySet()){
            String userName = userPair.getValue().getUserName();
            if (userName != null){
                names.add(userName);
            }
        }
        return names;
    }

    void removeUser(String  id){
        broadcast(users.get(id).getUserName() + " quit", null);
        users.get(id).interrupt();
        users.remove(id);
    }
    public static void print(String message){
        System.out.println(message);
    }

}