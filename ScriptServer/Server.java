public class Server {

    public static ChatServer server;

    public Server(int port){
        class ServerThread extends Thread {
            public void run() {
                server = new ChatServer(port);
                server.start(server);
            }
        }
        ServerThread serverThread = new ServerThread();
        serverThread.start();
    }

    public static void main(String[] args) {
        try{
            new Server(Integer.parseInt(args[0]));
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
