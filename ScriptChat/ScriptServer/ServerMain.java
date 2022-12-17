package ScriptServer;

public class ServerMain {

    public static ScriptServer server;

    public ServerMain(int port){
        class ServerThread extends Thread {
            public void run() {
                server = new ScriptServer(port);
                server.start();
            }
        }
        ServerThread serverThread = new ServerThread();
        serverThread.start();
    }

    public static void main(String[] args) {
        try{
            new ServerMain(Integer.parseInt(args[0]));
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
