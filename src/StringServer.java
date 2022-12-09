import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class StringServer {
    private final ServerSocket server;

    public StringServer(int port) throws IOException {
        server = new ServerSocket(port);
    }

    private void verbinde() {
        System.out.println("Server started");

        while (true) {
            Socket socket = null;
            try {
                socket = server.accept();
                reinRaus(socket);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (socket != null)
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
    }

    private void reinRaus(Socket socket) throws IOException {
        System.out.println("New connection");
        BufferedReader rein = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintStream raus = new PrintStream(socket.getOutputStream());
        String s;
        raus.println("Welcome to the Server");

        while (rein.ready()) {
            s = rein.readLine();
            raus.println(s);
        }
    }

    public static void main(String[] args) throws IOException {
        StringServer server = new StringServer(3141);
        server.verbinde();
    }
}