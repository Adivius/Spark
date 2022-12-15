import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient {
    private final String ip;
    private PrintWriter writer;
    private Socket socket;
    private final int port;
    private String userName;
    private ReadThread readThread;

    public ChatClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void start() {
        try {
            socket = new Socket(ip, port);
            UI.print("Connected to the chat server on port " + port);

            readThread = new ReadThread(socket, this);
            readThread.start();

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

    public ReadThread getReadThread() {
        return readThread;
    }

    public PrintWriter getWriter() {
        return writer;
    }
}