public class ServerMain {

    public static ChatServer server;

    public ServerMain(){
        ServerUI.init();
    }

    public static void start(int port){
        class ServerThread extends Thread {
            public void run() {
                ServerUI.connectButton.setText("Stop");
                server = new ChatServer(port);
                server.start(server);
            }
        }
        ServerThread serverThread = new ServerThread();
        serverThread.start();

    }

    public static void stop(){
        ServerUI.connectButton.setText("Start");
        server.shutdown();
        server = null;
    }

    public static void main(String[] args) {
        new ServerMain();
    }



}
