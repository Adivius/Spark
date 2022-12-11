public class ServerMain {

    public static ChatServer server;

    public ServerMain(){
        ServerUI.init();
    }

    public static void start(int port){
        ServerUI.connectButton.setText("Stop");
        server = new ChatServer(port);
        server.start(server);
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
