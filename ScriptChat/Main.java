public class Main {

    public static ChatClient client;

    public Main(String ip, int port){
        UI.init();
        client = new ChatClient(ip, port);
        client.start();
    }

    public static void main(String[] args) {
        if (args.length < 2){
            return;
        }
        new Main(args[0], Integer.parseInt( args[1]));
    }
}
