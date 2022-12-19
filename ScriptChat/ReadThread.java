import ScriptServer.packets.PacketDisconnect;
import ScriptServer.packets.PacketIds;
import ScriptServer.packets.PacketMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReadThread extends Thread {
    private BufferedReader reader;
    private ScriptClient client;
    private Socket socket;

    public ReadThread(Socket socket, ScriptClient client) {
        this.socket = socket;
        try {
            this.client = client;
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException ex) {
            UI.print("Error getting input stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void run() {
        String shutdownReason = null;
        loop: while (!socket.isClosed()) {
            try {
                if (!reader.ready()){
                    continue;
                }
                String response;
                if((response = reader.readLine()) == null){
                    break;
                }
                String[] packet = response.split(PacketIds.SEPARATOR);
                int packetID = Integer.parseInt(packet[0]);

                switch (packetID) {
                    case PacketIds.MESSAGE:
                        PacketMessage packetMessage = new PacketMessage(packet);
                        UI.print("\n" + packetMessage.MESSAGE);
                        break;
                    case PacketIds.DISCONNECT:
                        PacketDisconnect packetDisconnect = new PacketDisconnect(packet);
                        shutdownReason = packetDisconnect.REASON;
                        break loop;

                }
            } catch (IOException ex) {
                UI.print("Error reading from server: " + ex.getMessage());
                ex.printStackTrace();
                break;
            }
        }
        client.shutdown(shutdownReason);
    }

    public void shutdown() throws IOException {
        if (!socket.isClosed()){
            socket.close();
        }
        reader.close();
        this.interrupt();
    }

}