import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Arrays;

public class ReadThread extends Thread {
    private final Socket socket;
    private BufferedReader reader;
    private ScriptClient client;

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
        loop:
        while (!socket.isClosed()) {
            try {
                if (!reader.ready()) {
                    continue;
                }
                String response;
                if ((response = reader.readLine()) == null) {
                    break;
                }
                String[] packet = response.split(PacketIds.SEPARATOR);
                int packetID = Integer.parseInt(packet[0]);

                switch (packetID) {
                    case PacketIds.MESSAGE:
                        PacketMessage packetMessage = new PacketMessage(packet);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MMM/yyyy HH:mm");
                        String time = simpleDateFormat.format(packetMessage.TIMESTAMP);
                        UI.print("[" + packetMessage.SENDER + ", " + time + "]: " + packetMessage.MESSAGE);
                        break;
                    case PacketIds.DISCONNECT:
                        PacketDisconnect packetDisconnect = new PacketDisconnect(packet);
                        shutdownReason = packetDisconnect.REASON;
                        break loop;
                    case PacketIds.LOG:
                        String[] message = Arrays.copyOfRange(packet, 1, packet.length);
                        PacketLog packetLog = new PacketLog(String.join("\n", message));
                        if (packetLog.MESSAGE.isEmpty()) {
                            continue;
                        }
                        UI.print("System: " + packetLog.MESSAGE);
                        break;
                    case PacketIds.NAME:
                        PacketName packetName = new PacketName(packet);
                        if (!packetName.NAME.isEmpty()) {
                            client.setUserName(packetName.NAME);
                        }
                        break;
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
        if (!socket.isClosed()) {
            socket.close();
        }
        reader.close();
        this.interrupt();
    }
}