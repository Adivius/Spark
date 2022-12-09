import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

class StringClient {

    public static void main(String[] args) {
        Socket socket = null;
        try {
            socket = new Socket("localhost", 3141);

            OutputStream raus = socket.getOutputStream();
            PrintStream ps = new PrintStream(raus, true);
            ps.println("Hallo Welt!");

            InputStream rein = socket.getInputStream();
            System.out.println("verf\u00FCgbare Bytes: " + rein.available());
            BufferedReader buff = new BufferedReader(new InputStreamReader(rein));

            while (buff.ready()) {
                System.out.println(buff.readLine());
            }

        } catch (UnknownHostException e) {
            System.out.println("Unknown Host...");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IOProbleme...");
            e.printStackTrace();
        } finally {
            if (socket != null)
                try {
                    socket.close();
                    System.out.println("Socket geschlossen...");
                } catch (IOException e) {
                    System.out.println("Socket nicht zu schliessen...");
                    e.printStackTrace();
                }
        }
    }
}