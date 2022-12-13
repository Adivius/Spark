public class Main {

    public static ChatClient client;

    public Main(String ip, int port){
        //UI.init();
        client = new ChatClient("localhost", 1243);
        client.start();
    }

    public static void main(String[] args) {
        new Main(args[0], Integer.parseInt( args[1]));
    }

    /*public static void start(int port){
        client = new ChatClient("localhost", port);
        client.start();
    }*/
}
