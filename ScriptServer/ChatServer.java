import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ChatServer {
    private final int port;
    private final Set<String> userNames = new HashSet<>();
    private final Set<UserThread> userThreads = new HashSet<>();
    boolean running = false;
    private ServerSocket serverSocket;

    public ChatServer(int port) {
        this.port = port;
    }

    public void start(ChatServer server) {
        running = true;

        class MultithreadingDemo extends Thread {
            public void run() {
                try {
                    serverSocket = new ServerSocket(port);
                    print("Chat Server is listening on port " + port);
                    while (!serverSocket.isClosed()) {
                        Socket socket = serverSocket.accept();
                        print("New user connected");
                        UserThread newUser = new UserThread(socket, server);
                        userThreads.add(newUser);
                        newUser.start();
                    }
                    this.interrupt();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        MultithreadingDemo multithreadingDemo = new MultithreadingDemo();
        multithreadingDemo.start();
    }

    /**
     * Delivers a message from one user to others (broadcasting)
     */
    void broadcast(String message, UserThread excludeUser) {
        for (UserThread aUser : userThreads) {
            if (aUser != excludeUser) {
                aUser.sendMessage(message);
            }
        }
    }

    void shutdown(){
        for (UserThread userThread: userThreads){
            userThread.shutdown();
            userThreads.remove(userThread);
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        running = false;
        print("Server closed");
    }

    /**
     * Stores username of the newly connected client.
     */
    void addUserName(String userName) {
        userNames.add(userName);
    }

    /**
     * When a client is disconneted, removes the associated username and UserThread
     */
    void removeUser(String userName, UserThread aUser) {
        boolean removed = userNames.remove(userName);
        if (removed) {
            userThreads.remove(aUser);
            print("The user " + userName + " quitted");
        }
    }

    Set<String> getUserNames() {
        return this.userNames;
    }

    /**
     * Returns true if there are other users connected (not count the currently connected user)
     */
    boolean hasUsers() {
        return !this.userNames.isEmpty();
    }

    public static void  print(String message){
        System.out.println(message);
        ServerUI.textArea.setText(ServerUI.textArea.getText() + message + '\n');
    }
}