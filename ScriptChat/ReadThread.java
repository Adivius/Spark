import java.io.*;
import java.net.*;

public class ReadThread extends Thread {
    private BufferedReader reader;
    private final ChatClient client;
    private boolean connected = false;

    public ReadThread(Socket socket, ChatClient client) {
        this.client = client;
        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException ex) {
            UI.print("Error getting input stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void run() {
        setConnected(true);
        while (connected) {
            try {
                String response = reader.readLine();
                UI.print("\n" + response);

                // prints the username after displaying the server's message
                if (client.getUserName() != null) {
                }
            } catch (IOException ex) {
                UI.print("Error reading from server: " + ex.getMessage());
                ex.printStackTrace();
                break;
            }
        }

        try {
            reader.close();
        } catch (IOException ex) {
            UI.print("Error disconnecting " + ex.getMessage());
            ex.printStackTrace();
        }

    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public boolean isConnected() {
        return connected;
    }
}