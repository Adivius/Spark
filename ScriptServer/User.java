import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;

public class User extends Thread {

    private PrintWriter writer;
    private String userName;
    private final ChatServer server;
    private final Socket socket;
    private final String id;

    public User(Socket socket, ChatServer server, String id) {
        this.socket = socket;
        this.server = server;
        this.id = id;
    }

    public void run() {
        if (socket.isClosed()) {
            return;
        }
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
            printUsers();
            writer.println("Please enter a name: ");
            String name;
            while ((name = reader.readLine()) == null);
            userName = name;
            writer.println("Welcome " + name);

            String serverMessage = "New user connected: " + userName;
            server.broadcast(serverMessage, this);
            String clientMessage;
            while (!socket.isClosed() && socket.isConnected()){
                clientMessage = reader.readLine();
                if (clientMessage.equals("bye")){
                    break;
                }
                serverMessage = "[" + userName + "]: " + clientMessage;
                server.broadcast(serverMessage, null);
            }
            socket.close();
            server.removeUser(id);

        } catch (IOException ex) {
            System.out.println("Error in User: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    String getUserName() {
        return userName;
    }

    /**
     * Sends a message to the client.
     */
    void sendMessage(String message) {
        writer.println(message);
    }

    void printUsers(){
        writer.println("Users: " + server.getUserNames().toString());
    }
}