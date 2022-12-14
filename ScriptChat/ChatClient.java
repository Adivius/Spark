import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient {
    private String hostname;
    public static PrintWriter writer;
    private Socket socket;
    private int port;
    private String userName;

    public ChatClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public void start() {
        try {
            socket = new Socket(hostname, port);

            UI.print("Connected to the chat server on port " + port);

            new ReadThread(socket, this).start();
            //new WriteThread(socket, this).start();

        } catch (UnknownHostException ex) {
            UI.print("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            UI.print("I/O Error: " + ex.getMessage());
        }

        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            UI.print("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }

    }

    void setUserName(String userName) {
        this.userName = userName;
    }

    String getUserName() {
        return this.userName;
    }
}