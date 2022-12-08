import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {

    private final ArrayList<ConnectionHandler> connections;
    private ServerSocket server;
    private boolean running;
    private ExecutorService pool;


    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }


    public Server() {
        connections = new ArrayList<>();
    }

    @Override
    public void run() {
        running = true;
        try {
            server = new ServerSocket(1243);
            pool = Executors.newCachedThreadPool();
            while (running) {
                Socket client = server.accept();
                ConnectionHandler handler = new ConnectionHandler(client);
                connections.add(handler);
                pool.execute(handler);
            }
        } catch (Exception e) {
            shutdown();
        }
    }

    public void shutdown() {
        try {
            running = false;
            if (!server.isClosed()) {
                server.close();
            }
            for (ConnectionHandler connectionHandler : connections) {
                connectionHandler.shutdown();
            }
        } catch (IOException ignored) {
        }
    }

    public void broadcast(String message) {
        for (ConnectionHandler connectionHandler : connections) {
            if (connectionHandler != null) {
                connectionHandler.sendMessage(message);
            }
        }
    }


    class ConnectionHandler implements Runnable {

        private final Socket client;
        private BufferedReader in;
        private PrintWriter out;
        private String nickname;

        public ConnectionHandler(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(client.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                out.println("Please enter a Nickname: ");
                System.out.println("User Connected");
                nickname = in.readLine();
                System.out.println(nickname + " connected!");
                broadcast(nickname + " joined the chat");
                String message;
                while ((message = in.readLine()) != null) {
                    if (message.startsWith("/nick ")) {
                        String[] messageSplit = message.split(" ", 2);
                        if (messageSplit.length == 2) {
                            broadcast(nickname + " renamed to " + messageSplit[1]);
                            nickname = messageSplit[1];
                        } else {
                            out.println("Nickname couldn't switch");
                        }
                    } else if (message.startsWith("/quit")) {
                        broadcast(nickname + " left the chat!");
                        shutdown();

                    } else {
                        broadcast(nickname + ": " + message);
                    }
                }
            } catch (IOException e) {
                //TODO: handle
            }
        }

        public void shutdown() {
            try {
                in.close();
                out.close();
                if (!client.isClosed()) {
                    client.close();
                }
            } catch (Exception ignored) {
            }
        }

        public void sendMessage(String message) {
            out.println(message);
        }
    }
}
