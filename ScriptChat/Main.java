public class Main {

    public static ChatClient client;

    public Main(){
        UI.init();
    }

    public static void main(String[] args) {
        new Main();
    }

    public static void start(int port){
        client = new ChatClient("localhost", port);
        client.start();
    }
}
