import ScriptServer.packets.Packet;
import ScriptServer.packets.PacketMessage;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ScriptClient {
    private final String ip;
    private PrintWriter writer;
    private Socket socket;
    private final int port;
    private ReadThread readThread;

    public ScriptClient(String ip, int port) {
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

    }

    void end(){
        UI.print("Disconnected");
        try {
            writer.close();
            socket.close();
            Thread.sleep(2000);
        } catch (IOException | InterruptedException ex) {
            UI.print("Error disconnecting: " + ex.getMessage());
            ex.printStackTrace();
        }
        System.exit(0);
    }

    void sendPacket(Packet packet){
        send(packet.encode());
    }

    void send(String bytes){
        writer.println(bytes);
    }

    void sendMessage(String message){
        sendPacket(new PacketMessage(message));
    }

}