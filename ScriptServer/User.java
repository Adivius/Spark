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
            writer.write("Please enter a name: ");
            printUsers();
            String name = "";
            while ((name = reader.readLine()) == null){

            }
            userName = name;

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

            server.removeUser(id);
            socket.close();

            serverMessage = userName + " has quitted.";
            server.broadcast(serverMessage, this);

        } catch (IOException ex) {
            System.out.println("Error in User: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Sends a message to the client.
     */
    void sendMessage(String message) {
        writer.println(message);
    }

    void printUsers(){
        writer.println("Users: " + Arrays.toString(server.getUserNames()));
    }
}