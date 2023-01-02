import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SparkClient {
    private PrintWriter writer;
    private Socket socket;
    private ReadThread readThread;
    private String userName;


    public void start(String ip, int port, String name, String pw_hash) {
        try {
            socket = new Socket(ip, port);
            UI.print("Connected to the chat server on port " + port);
            readThread = new ReadThread(socket, this);
            readThread.start();
        } catch (UnknownHostException ex) {
            UI.print("ServerMain not found: " + ex.getMessage());
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
        try {
            sendPacket(new PacketConnect(name, pw_hash));
        } catch (Exception ex) {
            UI.print("Error sending connect Packet: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void shutdown(String reason) {
        UI.print("Disconnected: " + reason);
        try {
            sendPacket(new PacketDisconnect("Disconnect"));
            readThread.shutdown();
            writer.close();
            socket.close();
        } catch (IOException ex) {
            UI.print("Error disconnecting: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    void sendPacket(Packet packet) {
        send(packet.encode());

    }

    void send(String bytes) {
        writer.println(bytes);
    }

    public void prepareSendMessage(String message) {
        if (socket.isClosed()) {
            return;
        }
        if (message.isEmpty()) {
            return;
        }
        if (message.contains("~")) {
            return;
        }
        sendPacket(new PacketMessage(message, userName, 0));
        UI.messageArea.setText("");
    }

    public void setUserName(String userName) {
        this.userName = userName;
        UI.screen.setTitle(userName);
    }

    public String getUserName() {
        return userName;
    }
}