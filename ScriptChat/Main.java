public class Main {

    public Main(String ip, int port){
        UI.init();
        ChatClient client = new ChatClient(ip, port);
        client.start();
        UI.chatClient = client;
    }

    public static void main(String[] args) {
        try {
            Main main = new Main(args[0], Integer.parseInt( args[1]));
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
